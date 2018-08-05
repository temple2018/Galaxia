package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;
import java.util.*;
import org.templegalaxia.patterns.TemplePattern;
import processing.core.PApplet;

public class Temple extends LXModel {
  private static final int NUMBER_OF_PETALS = 20;
  private static final float ANGLE_BETWEEN_PETALS = PApplet.radians(360 / NUMBER_OF_PETALS);

  public final List<Petal> petals;
  public final List<Ring> rings;
  private final Map<LXPoint, Integer> pointToPixelMap;

  public Temple(PApplet applet) {
    super(new Fixture(applet));

    Fixture f = (Fixture) this.fixtures.get(0);

    this.petals = Collections.unmodifiableList(f.petals);
    this.rings = Collections.unmodifiableList(f.rings);
    this.pointToPixelMap = Collections.unmodifiableMap(f.pointToPixelMap);
  }

  /**
   * Return the global pixel index of the given model point
   *
   * @param point the point in question
   * @return The global pixel index
   */
  public int getPixelIndex(LXPoint point) {
    return this.pointToPixelMap.get(point);
  }

  private static class Fixture extends LXAbstractFixture {
    private final List<Petal> petals = new ArrayList<>();
    private final List<Ring> rings = new ArrayList<>();
    private final Map<LXPoint, Integer> pointToPixelMap = new HashMap<>();

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

      for (int pointItr = 0; pointItr < Petal.numPixels; ++pointItr) {
        List<LXPoint> points = new ArrayList<>();

        for (int petalItr = 0; petalItr < NUMBER_OF_PETALS; ++petalItr) {
          LXPoint point = petals.get(petalItr).getPoints().get(pointItr);
          points.add(point);
          int pixelIdx = TemplePattern.petalIndexToPointIndex(pointItr, petalItr);
          pointToPixelMap.put(point, pixelIdx);
        }
        rings.add(new Ring(points));
      }
    }
  }
}
