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

    private final BooleanParameter lightUpper = new BooleanParameter("Light Upper");
    private final BooleanParameter lightLower = new BooleanParameter("Light Lower");

    private final BooleanParameter enable = new BooleanParameter("Enable");


    public UniverseTester(LX lx) {
        super(lx);
        addParameter(universeToLight);
        addParameter(lightUpper);
        addParameter(lightLower);

        addParameter(enable);
    }

    public void run(double deltaMs) {
        setAllPoints(LXColor.BLACK);
        int colorToSet = enable.getValueb() ? LXColor.WHITE : LXColor.BLACK;

        int universeNumber = (int) universeToLight.getValue();
        if (lightUpper.getValueb()) {
            setFixtureToColor(model.upper.get(universeNumber), colorToSet);
        }

        if (lightLower.getValueb()) {
            setFixtureToColor(model.lower.get(universeNumber), colorToSet);
        }
        System.out.println(String.format("Universe Number: %d", universeNumber + 1));
    }
}
