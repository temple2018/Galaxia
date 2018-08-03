// Copyright 2018 Mark C. Slee
// Limited license granted for use in Galaxia 2018 Temple at Burning Man

package org.templegalaxia.patterns.mcslee;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.LXModulator;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.LXParameter;
import org.templegalaxia.model.Petal;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("Slee")
public class Synchronicity extends TemplePattern {

  public final CompoundParameter period = (CompoundParameter)
          new CompoundParameter("Period", 60000, 30000, 240000)
                  .setUnits(LXParameter.Units.MILLISECONDS)
                  .setDescription("Period of total oscillation cycle");


  public final CompoundParameter width = (CompoundParameter)
          new CompoundParameter("Width", 20, 10, 70)
                  .setDescription("Width of the strands");

  private final LXModulator basis = startModulator(new SinLFO(0, 1, period));

  public Synchronicity(LX lx) {
    super(lx);
    addParameter("period", this.period);
    addParameter("width", this.width);
  }

  public void run(double deltaMs) {
    double basis = this.basis.getValuef();

    // Apply shaping to basis
    basis = 2 * (basis - .5);
    basis = basis * Math.sqrt(Math.abs(basis));
    basis = (basis + 1) * .5;

    double falloff = 100 / this.width.getValue();
    int pi = 0;
    for (Petal petal : model.petals) {
      double multiplier = 1 + (pi % 5);
      double pb = (basis * multiplier) % 1.f;
      double pos = (.5f + .5f * Math.sin(pb * LX.TWO_PI)) * petal.size;
      int i = 0;
      for (LXPoint p : petal.points) {
        double b = Math.max(0, 100 - falloff*Math.abs(i - pos));
        colors[p.index] = LXColor.gray(b);
        ++i;
      }
      ++pi;
    }
  }
}
