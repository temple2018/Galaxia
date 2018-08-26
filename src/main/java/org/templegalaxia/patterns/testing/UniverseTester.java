package org.templegalaxia.patterns.testing;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("Test")
public class UniverseTester extends TemplePattern {
    private final BoundedParameter universeToLight =
            new BoundedParameter("Universe", 0, 0, model.upper.size() - 1);
    private final BooleanParameter isUpper = new BooleanParameter("Upper");

    public UniverseTester(LX lx) {
        super(lx);
        addParameter(universeToLight);
        addParameter(isUpper);
    }

    public void run(double deltaMs) {
        LXFixture fixtureToLight;

        if (isUpper.getValueb()){
            fixtureToLight = model.upper.get((int) universeToLight.getValue());
        } else {
            fixtureToLight = model.lower.get((int) universeToLight.getValue());
        }

        setAllPoints(LXColor.BLACK);
        for(LXPoint pt: fixtureToLight.getPoints()){
            colors[pt.index] = LXColor.WHITE;
        }
    }
}
