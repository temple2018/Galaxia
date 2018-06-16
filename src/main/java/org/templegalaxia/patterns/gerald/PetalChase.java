package org.templegalaxia.patterns.gerald;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.BoundedParameter;
import java.util.stream.IntStream;
import org.templegalaxia.model.Petal;
import org.templegalaxia.patterns.TemplePattern;
import processing.core.PApplet;

@LXCategory("Gerald")
public class PetalChase extends TemplePattern {
  private double lfoPosition = 0;

  private BoundedParameter blurParam =
      new BoundedParameter("blurParam", Petal.NUM_PIXELS / 6, 1, Petal.NUM_PIXELS / 2)
          .setDescription("Distance of trailing lights");

  private BoundedParameter speedParam =
      new BoundedParameter("Speed", 2000, 5000, 1000).setDescription("Speed of light wave");

  private BoundedParameter jitterParam =
      new BoundedParameter("Jitter", 1, 1, 3).setDescription("Jitter the lights on each petal");

  private SinLFO pixelLFO = new SinLFO(0, Petal.NUM_PIXELS, speedParam);

  public PetalChase(LX lx) {
    super(lx);
    addModulator(pixelLFO).trigger();
    addParameter(blurParam);
    addParameter(jitterParam);
    addParameter(speedParam);
  }

  public void run(double deltaMs) {
    float blurVal = blurParam.getValuef();
    double currentPixel = pixelLFO.getValuef();

    // Remap the currentPixel into a petal pixel space that is extended on both ends by the blurVal
    // this gives the illusion of the light chase falling off the edge of the petals.
    int remapPixel =
        (int)
            PApplet.map(
                (float) currentPixel, 0, Petal.NUM_PIXELS, -blurVal, Petal.NUM_PIXELS + blurVal);

    // Determine the oscilators direction, store currentPixel for the next run
    double lfoDirection = Math.signum(currentPixel - lfoPosition);
    lfoPosition = currentPixel;

    // Turn everything off
    for (LXPoint p : model.points) {
      colors[p.index] = LXColor.BLACK;
    }

    // Treat every petal's pixels the same
    IntStream.range(0, model.petals.size())
        .forEach(
            petalNum -> {

              // Add some random jitter to make things interesting
              int jitter = (int) jitterParam.getValuef();

              // Modify the length of the blurParam trail
              for (int blurStep = 1; blurStep <= blurVal; ++blurStep) {

                int blurPixel;
                if (lfoDirection >= 0) {
                  blurPixel = remapPixel - blurStep - jitter;
                } else {
                  blurPixel = remapPixel + blurStep + jitter;
                }

                // Ensure that the blurPixel is a pixel in reality
                if (0 <= blurPixel && blurPixel < Petal.NUM_PIXELS) {
                  int pointIndex = petalIndexToPointIndex(blurPixel, petalNum);

                  // Modify brightness based on the distance form the zeroth point
                  float bv = 100 / ((float) blurStep / 3);
                  colors[pointIndex] = LXColor.hsb(0, 0, bv);
                }
              }
            });
  }
}
