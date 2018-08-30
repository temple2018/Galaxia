package org.templegalaxia.patterns.pandores;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.SawLFO;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("PanDoreS")
public class Drahciug extends TemplePattern {
  private final BoundedParameter Effect = new BoundedParameter("Effect", 0, 1, 5);

  BoundedParameter Speed = new BoundedParameter("Speed", 50000, 3000, 75000);

  SawLFO saw = new SawLFO(0, 100, Speed.getValuef());

  private float background = 0;

  public Drahciug(LX lx) {
    super(lx);
    addModulator(saw).trigger();
    addParameter(Effect);
    addParameter(Speed);
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      float current_saw = saw.getValuef();
      switch ((int) Effect.getValue()) {
        case 1:
          background += 2;
          saw.setPeriod(Speed.getValuef());
          break;
        case 2:
          background += 4;
          saw.setPeriod(Speed.getValuef() / 2);
          break;
        case 3:
          background += 5;
          saw.setPeriod(Speed.getValuef() / 2);
          break;
        case 4:
          background += 11;
          saw.setPeriod(Speed.getValuef() / 4);
          break;
        case 5:
          background += 17;
          saw.setPeriod(Speed.getValuef() / 5);
          break;
        default:
          background++;
          break;
      }
      if ((background + current_saw) <= 100) {
        colors[p.index] = LXColor.hsb(0, 0, background + current_saw);
      } else {
        colors[p.index] = LXColor.hsb(0, 0, (background + current_saw) - 100);
      }
      if ((background) >= 100) {
        background = 0;
      }
    }
  }
}
