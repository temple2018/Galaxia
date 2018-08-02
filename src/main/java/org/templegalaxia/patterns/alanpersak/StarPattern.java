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
public class StarPattern extends TemplePattern {

    private static final double STARS_PER_SEC = 6;
    private static final int NUM_PIXELS = 1200;
    private static final double FADE_TIME = 30;
    private static final int MIN_LIFESPAN = 60;
    private static final int MAX_LIFESPAN = 300;
    private static final double TIME_SCALE = 10;

    private Random random = new Random();
    private LinkedList<Star> stars = new LinkedList<Star>();
    private HashSet<Integer> points = new HashSet<Integer>();
    private double time = 0;
    private int numStars = 0;
    private int numLitPixels = 0;

    private static double getBrightness(double brightness, double time, double lifespan) {
        if (time < FADE_TIME) {
            return brightness * time / FADE_TIME;
        }
        if (time > lifespan) {
            return 0;
        }
        if (time > lifespan - FADE_TIME) {
            return brightness * (lifespan - time) / FADE_TIME;
        }
        return brightness;
    }

    private Star createRandomStar(double deltaMs, int petalIndex, int pixelIndex) {
        int randMax = (int)(NUM_PIXELS / STARS_PER_SEC / (deltaMs / 1000 * TIME_SCALE));
        if (randMax == 0 || random.nextInt(randMax) != 0) {
            return null;
        }

        int size = 1 + random.nextInt(3);
        int brightness = 40 + 30*random.nextInt(3);
        double lifespan = MIN_LIFESPAN + random.nextInt(MAX_LIFESPAN-MIN_LIFESPAN);

        return new Star(petalIndex, pixelIndex, size, brightness, lifespan);
    }


    public StarPattern(LX lx) {
        super(lx);
    }

    public void run(double deltaMs) {
        // Print stats every 10 seconds.
        time += deltaMs / 1000;
        if (time > 20) {
            System.out.println("\nstats:");
            System.out.println("star rate: " + numStars / time / TIME_SCALE);
            System.out.println("star count: " + stars.size());
            System.out.println("used pixel count: " + points.size());
            System.out.println("lit pixel count: " + numLitPixels);
            numStars = 0;
            time = 0;
        }

        // Turn everything off.
        for (LXPoint p : model.points) {
            colors[p.index] = LXColor.BLACK;
        }

        // Create new stars.
        for (int petalIndex = 0; petalIndex < model.petals.size(); petalIndex++){
            for(int pixelIndex = 0; pixelIndex < model.petals.get(petalIndex).numPixels; pixelIndex++){
                Star star = createRandomStar(deltaMs, petalIndex, pixelIndex);
                if (star != null && addFreePoints(points, star.points)){
                    stars.add(star);
                    numStars++;
                    numLitPixels += star.numLitPixels;
                }
            }
        }

        // Animate existing stars.
        Iterator<Star> starIterator = stars.iterator();
        while(starIterator.hasNext()){
            Star star = starIterator.next();
            star.run(deltaMs);
            if(star.done()){
                removePoints(points, star.points);
                starIterator.remove();
                numLitPixels -= star.numLitPixels;
            }
        }
    }

    private static boolean addFreePoints(HashSet<Integer> usedPoints, int[] points){
        for(int newPoint : points){
            if (usedPoints.contains(newPoint)){
                return false;
            }
        }
        for(int newPoint : points){
            usedPoints.add(newPoint);
        }
        return true;
    }

    private static void removePoints(HashSet<Integer> usedPoints, int[] points){
        for(int point : points){
            usedPoints.remove(point);
        }
    }

    private class Star {
        public final int petalIndex;
        public final int pixelIndex;
        public final int size;
        public int brightness;
        public double lifespan;
        public double time;
        public int[] points;
        public int numLitPixels;

        public Star(int petalIndex, int pixelIndex, int size, int brightness, double lifespan) {
            this.petalIndex = petalIndex;
            this.pixelIndex = pixelIndex;
            this.size = size;
            this.brightness = brightness;
            this.lifespan = lifespan;
            this.time = 0;

            points = new int[size+2];
            for (int i = 0; i<size+2; i++) {
                int pi = pixelIndex + i - size/2;
                if (pi>= 0 && pi <60) {
                    points[i] = petalIndexToPointIndex(pi, petalIndex);
                } else {
                    points[i] = -1;
                }
            }

            this.numLitPixels = 0;
            for(int i=1; i<size+1; i++){
                int pointIndex = points[i];
                if(pointIndex >= 0) {
                    numLitPixels++;
                }
            }
            //System.out.println("new star " + petalIndex + " " + pixelIndex + " " + size + " " + brightness + " " + lifespan);
        }

        public void run(double deltaMs) {
            time += deltaMs / 1000 * TIME_SCALE;
            double displayBrightness = getBrightness(brightness, time, lifespan);

            for(int i=1; i<size+1; i++){
                int pointIndex = points[i];
                if(pointIndex >= 0) {
                    colors[pointIndex] = LXColor.hsb(0, 0, displayBrightness);
                }
            }
        }

        public boolean done() {
            return time > lifespan;
        }
    }
}
