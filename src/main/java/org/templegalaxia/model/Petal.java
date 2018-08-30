package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;

public class Petal extends LXModel {

  public static int numPixels;

  public Petal(RotatableFixture lowerPetal, RotatableFixture upperPetal) {
    super(new Fixture(lowerPetal, upperPetal));
  }

  private static class Fixture extends LXAbstractFixture {
    Fixture(RotatableFixture lowerPetal, RotatableFixture upperPetal) {
      numPixels = lowerPetal.getPoints().size() + upperPetal.getPoints().size();

      // NOTE(G3): We must build the model how we physically wire the nodes, but this means the
      // ordering
      // of the petal is "weird". We wire the lower petal [11:0], but we want to represent the petal
      // as one contiguous
      // fixture from module [0:19]. As such, we must reverse the order of the lower petal to add
      // the points starting
      // from Module 0
      for (int lowerItr = lowerPetal.getPoints().size() - 1; lowerItr >= 0; lowerItr--) {
        addPoint(lowerPetal.getPoints().get(lowerItr));
      }

      upperPetal.getPoints().forEach(this::addPoint);
    }
  }
}
