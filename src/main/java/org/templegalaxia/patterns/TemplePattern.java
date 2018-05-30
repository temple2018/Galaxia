package org.templegalaxia.patterns;

import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import org.templegalaxia.model.Temple;


public abstract class TemplePattern extends LXPattern {
    protected final Temple model;

    public TemplePattern(LX lx) {
        super(lx);
        model = (Temple)lx.model;
    }
}
