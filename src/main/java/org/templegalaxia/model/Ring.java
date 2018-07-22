package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXPoint;
import java.util.HashMap;
import java.util.Map;

public class Ring extends LXAbstractFixture {
  private Map<LXPoint, Integer> pixelIdxMap = new HashMap<LXPoint, Integer>();

  public LXAbstractFixture addIndexedPoint(LXPoint lxPoint, int pixelIndex) {
    pixelIdxMap.put(lxPoint, pixelIndex);
    return super.addPoint(lxPoint);
  }

  public int getPixelIndex() {
    return getPixelIndex();
  }

  /**
   * Return the pixel index associated with the given point.
   *
   * @param lxPoint
   * @return Index in the point in absolute model space (i.e. the index in the Model.colors array)
   */
  public int getPixelIndex(LXPoint lxPoint) {
    return pixelIdxMap.get(lxPoint);
  }
}
