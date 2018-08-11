package org.templegalaxia.patterns;

import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import org.templegalaxia.model.Petal;
import org.templegalaxia.model.Temple;

public abstract class TemplePattern extends LXPattern {
  protected final Temple model;

  public TemplePattern(LX lx) {
    super(lx);
    model = (Temple) lx.model;
  }

  public int pointIndexToPetalIndex(int pointIndex) {
    return pointIndex % Petal.numPixels;
  }

  public int pointIndexToPetalNumber(int pointIndex) {
    return pointIndex / Petal.numPixels;
  }

  public static int petalIndexToPointIndex(int petalIndex, int petalNumber) {
    return (petalNumber * Petal.numPixels) + petalIndex;
  }
}
