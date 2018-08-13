package org.templegalaxia.patterns.pandores;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.*;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("PanDoreS")
public class SpaceshipLoading extends TemplePattern {
  BoundedParameter Speed = new BoundedParameter("Speed", 25000, 1000, 30000);
  BooleanParameter Inversed = new BooleanParameter("Inversed", false);

  SinLFO SinLFO_Var = new SinLFO(model.yMin, model.yMax, Speed.getValue());

  public SpaceshipLoading(LX lx) {
    super(lx);
    addModulator(SinLFO_Var).trigger();
    addParameter(Speed);
    addParameter(Inversed);
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      SinLFO_Var.setPeriod(Speed.getValue());
      if (Inversed.getValueb()) {
        if (p.y <= SinLFO_Var.getValue()) {
          colors[p.index] = LXColor.BLACK;
        } else {
          colors[p.index] = LXColor.WHITE;
        }
      } else {
        if (p.y <= SinLFO_Var.getValue()) {
          colors[p.index] = LXColor.WHITE;
        } else {
          colors[p.index] = LXColor.BLACK;
        }
      }
    }
  }
}
