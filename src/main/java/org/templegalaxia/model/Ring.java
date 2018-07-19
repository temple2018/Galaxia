package org.templegalaxia.model;

import heronarts.lx.model.LXPoint;
import java.util.ArrayList;
import java.util.List;

public class Ring {
  private List<LXPoint> points;

  Ring(int size) {
    points = new ArrayList<LXPoint>(size);
  }

  public void addPoint(LXPoint point) {
    points.add(point);
  }

  public List<LXPoint> getPoints() {
    return points;
  }

  public LXPoint getPoint(int idx) {
    return points.get(idx);
  }
}
