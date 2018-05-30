package org.templegalaxia.patterns.testing;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("Test")
public class MoveYPosition extends TemplePattern {
    private final BoundedParameter yPos = new BoundedParameter("YPos", 0, model.yMin, model.yMax);

    public MoveYPosition(LX lx) {
        super(lx);
        addParameter(yPos);
    }

    public void run(double deltaMs) {
        for (LXPoint p : model.points) {
            float brightnessValue = Math.max(0, 100 - Math.abs(p.y - yPos.getValuef()));
            colors[p.index] = LXColor.hsb(0, 0, brightnessValue);
        }
    }
}