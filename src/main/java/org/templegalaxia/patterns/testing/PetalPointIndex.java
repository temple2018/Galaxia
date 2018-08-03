package org.templegalaxia.patterns.testing;

import heronarts.lx.LX;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.model.Petal;
import org.templegalaxia.patterns.TemplePattern;

import java.util.stream.IntStream;

public class PetalPointIndex extends TemplePattern {
  private BoundedParameter pointParam = new BoundedParameter("Point index", 0, Petal.numPixels);

  public PetalPointIndex(LX lx) {
    super(lx);

    addParameter(pointParam);
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      colors[p.index] = LXColor.BLACK;
    }

    IntStream.range(0, model.petals.size()-1).forEach(
      petalNum -> {
        colors[petalIndexToPointIndex((int)pointParam.getValuef(), petalNum)] = LXColor.WHITE;
      }
    );
  }
}
