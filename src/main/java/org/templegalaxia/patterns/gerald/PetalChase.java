package org.templegalaxia.patterns.gerald;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.model.Petal;
import org.templegalaxia.patterns.TemplePattern;
import processing.core.PApplet;

import java.util.stream.IntStream;

@LXCategory("Gerald")
public class PetalChase extends TemplePattern {
    private BoundedParameter blur = new BoundedParameter(
            "blur",
            Petal.NUM_PIXELS/6,
            1,
            Petal.NUM_PIXELS/2
    ).setDescription("Distance of trailing lights");

    private BoundedParameter speed = new BoundedParameter(
            "Speed",
            2000,
            5000,
            1000
    ).setDescription("Speed of light wave");

    private SinLFO pixelLFO = new SinLFO(0,Petal.NUM_PIXELS, speed);

    public PetalChase(LX lx) {
        super(lx);
        addModulator(pixelLFO).trigger();
        addParameter(blur);
        addParameter(speed);
    }

    public void run(double deltaMs) {
        float blurVal = blur.getValuef();
        float currentPixel = pixelLFO.getValuef();

        // Remap the currentPixel into a petal pixel space that is extended on both ends by the blurVal
        // this gives the illusion of the light chase falling off the edge of the petals.
        int remapPixel = (int)PApplet.map(
                currentPixel,
                0,
                Petal.NUM_PIXELS,
                -blurVal,
                Petal.NUM_PIXELS + blurVal
        );

        // Turn everything off
        for(LXPoint p : model.points) {
            colors[p.index] = LXColor.BLACK;
        }

        // Treat every petal's pixels the same
        IntStream.range(0, model.petals.size()).forEach(
            petalNum -> {

                // Modify the length of the blur trail
                for(int blurSteps = 1; blurSteps <= blurVal; ++blurSteps) {

                    // Determine if the pixelLFO's value is increasing or decreasing
                    int blurPixel;
                    if(pixelLFO.getValuef() > currentPixel) {
                        blurPixel = remapPixel - blurSteps;
                    } else {
                        blurPixel = remapPixel + blurSteps;
                    }

                    // Ensure that the blurPixel is a pixel in reality
                    if(0<= blurPixel && blurPixel < Petal.NUM_PIXELS) {
                        int pointIndex = petalIndexToPointIndex(blurPixel, petalNum);

                        // Modify brightness based on the distance form the zeroth point
                        float bv = 100/((float)blurSteps/3);
                        colors[pointIndex] = LXColor.hsb(0, 0, bv);
                    }
                }
            }
        );
    }
}
