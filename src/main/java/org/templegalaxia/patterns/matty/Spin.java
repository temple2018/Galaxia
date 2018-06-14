package org.templegalaxia.patterns.matty;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import org.templegalaxia.patterns.TemplePattern;

import java.util.PrimitiveIterator;
import java.util.Random;

@LXCategory("MattyG")
public class Spin extends TemplePattern {
    public Spin(LX lx) {
        super(lx);
    }

    public void run(double deltaMs) {
        for(LXPoint p : model.points) {
            colors[p.index] = LXColor.gray((LXColor.b(colors[p.index]) + ds.next())/2);
        }
    }
}
