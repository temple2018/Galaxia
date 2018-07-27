package org.templegalaxia.patterns.pandores;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.Click;
import heronarts.lx.parameter.*;
import java.util.Random;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("PanDoreS")
public class EiffelTower extends TemplePattern {
  private final BoundedParameter Stars = new BoundedParameter("Stars", 1, 1, 20);
  BoundedParameter Speed = new BoundedParameter("Speed", 50, 0, 1000);
  // Random background activated or not
  BooleanParameter Mode = new BooleanParameter("Mode", false);

  Click Click_Var = new Click(Speed.getValue());

  private final Random Random_Var;

  public EiffelTower(LX lx) {
    super(lx);
    addModulator(Click_Var).trigger();
    addParameter(Stars);
    addParameter(Speed);
    addParameter(Mode);
    Random_Var = new Random();
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      Click_Var.setPeriod(Speed.getValue());
      if (Click_Var.click()) {
        if (Random_Var.nextInt(100) <= Stars.getValue()) {
          if (!Mode.getValueb()) {
            colors[p.index] = LXColor.WHITE;
          } else {
            colors[p.index] = LXColor.hsb(0, 0, Random_Var.nextInt(100));
          }
        } else {
          colors[p.index] = LXColor.BLACK;
        }
      }
    }
  }
}
