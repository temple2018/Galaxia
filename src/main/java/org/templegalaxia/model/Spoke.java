package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

public class Spoke extends LXModel {
  public static int numPixels;

  public Spoke(Petal petal, RotatableFixture groundArc) {
    super(new Fixture(petal, groundArc));
  }

  private static class Fixture extends LXAbstractFixture {
    Fixture(Petal petal, RotatableFixture groundArc) {
      numPixels = petal.getPoints().size() + groundArc.getPoints().size();

      for (int groundItr = groundArc.getPoints().size() - 1; groundItr >= 0; groundItr--) {
        addPoint(groundArc.getPoints().get(groundItr));
      }

      for (LXPoint p : petal.getPoints()) {
        addPoint(p);
      }
    }
  }
}
