package org.templegalaxia.patterns;

import heronarts.lx.LX;
import heronarts.lx.LXPattern;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXPoint;
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

  public void setAllPoints(int lxcolor){
    for (LXPoint p : model.points) {
      colors[p.index] = lxcolor;
    }
  }

  public void setFixtureToColor(LXFixture fixture, int color){
    for(LXPoint pt: fixture.getPoints()){
      colors[pt.index] = color;
    }
  }
}
