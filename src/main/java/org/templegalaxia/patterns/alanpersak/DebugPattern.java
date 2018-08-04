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

/**
 * This pattern is just for printing out some useful info. It doesn't do any animation.
 */
@LXCategory("Alan Persak")
public class DebugPattern extends TemplePattern {

    private double minx = 100000;
    private double maxx = 0;
    private double miny = 100000;
    private double maxy = 0;
    private double minz = 100000;
    private double maxz = 0;
    private double minr = 100000;
    private double maxr = 0;

    public DebugPattern(LX lx) {
        super(lx);
        printModelStats();
    }

    public void printModelStats(){
        for(LXPoint point : model.points){
            minx = Math.min(minx, point.x);
            maxx = Math.max(maxx, point.x);
            miny = Math.min(miny, point.y);
            maxy = Math.max(maxy, point.y);
            minz = Math.min(minz, point.z);
            maxz = Math.max(maxz, point.z);
            double r = Math.hypot(point.z, point.x);
            minr = Math.min(minr, r);
            maxr = Math.max(maxr, r);

            System.out.println("\nmodel stats:");
            System.out.println("minx: " + minx + "\nmaxx: " + maxx);
            System.out.println("miny: " + miny + "\nmaxy:" + maxy);
            System.out.println("minz: " + minz + "\nmaxz: " + maxz);
            System.out.println("minr: " + minr + "\nmaxr: " + maxr);
        }
    }

    public void run(double deltaMs) {
    }
}
