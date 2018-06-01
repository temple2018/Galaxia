package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import processing.core.PApplet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Petal extends LXModel {
    public final static int NUM_PIXELS;

    public Petal(LXTransform transform) {
        super(new Fixture(transform));
    }

    private static List<float[]> xyzs = new ArrayList<>();

    // Statically initialize the CSV load on the first init
    static {
        if (xyzs.size() == 0) {
            CSVParser parser;
            try {
                parser = new CSVParser(
                        new BufferedReader(new InputStreamReader(Petal.class.getResourceAsStream("points.csv"))),
                        CSVFormat.EXCEL.withHeader());
            } catch (Exception e) {
                // Coherce to non checked exceptions.
                throw new RuntimeException(e);
            }

            // Read in all the xyzs
            // NOTE(G3): There is a coordinate systems mismatched between rhino & Processing
            // and this foolery ensures that the temple is upright in processing.
            for (CSVRecord csvRecord : parser) {
                xyzs.add(new float[]{
                        Float.parseFloat(csvRecord.get("y")),
                        Float.parseFloat(csvRecord.get("z")),
                        Float.parseFloat(csvRecord.get("x"))
                });
            }

            System.out.println(String.format("Loaded %d points.", xyzs.size()));
        }
        NUM_PIXELS = xyzs.size();
    }

    private static class Fixture extends LXAbstractFixture {
        Fixture(LXTransform transform) {
            for (float[] xyz : xyzs) {
                transform.push();

                transform.translate(xyz[0], xyz[1], xyz[2]);
                addPoint(new LXPoint(transform));

                transform.pop();
            }
        }
    }
}
