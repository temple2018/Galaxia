package org.templegalaxia.patterns.ping;

import org.templegalaxia.patterns.TemplePattern;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.DiscreteParameter;

@LXCategory("Ping")
public class Swirl extends TemplePattern {
  private CompoundParameter speedParam = new CompoundParameter("speed", 0, -0.1, 0.1);  // rev/s
  private CompoundParameter massParam = new CompoundParameter("mass", 10, 0.1, 20);  // kg
  private CompoundParameter flexParam = new CompoundParameter("flex", 1, 0, 4);  // rev
  private CompoundParameter stiffnessParam = new CompoundParameter("stiffness", 1000, 0, 5000);  // kg/s^2
  private CompoundParameter frictionParam = new CompoundParameter("friction", 1, 0, 2);  // kg rev/s
  private DiscreteParameter spokesParam = new DiscreteParameter("spokes", 6, 1, 21);  // 1/rev
  private CompoundParameter biasParam = new CompoundParameter("bias", 0, -10, 10);  // unitless

  private static final double TAU = 2 * Math.PI;
  private static final int NUM_SUBFRAMES = 100;
  private static final int NUM_SEGMENTS = 100;
  private double[] pos = new double[NUM_SEGMENTS + 1];  // rev
  private double[] vel = new double[NUM_SEGMENTS + 1];  // rev/s

  private double[] newPos = new double[NUM_SEGMENTS + 1];
  private double[] newVel = new double[NUM_SEGMENTS + 1];

  public Swirl(LX lx) {
    super(lx);
    speedParam.setDescription("Rotational speed of motor driving the chain from the top (revolutions per second)");
    addParameter(speedParam);
    massParam.setDescription("Mass of each chain element (kilograms)");
    addParameter(massParam);
    flexParam.setDescription("Range of flexibility, maximum difference in rotational position between top and bottom (revolutions)");
    addParameter(flexParam);
    stiffnessParam.setDescription("Stiffness of springs between chain elements (kilograms per second squared)");
    addParameter(stiffnessParam);
    frictionParam.setDescription("Constant of kinetic friction (kilogram-revolutions per second squared)");
    addParameter(frictionParam);
    spokesParam.setDescription("Order of rotational symmetry (number per revolution)");
    addParameter(spokesParam);
    biasParam.setDescription("Bias in rendered brightness (positive -> more white, negative -> more black)");
    addParameter(biasParam);
  }

  public void run(double deltaMs) {
    for (int i = 0; i < NUM_SUBFRAMES; i++) {
      advance(deltaMs / 1000.0 / NUM_SUBFRAMES);
    }
    render();
  }

  public void render() {
    double b = biasParam.getValue();
    int spokes = spokesParam.getValuei();
    for (LXPoint p : model.points) {
      double angle = revs(p.x, p.z) * spokes + getPos(p.y);
      colors[p.index] = LXColor.gray(100 * bias(wave(angle), b));
    }
  }

  /** Calculates the polar angle of a 2-D vector, measured in revolutions. */
  private double revs(double x, double y) {
    return Math.atan2(y, x) / TAU;
  }

  /** A sinusoid with period 1, range from 0 to 1, and initial value 0. */
  private double wave(double p) {
    return (1 - Math.cos(p * TAU)) / 2;
  }

  /** Shifts a value in the range [0..1] toward 0 (negative bias) or toward 1 (positive bias). */
  private double bias(double v, double bias) {
    return Math.pow(v, Math.exp(-bias));
  }

  private void advance(double deltaSec) {
    // Calculations are in kg, m, s units, assuming 1 rev = 1 m
    double speed = speedParam.getValue();
    double range = flexParam.getValue() / NUM_SEGMENTS;
    double stiffness = stiffnessParam.getValue();
    double friction = frictionParam.getValue();
    double mass = massParam.getValue();
    newPos[0] = pos[0] + speed * deltaSec;
    for (int i = 1; i < NUM_SEGMENTS + 1; i++) {
      double force = 0;
      if (i > 0) force += stiffness * (pos[i - 1] - pos[i]);
      if (i < NUM_SEGMENTS) force += stiffness * (pos[i + 1] - pos[i]);
      force += (vel[i] < 0 ? friction : -friction); //-= vel[i] * friction;
      double accel = force / mass;
      newVel[i] = vel[i] + accel * deltaSec;
      newPos[i] = pos[i] + vel[i] * deltaSec;
      newPos[i] = clamp(newPos[i], newPos[i - 1] - range, newPos[i - 1] + range);
    }
    double[] tmp;
    tmp = vel; vel = newVel; newVel = tmp;
    tmp = pos; pos = newPos; newPos = tmp;
  }

  private double getPos(double y) {
    double yFrac = (y - model.yMax) / (model.yMin - model.yMax);  // 0 at top, 1 at bottom
    double index = yFrac * NUM_SEGMENTS;
    int before = clamp((int) Math.floor(index), 0, NUM_SEGMENTS);
    int after = clamp((int) Math.ceil(index), 0, NUM_SEGMENTS);
    if (before == after) return pos[before];
    double frac = index - before;
    return pos[before] * (1 - frac) + pos[after] * frac;
  }

  private static int clamp(int v, int min, int max) {
    return v < min ? min : v > max ? max : v;
  }

  private static double clamp(double v, double min, double max) {
    return v < min ? min : v > max ? max : v;
  }
}
