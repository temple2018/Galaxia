package org.templegalaxia.patterns.testing;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("Test")
public class PetalIterator extends TemplePattern {
    private final BoundedParameter petalNum = new BoundedParameter("Petal", 0, 0, model.petals.size()-1);

    public PetalIterator(LX lx) {
        super(lx);
        addParameter(petalNum);
    }

    public void run(double deltaMs) {
        for(LXPoint p: model.points) {
            colors[p.index] = LXColor.BLACK;
        }
        for (LXPoint p : model.petals.get((int)petalNum.getValuef()).points) {
            colors[p.index] = LXColor.WHITE;
        }
    }
}
