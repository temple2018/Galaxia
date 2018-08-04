package org.templegalaxia.patterns.alanpersak;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.BoundedParameter;

import java.util.*;
import java.util.stream.IntStream;
import org.templegalaxia.model.Petal;
import org.templegalaxia.patterns.TemplePattern;
import processing.core.PApplet;

@LXCategory("Alan Persak")
public class WavePattern extends TemplePattern {

    private static final double TAU = 2 * Math.PI;
    private static final double TIME_SCALE = 5;
    private static final double DECAY = 5000;
    private static final int MIN_BRIGHTNESS = 10;
    private static final double RAMP = 2000;
    private static final double SPEED = 500;

    private Random random = new Random();
    private LinkedList<Wave> waves = new LinkedList<Wave>();
    private int numPoints;
    private double[] angles;
    private double[] radii;
    private double waveTimer;
    private double nextWaveTime = 0;

    public WavePattern(LX lx) {
        super(lx);

        // Precompute point data
        numPoints = model.points.length;
        angles = new double[numPoints];
        radii = new double[numPoints];
        for (int i=0; i<numPoints; i++){
            LXPoint point = model.points[i];
            angles[i] = angle(point);
            radii[i] = Math.hypot(point.x, point.z);
        }
    }

    double printTimer = 0;
    int frameCounter = 0;

    public void run(double deltaMs) {
        frameCounter++;

        // Print stats every 10 real seconds.
        printTimer += deltaMs / 1000;
        if(printTimer > 10){
            /*double size = 1;
            double direction = dir;
            dir += Math.PI/2;
            if(dir >= 2*Math.PI){
                dir = 0;
            }
            System.out.println("new wave " + size + " " + direction);
            waves.add(new Wave(MIN_POSITION, 1000, size, direction, 100));*/

            System.out.println("\nstats:");
            System.out.println("wave count: " + waves.size());
            System.out.println("frame rate: " + frameCounter / waveTimer);

            printTimer = 0;
            frameCounter = 0;
        }

        // Create new waves based on scaled time.
        double scaledDelta = deltaMs * TIME_SCALE;
        waveTimer += scaledDelta / 1000;
        if(waveTimer > nextWaveTime){
            waveTimer = 0;
            nextWaveTime = randomDouble(random, 5, 10, 100);

            double direction = randomDouble(random, 0, TAU, 360);
            double size = randomDouble(random, .5, 3, 100);
            waves.add(new Wave(MIN_POSITION, SPEED, size, direction, 100));

            System.out.println("\nnew wave: " + direction + " " + size);
            System.out.println("next wave: " + nextWaveTime);
        }

        // Turn everything off.
        for (LXPoint p : model.points) {
            //colors[p.index] = LXColor.BLACK;
            colors[p.index] = LXColor.hsb(0, 0, MIN_BRIGHTNESS);
        }

        // Animate the waves.
        for(Iterator<Wave> it = waves.iterator(); it.hasNext(); ){
            Wave wave = it.next();
            wave.run(scaledDelta);
            if(wave.done()){
                it.remove();
            }
        }
    }

    private static double MIN_POSITION = 1000; // Minimum measured radius: 3508
    private static final double MAX_POSITION = 60000;  // Maximum measured radius: 28769

    private class Wave {

        double direction; // Direction of the center of the wave, in radians.
        double minAngle;
        double maxAngle;
        double speed;     // Rate that wave's arc radius expands.
        double position;  // Current arc radius.
        double intensity; // Maximum brightness at the leading edge of the wave.

        public Wave(double position, double speed, double size, double direction, double intensity) {
            this.position = position;
            this.speed = speed;
            this.minAngle = clampAngle(direction - size/2);
            this.maxAngle = clampAngle(direction + size/2);
            this.direction = direction;
            this.intensity = intensity;
        }

        public void run(double deltaMs) {
            position += speed * deltaMs / 1000;

            for (int i=0; i<numPoints; i++){
                double angle = angles[i];
                if (!isAngleInRange(angle, minAngle, maxAngle)){
                    continue;
                }
                double radius = radii[i];
                if (radius > position + RAMP) {
                    continue;
                }
                double brightness = LXColor.b(colors[model.points[i].index]);
                if (radius > position) {
                    brightness = (int)linearInterpolate(position + RAMP - radius, 0, brightness, RAMP, intensity);

                } else {
                    brightness = (int) (intensity * Math.pow(2, (radius - position) / DECAY));
                    brightness = Math.max(brightness, MIN_BRIGHTNESS);
                }
                colors[model.points[i].index] = LXColor.hsb(0, 0, brightness);
            }
        }

        public boolean done() {
            return position > MAX_POSITION;
        }
    }

    // Returns whether an angle is within a range, while accounting for the boundary. All values are expected to be
    // in range [0, 2PI).
    private static boolean isAngleInRange(double angle, double min, double max){
        if (min < max) {
            return angle >= min && angle <= max;
        }
        // else the range wraps around the circle boundary.
        return angle >= min || angle < max;
    }

    // Limits an angle in radians to range [0, 2PI).
    private static double clampAngle(double angle) {
        while(angle < 0){
            angle += TAU;
        }
        while (angle >= TAU){
            angle -= TAU;
        }
        return angle;
    }

    private static double angle(LXPoint point) {
        return Math.atan2(point.z, point.x) + Math.PI;
    }

    private static double randomDouble(Random r, double min, double max, int precision){
        return r.nextInt(precision) * (max - min) / precision + min;
    }

    private static double linearInterpolate(double x, double x0, double y0, double x1, double y1){
        return (y0*(x1-x)+y1*(x-x0))/(x1-x0);
    }
}
