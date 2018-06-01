package org.templegalaxia.patterns.matty;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.SawLFO;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.patterns.TemplePattern;
import processing.core.PApplet;

@LXCategory("MattyG")
public class Teleport extends TemplePattern {
    BoundedParameter speed = new BoundedParameter(
            "speed",
            5000, 10000, 250);

    BoundedParameter bandWidth = new BoundedParameter(
            "bandwidth",
            (model.yMax - model.yMin) / 10,
            (model.yMax - model.yMin) / 100,
            (model.yMax - model.yMin) / 2);

    BoundedParameter acceleration = new BoundedParameter(
            "Accel",
            1, 0.5, 5);

    SawLFO saw = new SawLFO(model.yMin, model.yMax, speed);

    public Teleport(LX lx) {
        super(lx);
        addModulator(saw).trigger();
        addParameter(acceleration);
        addParameter(bandWidth);
        addParameter(speed);
    }

    public void run(double deltaMs) {
        float currentY = saw.getValuef();
        float halfBandwidth = (bandWidth.getValuef() / 2);
        currentY = PApplet.map(
                currentY,
                model.yMin,
                model.yMax,
                model.yMin - halfBandwidth,
                model.yMax + halfBandwidth
        );

        for(LXPoint p : model.points) {
            boolean aboveMin = (p.y - halfBandwidth) < currentY;
            boolean belowMax = (p.y + halfBandwidth) > currentY;

            if(aboveMin && belowMax){
                colors[p.index] = LXColor.WHITE;
            } else {
                colors[p.index] = LXColor.BLACK;
            }

        }
    }
}
