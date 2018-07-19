package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.transform.LXTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import processing.core.PApplet;

public class Temple extends LXModel {
  private static final int NUMBER_OF_PETALS = 20;
  private static final float ANGLE_BETWEEN_PETALS = PApplet.radians(360 / NUMBER_OF_PETALS);

  public final List<Petal> petals;

  public Temple(PApplet applet) {
    super(new Fixture(applet));
    Fixture f = (Fixture) this.fixtures.get(0);
    this.petals = Collections.unmodifiableList(f.petals);
  }

  private static class Fixture extends LXAbstractFixture {
    private final List<Petal> petals = new ArrayList<>();
    private final List<Ring> rings = new ArrayList<Ring>();

    Fixture(PApplet applet) {
      LXTransform transform = new LXTransform();
      int pixelsPerPetal = new Petal(transform).numPixels;
      for (int i = 0; i < pixelsPerPetal; i++) {
        rings.add(new Ring(NUMBER_OF_PETALS));
      }

      for (int i = 0; i < NUMBER_OF_PETALS; ++i) {
        Petal petal = new Petal(transform);
        addPoints(petal);
        this.petals.add(petal);

        petal.getPoints().forEach((point) -> {});
        for (int j = 0; j < petal.getPoints().size(); j++) {
          rings.get(j).addPoint(petal.getPoints().get(j));
        }

        transform.rotateY(ANGLE_BETWEEN_PETALS);
      }
    }
  }
}
