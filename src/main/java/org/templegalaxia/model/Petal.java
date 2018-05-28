package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import processing.data.Table;
import processing.data.TableRow;

public class Petal extends LXModel {

    public Petal(Table pixelLocations) {
        super(new Fixture(pixelLocations));
    }

    private static class Fixture extends LXAbstractFixture {

        Fixture(Table pixelLocations) {
            for (TableRow row : pixelLocations.rows()) {
                float x = row.getFloat("x");
                float y = row.getFloat("y");
                float z = row.getFloat("z");

                addPoint(new LXPoint(x,y,z));
            }
        }
    }
}
