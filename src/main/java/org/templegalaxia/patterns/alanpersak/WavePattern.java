package org.templegalaxia.patterns.alanpersak;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BoundedParameter;

import java.util.*;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("Alan Persak")
public class WavePattern extends TemplePattern {
    private static final double TAU = 2 * Math.PI;
    private static double MIN_POSITION = 0; // Initial wave radius. Minimum pixel radius is 3508.
    private static final double MAX_POSITION = 100000; // Maximum wave radius before it is removed from the simulation. Maximum pixel radius is 28769.

    private BoundedParameter timeScaleParam = new BoundedParameter("Time Scale", 1, .1, 10).setDescription("Scales the speed of the wave simulation.");
    private BoundedParameter decayParam = new BoundedParameter("Decay Rate", 10000, 1000, 100000).setDescription("The rate of exponential decay behind the wave front, as half-life distance. E.g. at 1000, the brightness will halve every 1000 distance units from the wave front.");;
    private BoundedParameter fadeDistanceParam = new BoundedParameter("Fade Distance", 1500, 0,10000).setDescription("The distance from the wave front over which the wave fades in. The brightness is linearly interpolated from the current brightness to the wave's brightness.");
    private BoundedParameter fadeAngleParam = new BoundedParameter("Fade Angle", .2, 0, .5).setDescription("The angle over which the brightness fades at the edge of the wave.");
    private BoundedParameter baseBrightnessParam = new BoundedParameter("Base Brightness", 5, 0, 100).setDescription("The background brightness.");
    private BoundedParameter waveBrightnessParam = new BoundedParameter("Wave Brightness", 100, 0, 200).setDescription("The brightness at the peak of the wave front. Going above 100 will create a flat top where the brightness is saturated.");
    private BoundedParameter waveSpeedParam = new BoundedParameter("Wave Speed", 500, 100, 10000).setDescription("The rate at which the wave arc radius expands.");
    private BoundedParameter minWaveAngleParam = new BoundedParameter("Minimum Wave Angle", 1, 0, TAU).setDescription("The minimum angle of the randomized wave size.");
    private BoundedParameter maxWaveAngleParam = new BoundedParameter("Maximum Wave Angle", 3, 0, TAU).setDescription("The maximum angle of the randomized wave size.");
    private BoundedParameter minWaveTimeParam = new BoundedParameter("Minimum Wave Period", 20, 1, 300).setDescription("The minimum time of the randomized period between waves, in seconds.");
    private BoundedParameter maxWaveTimeParam = new BoundedParameter("Maximum Wave Period", 30, 1, 300).setDescription("The maximum time of the randomized period between waves, in seconds.");
    private BoundedParameter offsetParam = new BoundedParameter("Direction Offset", Math.PI/2, 0, Math.PI).setDescription("The minimum angle between successive wave directions, which allows for a more balanced distribution.");

    private Random random = new Random();
    private LinkedList<Wave> waves = new LinkedList<Wave>();
    private int numPoints; // Number of points/pixels.
    private double[] angles; // Angles of each point from the origin.
    private double[] radii; // Radii of each point from the origin.
    private double waveTimer; // Timer which counts up to next wave.
    private double nextWaveTime = 0; // Time of next wave.
    private double waveSpeed = 0; // Rate of increase of a wave's radius.
    private double waveBrightness = 0; // Maximum brightness at a wave's peak.
    private double lastWaveDirection = 0; // Direction of last wave, used to offset the directions of successive waves.
    double statTimer = 0; // Timer which counts for printing stats.
    int frameCounter = 0; // Number of animation frames.

    public WavePattern(LX lx) {
        super(lx);

        addParameter(timeScaleParam);
        addParameter(decayParam);
        addParameter(fadeDistanceParam);
        addParameter(fadeAngleParam);
        addParameter(baseBrightnessParam);
        addParameter(waveBrightnessParam);
        addParameter(waveSpeedParam);
        addParameter(minWaveAngleParam);
        addParameter(maxWaveAngleParam);
        addParameter(minWaveTimeParam);
        addParameter(maxWaveTimeParam);
        addParameter(offsetParam);

        // Precompute point data in lookup tables for efficiency.
        numPoints = model.points.length;
        angles = new double[numPoints];
        radii = new double[numPoints];
        for (int i=0; i<numPoints; i++){
            LXPoint point = model.points[i];
            angles[i] = angle(point);
            radii[i] = Math.hypot(point.x, point.z);
        }
    }

    public void run(double deltaMs) {
        waveSpeed = waveSpeedParam.getValue();
        waveBrightness = waveBrightnessParam.getValue();

        // Print stats every 10 real seconds.
        frameCounter++;
        statTimer += deltaMs / 1000;
        if(statTimer > 10){
            System.out.println("\nstats:");
            System.out.println("wave count: " + waves.size());
            System.out.println("frame rate: " + frameCounter / statTimer);
            frameCounter = 0;
            statTimer = 0;
        }

        // Create new waves based on scaled time.
        double scaledDelta = deltaMs * timeScaleParam.getValue();
        waveTimer += scaledDelta / 1000;
        if(waveTimer > nextWaveTime){
            waveTimer = 0;
            double minWaveTime = minWaveTimeParam.getValue();
            double maxWaveTime = Math.max(minWaveTime, maxWaveTimeParam.getValue());
            nextWaveTime = randomDouble(random, minWaveTime, maxWaveTime, 1000);

            // To get a more balanced distribution of wave directions, the next wave direction is offset from the
            // the previous wave direction.
            double offset = offsetParam.getValue();
            double minDirection = lastWaveDirection + offset;
            double maxDirection = lastWaveDirection - offset + TAU;
            double direction = randomDouble(random, minDirection, maxDirection, 1000);
            direction = clampAngle(direction);
            lastWaveDirection = direction;

            double size = randomDouble(random, minWaveAngleParam.getValue(), maxWaveAngleParam.getValue(), 1000);

            waves.add(new Wave(direction, size));

            System.out.println("\nnew wave: " + direction + " " + size);
            System.out.println("next wave time: " + nextWaveTime);
        }

        // Set every pixel to base brightness.
        for (LXPoint p : model.points) {
            colors[p.index] = LXColor.hsb(0, 0, baseBrightnessParam.getValue());
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

    private class Wave {
        double minAngle;  // Minimum wave angle. Equal to direction - size/2.
        double maxAngle;  // Maximum wave angle. Equal to direction + size/2.
        double position = MIN_POSITION;  // Current arc radius.

        public Wave(double direction, double size) {
            this.minAngle = clampAngle(direction - size/2);
            this.maxAngle = clampAngle(direction + size/2);
        }

        public void run(double deltaMs) {
            double fadeDistance = fadeDistanceParam.getValue();
            double fadeAngle = fadeAngleParam.getValue();
            double decay = decayParam.getValue();
            double baseBrightness = baseBrightnessParam.getValue();

            position += waveSpeed * deltaMs / 1000;

            for (int i=0; i<numPoints; i++){
                double angle = angles[i];
                if (!isAngleInRange(angle, minAngle, maxAngle)){
                    // Pixel is outside of wave angle. Skip it.
                    continue;
                }
                double radius = radii[i];
                if (radius > position) {
                    // Pixel is outside of wave radius. Skip it.
                    continue;
                }
                double oldBrightness = LXColor.b(colors[model.points[i].index]);
                double brightness;
                if (radius > position - fadeDistance) {
                    // Pixel is between peak and fade distance. Fade it in with linear interpolation.
                    brightness = (int)Util.linearInterpolate(position - radius, 0, oldBrightness, fadeDistance, waveBrightness);
                    brightness = Math.min(brightness, 100);

                } else {
                    // Pixel is within the wave peak. Brightness decays exponentially with distance from peak.
                    brightness = (int) (waveBrightness * Math.pow(2, (radius - position + fadeDistance) / decay));
                    brightness = Math.max(brightness, baseBrightness);
                    brightness = Math.min(brightness, 100);
                    // TODO: maybe exponential decay should be offset by base brightness.
                }
                if (isAngleInRange(angle, maxAngle - fadeAngle, maxAngle)){
                    // Pixel is within upper fade angle. Fade it in.
                    brightness = (int)Util.linearInterpolate(maxAngle - angle, 0, oldBrightness, fadeAngle, brightness);
                }
                if (isAngleInRange(angle, minAngle, minAngle + fadeAngle)){
                    // Pixel is within lower fade angle. Fade it in.
                    brightness = (int)Util.linearInterpolate(angle - minAngle, 0, oldBrightness, fadeAngle, brightness);
                }
                colors[model.points[i].index] = LXColor.hsb(0, 0, brightness);
            }
        }

        // Returns whether to remove the wave from the simulation.
        public boolean done() {
            // TODO: it might be better to compute whether the least bright pixel is within a threshold of the base
            // brightness, but that could risk allowing too many waves to slow down the animation.
            return position > MAX_POSITION;
        }
    }

    // Returns whether an angle is within a range. Values are expected to be in range [0, 2PI).
    private static boolean isAngleInRange(double angle, double min, double max){
        if (min < max) {
            return angle >= min && angle <= max;
        }
        // The range wraps around the circle boundary.
        return angle >= min || angle < max;
    }

    // Limits an angle to range [0, 2PI).
    private static double clampAngle(double angle) {
        while(angle < 0){
            angle += TAU;
        }
        while (angle >= TAU){
            angle -= TAU;
        }
        return angle;
    }

    // Returns the angle from a point to the origin;
    private static double angle(LXPoint point) {
        return Math.atan2(point.z, point.x) + Math.PI;
    }

    // Returns a random double between min and max with specified steps of precision.
    private static double randomDouble(Random r, double min, double max, int precision){
        return r.nextInt(precision) * (max - min) / precision + min;
    }
}
