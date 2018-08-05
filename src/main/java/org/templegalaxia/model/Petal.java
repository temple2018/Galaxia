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

  public Petal(LowerPetal lowerPetal, UpperPetal upperPetal){
    super(new Fixture(lowerPetal, upperPetal));
  }

  private static class LoadPetalChunks {
    LoadPetalChunks(Fixture fixture, LowerPetal lowerPetal, UpperPetal upperPetal){
      numPixels = lowerPetal.getPoints().size() + upperPetal.getPoints().size();
      for (int lowerItr = LowerPetal.numPixels - 1; lowerItr >= 0; lowerItr--){
        fixture.addPoint(lowerPetal.getPoints().get(lowerItr));
      }

      for (LXPoint p : upperPetal.getPoints()){
        fixture.addPoint(p);
      }
    }
  }

  private static class Fixture extends LXAbstractFixture {
    Fixture(LowerPetal lowerPetal, UpperPetal upperPetal){
      new LoadPetalChunks(this, lowerPetal, upperPetal);
    }
    Fixture(LXTransform transform) {
      LowerPetal lowerPetal = new LowerPetal(transform);
      UpperPetal upperPetal = new UpperPetal(transform);

      new LoadPetalChunks(this, lowerPetal, upperPetal);
    }
  }
}
