package org.templegalaxia.patterns.pandores;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("PanDoreS")
public class Drahciug extends TemplePattern {
    private final BoundedParameter Iterator =
            new BoundedParameter("Iterator", 0, 0, 400);
    public Drahciug(LX lx) {
        super(lx);
        addParameter(Iterator);
    }
    private int background = 0;

    public void run(double deltaMs) {

        for (LXPoint p : model.points) {
            colors[p.index] = LXColor.hsb(0, 0, (background++) + Iterator.getValue());
            if(background == 100){
                background = 0;
            }
        }
    }
}
