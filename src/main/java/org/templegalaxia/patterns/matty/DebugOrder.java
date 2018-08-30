package org.templegalaxia.patterns.matty;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("MattyG")
public class DebugOrder extends TemplePattern {
  private BoundedParameter selectedMod = new BoundedParameter("rgb", 0, 0, 2);

  public DebugOrder(LX lx) {
    super(lx);
    addParameter(selectedMod);
  }

  public void run(double deltaMs) {
    for (int i = 0; i < colors.length; ++i) {
      if ((i % 3) == Math.round(selectedMod.getValue())) colors[i] = LXColor.WHITE;
      else colors[i] = LXColor.BLACK;
    }
  }
}
