package org.templegalaxia.patterns.pandores;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.patterns.TemplePattern;
import processing.core.PApplet;

@LXCategory("PanDoreS")
public class Drahciug extends TemplePattern {
    private final BoundedParameter Iterator =
            new BoundedParameter("Iterator", 0, 0, 1000);
    private final BoundedParameter Step =
            new BoundedParameter("Step", 0, 1, 6);
    public Drahciug(LX lx) {
        super(lx);
        addParameter(Iterator);
        addParameter(Step);
    }
    private int background = 0;

    public void run(double deltaMs) {
        for (LXPoint p : model.points) {
            switch ((int) Step.getValue()){
                case 1:
                    background += 1;
                    break;
                case 2:
                    background += 4;
                    break;
                case 3:
                    background += 9;
                    break;
                case 4:
                    background += 12;
                    break;
                case 5:
                    background += 17;
                    break;
                case 6:
                    background += 25;
                    break;
                default:
                    background++;
                    break;
            }
            colors[p.index] = LXColor.hsb(0, 0, ((background++) + Iterator.getValue()));
            if(background >= 100){
                background = 0;
            }
        }
    }
}
