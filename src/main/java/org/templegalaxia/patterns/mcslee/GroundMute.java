package org.templegalaxia.patterns.mcslee;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.LXModelEffect;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import org.templegalaxia.model.Temple;

@LXCategory("Slee")
public class GroundMute extends LXModelEffect<Temple> {
  public GroundMute(LX lx) {
    super(lx);
  }

  public void run(double deltaMs, double amt) {
    if (amt > 0) {
      for (LXPoint p : model.groundPoints) {
        setColor(p.index, LXColor.BLACK);
      }
    }
  }
}
