package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import java.util.ArrayList;
import java.util.List;

public class Ring extends LXAbstractFixture {

  public LXPoint getPoint(int idx) {
    return points.get(idx);
  }
}
