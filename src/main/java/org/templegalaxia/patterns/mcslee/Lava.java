// Copyright 2018 Mark C. Slee
// Limited license granted for use in Galaxia 2018 Temple at Burning Man

package org.templegalaxia.patterns.mcslee;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.LXLayer;
import heronarts.lx.LXUtils;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.Accelerator;
import heronarts.lx.parameter.CompoundParameter;
import org.templegalaxia.model.Petal;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("Slee")
public class Lava extends TemplePattern {

  private static final int DROPS_PER_PETAL = 10;

  public final CompoundParameter speed =
      new CompoundParameter("Speed", 2, 0, 4).setDescription("Initial velocity of raindrops");

  public final CompoundParameter gravity =
      new CompoundParameter("Gravity", 2, 0, 4)
          .setDescription("Strength of gravity acting on raindrops");

  public final CompoundParameter size =
      new CompoundParameter("Size", 10, 4, 20).setDescription("Size of the raindrops");

  public Lava(LX lx) {
    super(lx);
    for (Petal petal : model.petals) {
      for (int i = 0; i < DROPS_PER_PETAL; ++i) {
        addLayer(new Drop(lx, petal));
      }
    }
    addParameter("speed", this.speed);
    addParameter("gravity", this.gravity);
    addParameter("size", this.size);
  }

  class Drop extends LXLayer {

    private final Petal petal;

    private double size;
    private final Accelerator pos = (Accelerator) startModulator(new Accelerator("Accel", 0, 0, 0));
    private boolean started = false;

    Drop(LX lx, Petal petal) {
      super(lx);
      this.petal = petal;
      init();
      this.pos.setValue(LXUtils.random(0, this.petal.size));
    }

    void init() {
      this.size = Lava.this.size.getValue() * LXUtils.random(.8, 1.2);
      this.pos.setVelocity(-speed.getValue() * LXUtils.random(.75, 1.25));
      this.pos.setValue(this.petal.size + 100 / this.size);
      this.pos.setAcceleration(-gravity.getValue() * LXUtils.random(.5, 2));
      this.started = false;
    }

    public void run(double deltaMs) {
      boolean init = true;
      int pi = 0;
      double falloff = 100 / this.size;
      double pos = this.pos.getValue();
      for (LXPoint p : petal.points) {
        double b = 100 - falloff * Math.abs(pi - pos);
        if (b > 0) {
          init = false;
          this.started = true;
          addColor(p.index, LXColor.gray(b));
        }
        ++pi;
      }
      if (this.started && init) {
        init();
      }
    }
  }

  public void run(double deltaMs) {
    setColors(LXColor.BLACK);
  }
}
