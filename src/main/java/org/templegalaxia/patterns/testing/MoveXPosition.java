package org.templegalaxia.patterns.testing;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("Test")
public class MoveXPosition extends TemplePattern {
  private final BoundedParameter xPos = new BoundedParameter("XPos", 0, model.xMin, model.xMax);

  public MoveXPosition(LX lx) {
    super(lx);
    addParameter(xPos);
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      float brightnessValue = Math.max(0, 100 - Math.abs(p.x - xPos.getValuef()));
      colors[p.index] = LXColor.hsb(0, 0, brightnessValue);
    }
  }
}
