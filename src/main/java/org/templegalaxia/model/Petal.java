package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;

import java.util.Collections;

public class Petal extends LXModel {

  public static int numPixels;

  public Petal(LXTransform transform) {
    super(new Fixture(transform));
  }

  private static class Fixture extends LXAbstractFixture {
    Fixture(LXTransform transform) {
      LowerPetal lowerPetal = new LowerPetal(transform);
      UpperPetal upperPetal = new UpperPetal(transform);

      numPixels = lowerPetal.getPoints().size() + upperPetal.getPoints().size();
      for (int lowerItr = LowerPetal.numPixels - 1; lowerItr >= 0; lowerItr--){
        addPoint(lowerPetal.getPoints().get(lowerItr));
      }

      for (LXPoint p : upperPetal.getPoints()){
        addPoint(p);
      }
    }
  }
}
