package org.templegalaxia.patterns.alanpersak;

import java.util.Random;

/** Contains generic utility functions. */
public class Util {

  // Returns a random double between min and max with the specified number of steps of precision.
  public static double randomDouble(Random r, double min, double max, int precision) {
    return r.nextInt(precision) * (max - min) / precision + min;
  }

  // Returns a linearly interpolated y value given an x value and two points on a line.
  public static double linearInterpolate(double x, double x0, double y0, double x1, double y1) {
    return (y0 * (x1 - x) + y1 * (x - x0)) / (x1 - x0);
  }
}
