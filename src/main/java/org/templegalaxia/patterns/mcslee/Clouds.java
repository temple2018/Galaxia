// Copyright 2018 Mark C. Slee
// Limited license granted for use in Galaxia 2018 Temple at Burning Man

package org.templegalaxia.patterns.mcslee;

import heronarts.lx.LX;
import heronarts.lx.LXUtils;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.HistoryBuffer;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.CompoundParameter;
import org.templegalaxia.model.Ring;
import org.templegalaxia.patterns.TemplePattern;

public class Clouds extends TemplePattern {

  public final CompoundParameter speed = new CompoundParameter("Range", 5, 1, 10)
          .setDescription("Range of the cloud motion");

  public final CompoundParameter value = new CompoundParameter("Value", .5)
          .setDescription("Value to be modulated to generate randomness in the rising cloud");

  public final CompoundParameter contrast = new CompoundParameter("Constrast", 1, 1, 4)
          .setDescription("Amount to increase contrast by");

  private final HistoryBuffer history = new HistoryBuffer("History", 4096) {
    @Override
    protected double getFrameValue() {
      return value.getValue();
    }
  };

  public Clouds(LX lx) {
    super(lx);
    startModulator(this.history);
    addParameter("value", this.value);
    addParameter("speed", this.speed);
    addParameter("contrast", this.contrast);
  }

  public void run(double deltaMs) {
    int speed = (int) this.speed.getValue();
    double contrast = this.contrast.getValue();
    int ri = 0;
    for (Ring ring : model.rings) {
      int pi = 0;
      for (LXPoint p : ring.points) {
        double b = 100 * this.history.getHistory(ri * speed, (ri + 1) * speed);
        b = LXUtils.constrain((b - 50) * contrast, 0, 100);
        colors[p.index] = LXColor.gray(b);
        ++pi;
      }
      ++ri;
    }
  }
}
