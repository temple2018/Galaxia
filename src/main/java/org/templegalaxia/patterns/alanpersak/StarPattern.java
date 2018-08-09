package org.templegalaxia.patterns.alanpersak;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BoundedParameter;
import java.util.*;
import org.templegalaxia.patterns.TemplePattern;

/**
 * An animation of randomized stars. A star is defined by its "center" pixel, size in pixels, brightness and lifetime.
 * Each star occupies a number of pixels equal to its size, plus one empty pixel on either side, and stars cannot
 * overlap. Stars are created with randomized parameters in random locations and fade in and out over the course of
 * their lifetimes.
 */
@LXCategory("Alan Persak")
public class StarPattern extends TemplePattern {

    private BoundedParameter timeScaleParam = new BoundedParameter("Time Scale", 1, .1, 20).setDescription("Scales the speed of animation.");
    private BoundedParameter rateParam = new BoundedParameter("Rate", 6, 1, 50).setDescription("The probability of a star being created. A rate of 10 means on average 10 stars per second when no stars are present. The actual rate will be less since stars cannot overlap.");
    private BoundedParameter minSizeParam = new BoundedParameter("Minimum Size", 1, 1, 10).setDescription("The minimum size of a star in pixels.");
    private BoundedParameter maxSizeParam = new BoundedParameter("Maximum Size", 3, 1, 10).setDescription("The maximum size of a star in pixels.");
    private BoundedParameter minBrightnessParam = new BoundedParameter("Minimum Brightness", 40, 0, 100);
    private BoundedParameter maxBrightnessParam = new BoundedParameter("Maximum Brightness", 100, 0, 100);
    private BoundedParameter brightnessStepsParam = new BoundedParameter("Brightness Steps", 3, 1, 10).setDescription("The number of brightness steps.");
    private BoundedParameter minLifespanParam = new BoundedParameter("Minimum Lifespan", 60, 1, 1000).setDescription("The minimum duration of randomized star lifetimes.");
    private BoundedParameter maxLifespanParam = new BoundedParameter("Maximum Lifespan", 300, 1, 1000).setDescription("The maximum duration of randomized star lifetimes.");
    private BoundedParameter fadeTimeParam = new BoundedParameter("Fade Time", 30, 1, 300).setDescription("The amount of time for a star to fade in and out.");
    private BoundedParameter baseBrightnessParam = new BoundedParameter("Base Brightness", 0, 0, 100).setDescription("The background brightness.");

    private final int numPixels;
    private final int numPixelsPerPetal;

    private Random random = new Random();
    private LinkedList<Star> stars = new LinkedList<Star>();
    private HashSet<Integer> usedPoints = new HashSet<Integer>(); // Set of indicies of used pixels. Stars cannot occupy pixels that are already used.
    private double statTimer = 0;
    private int starCounter = 0; // Counts new stars to print rate stats.
    private int numLitPixels = 0;

    public StarPattern(LX lx) {
        super(lx);

        addParameter(timeScaleParam);
        addParameter(rateParam);
        addParameter(minSizeParam);
        addParameter(maxSizeParam);
        addParameter(minBrightnessParam);
        addParameter(maxBrightnessParam);
        addParameter(brightnessStepsParam);
        addParameter(minLifespanParam);
        addParameter(maxLifespanParam);
        addParameter(fadeTimeParam);
        addParameter(baseBrightnessParam);

        numPixels = model.points.length;
        numPixelsPerPetal = model.petals.get(0).numPixels;
    }

    public void run(double deltaMs) {

        double timeScale = timeScaleParam.getValue();
        double rate = rateParam.getValue();
        int minSize = (int) minSizeParam.getValue();
        int maxSize = Math.max(minSize, (int) maxSizeParam.getValue());
        double minBrightness = minBrightnessParam.getValue();
        double maxBrightness = Math.max(minBrightness, maxBrightnessParam.getValue());
        int brightnessSteps = (int)brightnessStepsParam.getValue();
        double minLifespan = minLifespanParam.getValue();
        double maxLifespan = Math.max(minLifespan, maxLifespanParam.getValue());
        double fadeTime = fadeTimeParam.getValue();
        double baseBrightness = baseBrightnessParam.getValue();

        double scaledDeltaMs = deltaMs * timeScale;

        // Print stats every 10 seconds.
        statTimer += deltaMs / 1000;
        if (statTimer > 10) {
            System.out.println("\nstats:");
            System.out.println("total pixel count: " + numPixels);
            System.out.println("star count: " + stars.size());
            System.out.println("scaled star rate: " + starCounter / statTimer / timeScale);
            System.out.println("used pixel count: " + usedPoints.size());
            System.out.println("lit pixel count: " + numLitPixels);
            starCounter = 0;
            statTimer = 0;
        }

        // Set everything to base brightness.
        for (LXPoint p : model.points) {
            colors[p.index] = LXColor.hsb(0, 0, baseBrightness);
        }

        // Create new stars.
        for (int petalIndex = 0; petalIndex < model.petals.size(); petalIndex++){
            for(int pixelIndex = 0; pixelIndex < model.petals.get(petalIndex).numPixels; pixelIndex++){
                if (!shouldCreateStar(random, rate, numPixels, scaledDeltaMs)){
                    continue;
                }

                Star star = createRandomStar(petalIndex, pixelIndex, minSize, maxSize, minBrightness, maxBrightness, brightnessSteps, minLifespan, maxLifespan);
                if (addPointsIfUnused(usedPoints, star.points)){
                    stars.add(star);
                    starCounter++;
                    numLitPixels += star.numLitPixels;
                }
            }
        }

        // Animate the stars.
        Iterator<Star> starIterator = stars.iterator();
        while(starIterator.hasNext()){
            Star star = starIterator.next();
            star.run(scaledDeltaMs, fadeTime, baseBrightness);
            if(star.done()){
                removePoints(usedPoints, star.points);
                starIterator.remove();
                numLitPixels -= star.numLitPixels;
            }
        }
    }

    /**
     * Adds points to the set of used points only if they are all unused. Returns whether or not they were added.
     */
    private static boolean addPointsIfUnused(HashSet<Integer> usedPoints, int[] points){
        for(int point : points){
            if (point >= 0 && usedPoints.contains(point)){
                return false;
            }
        }
        for(int point : points){
            usedPoints.add(point);
        }
        return true;
    }

    /**
     * Removes points from the set of used points.
     */
    private static void removePoints(HashSet<Integer> usedPoints, int[] points){
        for(int point : points){
            usedPoints.remove(point);
        }
    }

    /**
     * Computes the current brightness from a star based on its parameters and age.
     */
    private static double getBrightness(double brightness, double baseBrightness, double lifespan, double fadeTime, double age) {
        if (age < fadeTime) {
            //return brightness * age / fadeTime;
            return Util.linearInterpolate(age, 0, baseBrightness, fadeTime, brightness);
        }
        if (age > lifespan) {
            return baseBrightness;
        }
        if (age > lifespan - fadeTime) {
            //return brightness * (lifespan - age) / fadeTime;
            return Util.linearInterpolate(lifespan - age, 0, baseBrightness, fadeTime, brightness);
        }
        return brightness;
    }

    /**
     * Returns whether a star should be created, based on parameters for the probabilistic rate of creation.
     */
    private static boolean shouldCreateStar(Random random, double rate, int numPixels, double deltaMs) {
        int randMax = (int)(numPixels / rate / (deltaMs / 1000));
        if (randMax == 0 || random.nextInt(randMax) != 0) {
            return false;
        }
        return true;
    }

    /**
     *  Creates a star based on constraints for randomized parameters.
     */
    private Star createRandomStar(int petalIndex, int pixelIndex, int minSize, int maxSize, double minBrightness, double maxBrightness, int brightnessSteps, double minLifespan, double maxLifespan) {
        int size = minSize + random.nextInt(maxSize-minSize+1);
        double brightness = Util.randomDouble(random, minBrightness, maxBrightness, brightnessSteps);
        double lifespan = Util.randomDouble(random, minLifespan, maxLifespan, 1000);
        return new Star(petalIndex, pixelIndex, size, brightness, lifespan);
    }

    private class Star {

        public final int petalIndex; // Index of petal.
        public final int pixelIndex; // Index of pixel within petal.
        public final int size;       // Size in pixels.
        public double brightness;    // Brightness from 0 to 100.
        public double lifespan;      // Lifespan in seconds
        public double age;           // Age in seconds;
        public int[] points;         // Pixels indicies that are occupied by this star.
        public int numLitPixels;     // Number of lit pixels, which may be less than size when it goes out of bounds.

        public Star(int petalIndex, int pixelIndex, int size, double brightness, double lifespan) {

            this.petalIndex = petalIndex;
            this.pixelIndex = pixelIndex;
            this.size = size;
            this.brightness = brightness;
            this.lifespan = lifespan;
            this.age = 0;

            // Compute set of used pixels.
            points = new int[size+2];
            for (int i = 0; i<size+2; i++) {
                int pi = pixelIndex + i - size/2;
                if (pi >= 0 && pi < numPixelsPerPetal) {
                    points[i] = petalIndexToPointIndex(pi, petalIndex);
                } else {
                    points[i] = -1;
                }
            }

            // Compute number of lit pixels, mainly for printing stats.
            this.numLitPixels = 0;
            for(int i=1; i<size+1; i++){
                int pointIndex = points[i];
                if(pointIndex >= 0) {
                    numLitPixels++;
                }
            }
        }

        public void run(double deltaMs, double fadeTime, double baseBrightness) {
            age += deltaMs / 1000;
            double displayBrightness = getBrightness(brightness, baseBrightness, lifespan, fadeTime, age);

            for(int i=1; i<size+1; i++){
                int pointIndex = points[i];
                if(pointIndex >= 0) {
                    colors[pointIndex] = LXColor.hsb(0, 0, displayBrightness);
                }
            }
        }

        // Returns whether to remove star from the animation.
        public boolean done() {
            return age > lifespan;
        }
    }
}
