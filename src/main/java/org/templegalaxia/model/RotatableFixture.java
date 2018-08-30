package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;

public class RotatableFixture extends LXModel {

  public static int numPixels;

  public RotatableFixture(LXTransform transform, PointLoader pointLoader) {
    super(new Fixture(transform, pointLoader));
  }

  private static class Fixture extends LXAbstractFixture {

    Fixture(LXTransform transform, PointLoader pointLoader) {
      numPixels = pointLoader.getNumPixels();
      for (float[] xyz : pointLoader.getPoints()) {
        transform.push();

        transform.translate(xyz[0], xyz[1], xyz[2]);
        addPoint(new LXPoint(transform));

        transform.pop();
      }
    }
  }
}
