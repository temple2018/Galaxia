package org.templegalaxia.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class PointLoader {
  private List<float[]> pointArray = new ArrayList<>();
  private int numPixels = 0;
  private String resourceName;
  private boolean loadPoints = true;

  // NOTE(G3): There is a coordinate systems mismatched between rhino & Processing
  private static final String DEFAULT_COORDINATE_ORDER = "yzx";
  private String coordinateOrder;

  public PointLoader(String resourceName) {
    this(resourceName, DEFAULT_COORDINATE_ORDER);
  }

  public PointLoader(String resourceName, String coordinateOrder) {
    this.coordinateOrder = coordinateOrder;
    this.resourceName = resourceName;

    loadResources();
  }

  public List<float[]> getPoints() {
    return pointArray;
  }

  public int getNumPixels() {
    return numPixels;
  }

  private void loadResources() {
    if (loadPoints) {
      CSVParser parser;
      try {
        parser =
            new CSVParser(
                new BufferedReader(
                    new InputStreamReader(Petal.class.getResourceAsStream(resourceName))),
                CSVFormat.EXCEL.withHeader());
      } catch (Exception e) {
        // Coerce to non checked exceptions.
        throw new RuntimeException(e);
      }

      // Read in all the pointArray
      for (CSVRecord csvRecord : parser) {
        float[] pointCoordinates = new float[coordinateOrder.length()];

        int ii = 0;
        for (char ordinate : coordinateOrder.toCharArray()) {
          pointCoordinates[ii] = Float.parseFloat(csvRecord.get(String.valueOf(ordinate)));
          ii += 1;
        }
        pointArray.add(pointCoordinates);
      }
      loadPoints = false;
      System.out.println(
          String.format("Loaded %d points from %s", pointArray.size(), resourceName));
    } else {
      System.out.println(String.format("Already loaded points for %s, skipping!", resourceName));
    }
    this.numPixels = pointArray.size();
  }
}
