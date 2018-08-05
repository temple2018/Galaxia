package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;
import java.util.*;
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
    private final List<Ring> rings = new ArrayList<>();

    Fixture(PApplet applet) {
      LXTransform transform = new LXTransform();

      // Build the spokes
      for (int i = 0; i < NUMBER_OF_PETALS; ++i) {
        LowerPetal lowerPetal = new LowerPetal(transform);
        UpperPetal upperPetal = new UpperPetal(transform);

        Petal petal = new Petal(lowerPetal, upperPetal);
        addPoints(petal);

        this.petals.add(petal);

        transform.rotateY(ANGLE_BETWEEN_PETALS);
      }

      for (int pointItr = 0; pointItr < Petal.numPixels; ++pointItr) {
        List<LXPoint> points = new ArrayList<>();

        for (int petalItr = 0; petalItr < NUMBER_OF_PETALS; ++petalItr) {
          LXPoint point = petals.get(petalItr).getPoints().get(pointItr);
          points.add(point);
        }
        rings.add(new Ring(points));
      }
    }
  }
}
