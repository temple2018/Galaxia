package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;
import java.util.*;

import org.templegalaxia.datagrams.MultiplexedArtNet;
import processing.core.PApplet;

public class Temple extends LXModel {
  public static final int NUMBER_OF_PETALS = 20;
  private static final float ANGLE_BETWEEN_PETALS = PApplet.radians(360 / NUMBER_OF_PETALS);

  public final List<Petal> petals;
  public final List<Ring> rings;

  public final List<RotatableFixture> lower;
  public final List<RotatableFixture> upper;

  public final List<Spoke> spokes;

  public Temple() {
    super(new TempleFixture());

    TempleFixture f = (TempleFixture) this.fixtures.get(0);

    this.petals = Collections.unmodifiableList(f.petals);

    this.lower = Collections.unmodifiableList(f.lower);
    this.upper = Collections.unmodifiableList(f.upper);

    this.rings = Collections.unmodifiableList(f.rings);
    this.spokes = Collections.unmodifiableList(f.spokes);
  }

  private static class TempleFixture extends LXAbstractFixture {
    private final List<Petal> petals = new ArrayList<>();
    private final List<RotatableFixture> upper = new ArrayList<>();
    private final List<RotatableFixture> lower = new ArrayList<>();

    private final List<Ring> rings = new ArrayList<>();
    private final List<Spoke> spokes = new ArrayList<>();

    private static PointLoader lowerPetalLoader = new PointLoader("lowerPetalPoints.csv");
    private static PointLoader upperPetalLoader = new PointLoader("upperPetalPoints.csv");

    TempleFixture() {
      LXTransform transform = new LXTransform();

      for (int i = 0; i < NUMBER_OF_PETALS; ++i) {
        RotatableFixture lowerPetal = new RotatableFixture(transform, lowerPetalLoader);
        lower.add(lowerPetal);

        RotatableFixture upperPetal = new RotatableFixture(transform, upperPetalLoader);
        upper.add(upperPetal);

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
