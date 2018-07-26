package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import processing.core.PApplet;

public class Temple extends LXModel {
  private static final float NUMBER_OF_PETALS = 20;
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
    private final List<Ring> rings = new ArrayList<>();

    Fixture(PApplet applet) {
      LXTransform transform = new LXTransform();

      for (int i = 0; i < NUMBER_OF_PETALS; ++i) {
        Petal petal = new Petal(transform);
        addPoints(petal);
        this.petals.add(petal);

        transform.rotateY(ANGLE_BETWEEN_PETALS);
      }

      for (int pointItr = 0; pointItr < Petal.NUM_PIXELS; ++pointItr) {
        List<LXPoint> points = new ArrayList<>();

        for (int petalItr = 0; petalItr < NUMBER_OF_PETALS; ++petalItr) {
          points.add(petals.get(petalItr).getPoints().get(pointItr));
        }
        rings.add(new Ring(points));
      }
    }
  }
}
