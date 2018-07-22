package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.templegalaxia.patterns.TemplePattern;
import processing.core.PApplet;

public class Temple extends LXModel {
  private static final int NUMBER_OF_PETALS = 20;
  private static final float ANGLE_BETWEEN_PETALS = PApplet.radians(360 / NUMBER_OF_PETALS);

  public final List<Petal> petals;
  public final List<Ring> rings;

  public Temple(PApplet applet) {
    super(new Fixture(applet));
    Fixture f = (Fixture) this.fixtures.get(0);
    this.petals = Collections.unmodifiableList(f.petals);
    this.rings = Collections.unmodifiableList(f.rings);
  }

  private static class Fixture extends LXAbstractFixture {
    private final List<Petal> petals = new ArrayList<>();
    private final List<Ring> rings = new ArrayList<Ring>();

    Fixture(PApplet applet) {
      LXTransform transform = new LXTransform();
      int pixelsPerPetal = -1;

      for (int i = 0; i < NUMBER_OF_PETALS; ++i) {
        Petal petal = new Petal(transform);
        addPoints(petal);
        this.petals.add(petal);
        transform.rotateY(ANGLE_BETWEEN_PETALS);
        if (pixelsPerPetal == -1) pixelsPerPetal = petal.size;
      }

      // Reindex points into rings of the same height.
      for (int i = 0; i < pixelsPerPetal; i++) {
        Ring ring = new Ring();
        for (int j = 0; j < petals.size(); j++) {
          Petal petal = petals.get(j);
          LXPoint point = petal.getPoints().get(i);
          int pixelIdx = TemplePattern.petalIndexToPointIndex(i, j);
          ring.addIndexedPoint(point, pixelIdx);
        }
        rings.add(ring);
      }
      assert rings.size() == petals.get(0).size;
    }
  }
}
