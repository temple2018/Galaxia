// Copyright 2018 Mark C. Slee
// Limited license granted for use in Galaxia 2018 Temple at Burning Man

package org.templegalaxia.patterns.mcslee;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.LXLayer;
import heronarts.lx.LXUtils;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.LXModulator;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.LXParameter;
import org.templegalaxia.model.Petal;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("Slee")
public class Crawlers extends TemplePattern {

  public final CompoundParameter period = (CompoundParameter) new CompoundParameter("Period", 9000, 2000, 27000)
          .setUnits(LXParameter.Units.MILLISECONDS)
          .setDescription("Period of oscillation of position");

  public final CompoundParameter contrast = new CompoundParameter("Contrast", .5)
          .setDescription("Scaling of brightness contrast");

  public final CompoundParameter minBrightness = new CompoundParameter("MinBrt", 20, 0, 100)
          .setDescription("Minimum brightness of the moving objects");

  public final CompoundParameter minSize = new CompoundParameter("MinSz", 4, 1, 10)
          .setDescription("Minimum Size of the crawling objects");

  public final CompoundParameter maxSize = new CompoundParameter("MaxSz", 30, 20, 75)
          .setDescription("Maximum Size of the crawling objects");

  public final CompoundParameter sizeRate = (CompoundParameter) new CompoundParameter("SzRate", 13000, 2000, 27000)
          .setUnits(LXParameter.Units.MILLISECONDS)
          .setDescription("Period of change in the size");

  public final CompoundParameter invert = new CompoundParameter("Invert", 0)
          .setDescription("Amount of inversion applied");


  public Crawlers(LX lx) {
    super(lx);
    addParameter("minBrightness", this.minBrightness);
    addParameter("contrast", this.contrast);
    addParameter("period", this.period);
    addParameter("minSize", this.minSize);
    addParameter("maxSize", this.maxSize);
    addParameter("sizeRate", this.sizeRate);
    addParameter("invert", this.invert);
    for (Petal petal : model.petals) {
      addLayer(new PetalLayer(lx, petal));
    }
  }

  class PetalLayer extends LXLayer {

    private final Petal petal;

    private final LXModulator pos;
    private final LXModulator size;

    PetalLayer(LX lx, Petal petal) {
      super(lx);
      this.petal = petal;
      this.pos = startModulator(new SinLFO(0, petal.size-1, period).randomBasis());
      this.size = startModulator(new SinLFO(minSize, maxSize, sizeRate).randomBasis());
    }

    public void run(double deltaMs) {
      int i = 0;
      float pos = this.pos.getValuef();
      float size = this.size.getValuef();
      float falloff = 1.f / size;
      float invf = invert.getValuef();
      float minb = minBrightness.getValuef();
      float contrastf = contrast.getValuef();
      for (LXPoint p : petal.points) {
        float d = Math.min(1, falloff * Math.abs(i - pos));
        d = LXUtils.lerpf(d, d*d*d, contrastf);
        float b = Math.min(100, minb + 100*d);
        b = LXUtils.lerpf(b, 100-b+minb, invf);
        colors[p.index] = LXColor.gray(b);
        ++i;
      }
    }
  }

  public void run(double deltaMs) {
    setColors(LXColor.BLACK);
  }
}
