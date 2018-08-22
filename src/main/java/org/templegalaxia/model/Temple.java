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

  public final List<RotatableFixture> groundArcs;
  public final List<LXPoint> groundPoints;
  public final List<Petal> petals;
  public final List<Ring> rings;
  public final List<Spoke> spokes;

  public Temple() {
    super(new Fixture());

    Fixture f = (Fixture) this.fixtures.get(0);

    this.groundArcs = Collections.unmodifiableList(f.groundArcs);
    this.petals = Collections.unmodifiableList(f.petals);
    this.rings = Collections.unmodifiableList(f.rings);
    this.spokes = Collections.unmodifiableList(f.spokes);

    this.groundPoints = new ArrayList<LXPoint>();
    for (RotatableFixture groundArc : this.groundArcs) {
      for (LXPoint p : groundArc.points) {
        this.groundPoints.add(p);
      }
    }
  }

  private static class Fixture extends LXAbstractFixture {
    private final List<RotatableFixture> groundArcs = new ArrayList<>();
    private final List<Petal> petals = new ArrayList<>();
    private final List<Ring> rings = new ArrayList<>();
    private final List<Spoke> spokes = new ArrayList<>();

    private static PointLoader groundArcLoader = new PointLoader("groundArcPoints.csv");
    private static PointLoader lowerPetalLoader = new PointLoader("lowerPetalPoints.csv");
    private static PointLoader upperPetalLoader = new PointLoader("upperPetalPoints.csv");

    Fixture() {
      LXTransform transform = new LXTransform();

      for (int i = 0; i < NUMBER_OF_PETALS; ++i) {
        RotatableFixture lowerPetal = new RotatableFixture(transform, lowerPetalLoader);
        RotatableFixture groundArc = new RotatableFixture(transform, groundArcLoader);
        RotatableFixture upperPetal = new RotatableFixture(transform, upperPetalLoader);

        addPoints(groundArc);
        this.groundArcs.add(groundArc);

        Petal petal = new Petal(lowerPetal, upperPetal);
        addPoints(petal);
        this.petals.add(petal);

        this.spokes.add(new Spoke(petal, groundArc));

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
