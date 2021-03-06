package org.templegalaxia.patterns.matty;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import java.util.PrimitiveIterator;
import java.util.Random;

import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("MattyG")
public class Sparkle extends TemplePattern {
  private final Random myRandom;

  private final BoundedParameter weighting =
          new BoundedParameter("Weighting", 0, 0.5, 1);

  public Sparkle(LX lx) {
    super(lx);
    myRandom = new Random(3);
    addParameter(weighting);
  }

  public void run(double deltaMs) {
    PrimitiveIterator.OfDouble ds = myRandom.doubles(0.0, 100.0).iterator();
    for (LXPoint p : model.points) {
      int currentColor = colors[p.index];

      double w = weighting.getValue();
      double prev = LXColor.b(currentColor) * w;
      double next = ds.next() * (1.0 - w);

      colors[p.index] = LXColor.gray(next + prev);
    }
  }
}
