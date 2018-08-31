package org.templegalaxia.patterns.matty;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("MattyG")
public class AnnasCrown extends TemplePattern {
    private static final double YMIN = 0.6;
    public AnnasCrown(LX lx) {
        super(lx);
    }

    public void run(double deltaMs) {
        setAllPoints(LXColor.BLACK);

        for (LXPoint p : model.points) {
            double xCP = (model.xMin + model.xMax) / 2;
            double yCP = (model.yMin + model.yMax) / 2;
            double zCP = (model.zMin + model.zMax) / 2;

            double xRange = (model.xMax - model.xMin);
            double yRange = (model.yMax - model.yMin);
            double zRange = (model.zMax - model.zMin);

            double ndX = (p.x - model.xMin )/ xRange;
            double ndY = (p.y - model.yMin) / yRange;
            double ndZ = (p.z - model.zMin) / zRange;

            double theta = Math.atan2((ndX - 0.5) * 2, (ndZ - 0.5) * 2);

            if(ndY > YMIN){
                if (ndY < 0.75){
                    colors[p.index] = LXColor.WHITE;
                }

                // Crown spires
                if (theta > -0.3 * Math.PI && theta < -0.2 * Math.PI) {
                    colors[p.index] = LXColor.WHITE;
                }

                if (theta > -0.1 * Math.PI && theta < 0.0 * Math.PI) {
                    colors[p.index] = LXColor.WHITE;
                }
                if (theta > 0.1 * Math.PI && theta < 0.2 * Math.PI) {
                    colors[p.index] = LXColor.WHITE;

                if (theta > 0.3 * Math.PI && theta < 0.4 * Math.PI) {
                    colors[p.index] = LXColor.WHITE;
                }

                if (theta > 0.5 * Math.PI && theta < 0.6 * Math.PI) {
                    colors[p.index] = LXColor.WHITE;
                }

                if (theta > 0.7 * Math.PI && theta < 0.8 * Math.PI) {
                    colors[p.index] = LXColor.WHITE;
                }

                if (theta > 0.9 * Math.PI && theta < 1.0 * Math.PI) {
                    colors[p.index] = LXColor.WHITE;
                }

                }


            }
        }
    }
}

