package org.templegalaxia.patterns.ted;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.BoundedParameter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.templegalaxia.model.Ring;
import org.templegalaxia.patterns.TemplePattern;

@LXCategory("Test")
public class RingTest extends TemplePattern {
  double period = 1000.0; // one second

  private BoundedParameter speedParam =
      new BoundedParameter("Speed", 2500, 5000, 200).setDescription("Speed of chase");

  private SinLFO ringLFO = new SinLFO(0, model.rings.size() - 1, speedParam);
  private Set<Integer> selectedPointIndexes = new HashSet<Integer>();

  public RingTest(LX lx) {
    super(lx);
    addModulator(ringLFO).trigger();
    addParameter(speedParam);
  }

  @Override
  protected void run(double deltaMs) {
    int selectedIdx = Math.round(ringLFO.getValuef());
    List<Ring> rings = model.rings;
    assert selectedIdx < rings.size();
    Ring selectedRing = rings.get(selectedIdx);

    selectedPointIndexes.clear();
    for (LXPoint point : selectedRing.getPoints()) {
      selectedPointIndexes.add(point.index);
    }
    for (int i = 0; i < colors.length; i++) {
      if (selectedPointIndexes.contains(i)) {
        colors[i] = LXColor.hsb(0, 0, 100);
      } else {
        colors[i] = LXColor.BLACK;
      }
    }
  }
}
