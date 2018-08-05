package org.templegalaxia.patterns.testing;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.CompoundParameter;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("Test")
public class RingIterator extends TemplePattern {
  private final CompoundParameter ringNum =
      new CompoundParameter("Petal", 0, 0, model.rings.size() - 1);

  public RingIterator(LX lx) {
    super(lx);
    addParameter(ringNum);
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      colors[p.index] = LXColor.BLACK;
    }
    
    for (LXPoint p : model.rings.get((int) ringNum.getValuef()).points) {
      colors[p.index] = LXColor.WHITE;
    }
  }
}
