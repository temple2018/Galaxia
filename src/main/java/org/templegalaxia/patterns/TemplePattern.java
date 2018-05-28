package org.templegalaxia.patterns;

import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import org.templegalaxia.model.Temple;


abstract class TemplePattern extends LXPattern {
    protected final Temple model;

    TemplePattern(LX lx) {
        super(lx);
        model = (Temple)lx.model;
    }
}
