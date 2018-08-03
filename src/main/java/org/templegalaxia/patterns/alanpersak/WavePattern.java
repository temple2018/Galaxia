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

    private Random random = new Random();
    private LinkedList<Wave> waves = new LinkedList<Wave>();
    private int numPoints;
    private double[] angles;
    private double[] radii;

    public WavePattern(LX lx) {
        super(lx);
        //waves.add(new Wave(1000, 1000, 1, 0, 100));

        // precompute pixel data
        numPoints = model.points.length;
        angles = new double[numPoints];
        radii = new double[numPoints];
        for (int i=0; i<numPoints; i++){
            LXPoint point = model.points[i];
            angles[i] = angle(point);
            radii[i] = Math.hypot(point.x, point.z);
        }
    }

    double waveTimer = 0;
    int frameCounter = 0;
    double dir = 0;

    public void run(double deltaMs) {
        deltaMs *= 5;
        waveTimer += deltaMs / 1000;
        frameCounter++;
        if(waveTimer > 10){

            //double size = (50 + random.nextInt(151)) / 100.0;
            //double direction = random.nextInt(360) / 360.0 * 2 * Math.PI;
            double size = 1;
            double direction = dir;
            dir += Math.PI/2;
            if(dir >= 2*Math.PI){
                dir = 0;
            }
            System.out.println("new wave " + size + " " + direction);
            waves.add(new Wave(1000, 1000, size, direction, 100));



            System.out.println("\nstats:");
            System.out.println("wave count: " + waves.size());
            System.out.println("frame rate: " + frameCounter / waveTimer);

            waveTimer = 0;
            frameCounter = 0;
        }

        // Turn everything off.
        for (LXPoint p : model.points) {
            colors[p.index] = LXColor.BLACK;
        }

        for(Iterator<Wave> it = waves.iterator(); it.hasNext(); ){
            Wave wave = it.next();
            wave.run(deltaMs);
            if(wave.done()){
                it.remove();
            }
        }
    }

    private static double MIN_POSITION = 1;

    // max radius: 28769.12613185006
    private static final double MAX_POSITION = 60000;

    private double angle(LXPoint point) {
        return Math.atan2(point.z, point.x) + Math.PI;
    }

    private class Wave {

        double direction; // angle of the waves center.
        double size;    // angle of the waves edges, going out from center.
        double speed; // rate that arc radius expands.
        double position; // arc radius.
        double intensity;

        public Wave(double position, double speed, double size, double direction, double intensity) {
            this.position = position;
            this.speed = speed;
            this.size = size;
            this.direction = direction;
            this.intensity = intensity;
        }

        public void run(double deltaMs) {
            position += speed * deltaMs / 1000;
            //System.out.println("pos: " + position);

            for (int i=0; i<numPoints; i++){
                //double angle = angle(point);
                //double radius = Math.hypot(point.x, point.z);
                //System.out.println("point " + point.x + " " + point.y + " " + angle + " " + radius);
                double angle = angles[i];
                if (angle < direction - size || angle > direction + size){
                    continue;
                }
                //double radius = Math.hypot(point.x, point.y);
                double radius = radii[i];
                if (radius > position) {
                    continue;
                }
                int brightness = (int)(intensity * Math.pow(2, (radius - position)/5000));
                LXPoint point = model.points[i];
                colors[model.getPixelIndex(point)] = LXColor.hsb(0, 0, brightness);
            }
        }

        public boolean done() {
            return position > MAX_POSITION;
        }
    }
}
