package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.transform.LXTransform;
import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

public class Petal extends LXModel {

    public Petal(Table pixelLocations, LXTransform transform) {
        super(new Fixture(pixelLocations, transform));
    }

    public static Petal loadFromCsv(PApplet applet, LXTransform transform, String fileName) {
        Table pixels = applet.loadTable(fileName, "header");

        return new Petal(pixels, transform);

    }

    private static class Fixture extends LXAbstractFixture {

        Fixture(Table pixelLocations, LXTransform transform) {
            for (TableRow row : pixelLocations.rows()) {
                // NOTE(G3): There is a coordinate systems mismatched between rhino & Processing
                // and this foolery ensures that the temple is upright in processing.
                float x = row.getFloat("y");
                float y = row.getFloat("z");
                float z = row.getFloat("x");

                transform.push();
                transform.translate(x, y, z);

                addPoint(new LXPoint(transform));

                transform.pop();
            }
        }
    }
}
