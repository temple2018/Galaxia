package org.templegalaxia.patterns.cbarnes;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.BoundedParameter;
import java.math.*;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("cbarnes")
public class RingFall extends TemplePattern {
  BoundedParameter y_speed =
      new BoundedParameter("Y-speed", 40000, 60000, 20000)
          .setDescription("The speed of the up and down movement of the ring");
  BoundedParameter rot_speed =
      new BoundedParameter("Rot-vel", 40000, 60000, 20000)
          .setDescription("The speed that the ring moves around the center");
  BoundedParameter ang_speed =
      new BoundedParameter("Ang-vel", 40000, 60000, 20000)
          .setDescription("How fast the tilt of the ring changes");

  SinLFO saw = new SinLFO(model.yMin - model.yRange / 2, model.yMax + model.yRange / 2, y_speed);
  SinLFO basis = new SinLFO(0.0, 1.0, rot_speed);
  SinLFO angle = new SinLFO(0.0, 1.0, ang_speed);

  public RingFall(LX lx) {
    super(lx);
    addModulator(saw).trigger();
    addModulator(basis).trigger();
    addModulator(angle.randomBasis()).trigger();
    addParameter(y_speed);
    addParameter(rot_speed);
    addParameter(ang_speed);
  }

  public void run(double deltaMs) {
    float currentY = saw.getValuef();
    double r = angle.getValue();
    double theta = basis.getBasis() * Math.PI * 2;

    // Point on the plane
    Vector pl = new Vector(model.cx, currentY, model.cz);

    // Normal vector of the plane
    Vector n = new Vector(r * Math.cos(theta), 1.0, r * Math.sin(theta));

    // Max distance to light up
    float max_d = model.yRange / 4;

    for (LXPoint p : model.points) {

      // Distance from the point to the plane
      double distance =
          Math.abs(n.x * (p.x - pl.x) + n.y * (p.y - pl.y) + n.z * (p.z - pl.z)) / n.length();
      double scaled = (max_d - distance) / max_d;

      if (distance < max_d) {
        colors[p.index] = LXColor.gray(100 * scaled);
      } else {
        colors[p.index] = LXColor.BLACK;
      }
    }
  }

  class Vector {
    public double x;
    public double y;
    public double z;

    public Vector(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    public double length() {
      return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }
  }
}
