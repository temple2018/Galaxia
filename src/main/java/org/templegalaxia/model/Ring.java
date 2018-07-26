package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import java.util.List;

public class Ring extends LXModel {
  public Ring(List<LXPoint> points) {
    super(new Fixture(points));
  }

  private static class Fixture extends LXAbstractFixture {
    private Fixture(List<LXPoint> points) {
      for (LXPoint point : points) {
        addPoint(point);
      }
    }
  }
}
