package org.templegalaxia.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class PointLoader {
  private List<float[]> xyzs = new ArrayList<>();
  private int numPixels = 0;
  private String resourceName;

  // NOTE(G3): There is a coordinate systems mismatched between rhino & Processing
  private String coordinateOrder = "yzx";

  public PointLoader(String resourceName) {
    this.resourceName = resourceName;

    LoadResource();
  }

  public PointLoader(String resourceName, String coordinateOrder) {
    this.coordinateOrder = coordinateOrder;
    this.resourceName = resourceName;

    LoadResource();
  }

  public List<float[]> getPoints() {
    return xyzs;
  }

  public int getNumPixels() {
    return numPixels;
  }

  private void LoadResource() {
    if (xyzs.size() == 0) {
      CSVParser parser;
      try {
        System.out.println("YO");
        parser =
            new CSVParser(
                new BufferedReader(
                    new InputStreamReader(Petal.class.getResourceAsStream(resourceName))),
                CSVFormat.EXCEL.withHeader());
      } catch (Exception e) {
        // Coerce to non checked exceptions.
        throw new RuntimeException(e);
      }

      // Read in all the xyzs
      for (CSVRecord csvRecord : parser) {
        float[] pointCoordinates = new float[coordinateOrder.length()];

        int ii = 0;
        for (char ordinate : coordinateOrder.toCharArray()) {
          pointCoordinates[ii] = Float.parseFloat(csvRecord.get((String.valueOf(ordinate))));
          ii += 1;
        }
        xyzs.add(pointCoordinates);
      }

      System.out.println(String.format("Loaded %d points.", xyzs.size()));
    }
    this.numPixels = xyzs.size();
  }
}
