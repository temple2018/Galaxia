package org.templegalaxia.patterns.matty;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import org.templegalaxia.patterns.TemplePattern;

import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.stream.DoubleStream;

@LXCategory("MattyG")
public class Sparkle extends TemplePattern {
    private final Random myRandom;
    public Sparkle(LX lx) {
        super(lx);
        myRandom = new Random(3);
    }

    public void run(double deltaMs) {
        PrimitiveIterator.OfDouble ds = myRandom.doubles(0.0, 100.0).iterator();
        for(LXPoint p : model.points) {
            colors[p.index] = LXColor.gray((LXColor.b(colors[p.index]) + ds.next())/2);
        }
    }
}
