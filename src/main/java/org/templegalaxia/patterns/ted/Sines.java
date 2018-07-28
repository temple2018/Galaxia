package org.templegalaxia.patterns.ted;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.LXModulator;
import heronarts.lx.modulator.SawLFO;
import heronarts.lx.parameter.BoundedParameter;
import org.templegalaxia.model.Ring;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("Tedward")
public class Sines extends TemplePattern {
  BoundedParameter speedParam =
      new BoundedParameter("speed", 10000, 90000, 1000)
          .setDescription("Period of the base oscillator.");
  BoundedParameter freqParam =
      new BoundedParameter("frequency", 2, 0.1, 10).setDescription("Number of cycles per ring.");
  BoundedParameter phaseOffsetParam =
      new BoundedParameter("phaseOffset", 0.12, 0, Math.PI / 2)
          .setDescription("Phase offset between adjacent rings.");
  BoundedParameter brightnessParam =
      new BoundedParameter("brightness", 0.5, 0, 1).setDescription("Global scaling factor.");
  LXModulator time = new SawLFO(0, 2 * Math.PI, speedParam);

  public Sines(LX lx) {
    super(lx);
    addModulator(time).trigger();
    addParameter(speedParam);
    addParameter(freqParam);
    addParameter(phaseOffsetParam);
    addParameter(brightnessParam);
  }

  @Override
  protected void run(double v) {
    double phase = 0;
    for (int i = 0; i < model.rings.size(); i++) {
      Ring ring = model.rings.get(i);
      int j = 0;
      for (LXPoint point : ring.getPoints()) {
        double angle = 2 * Math.PI / ring.getPoints().size() * j;
        double value = Math.sin(time.getValue() + freqParam.getValue() * angle + phase);
        value = 100 * Math.abs(value); // normalize
        value *= brightnessParam.getValue(); // attenuate
        colors[model.getPixelIndex(point)] = LXColor.hsb(0, 0, value);
        j++;
      }
      phase += phaseOffsetParam.getValue();
    }
  }
}
