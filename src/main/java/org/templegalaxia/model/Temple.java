package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.transform.LXTransform;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Temple extends LXModel {
    private static final float NUMBER_OF_PETALS = 20;
    private static final float ANGLE_BETWEEN_PETALS = PApplet.radians(360/NUMBER_OF_PETALS);

    public final List<Petal> petals;

    public Temple(PApplet applet) {
        super(new Fixture(applet));
        Fixture f = (Fixture) this.fixtures.get(0);
        this.petals = Collections.unmodifiableList(f.petals);
    }

    private static class Fixture extends LXAbstractFixture {
        private final List<Petal> petals = new ArrayList<>();

        Fixture(PApplet applet){
            LXTransform transform = new LXTransform();
            for (int i = 0; i < NUMBER_OF_PETALS; ++i) {
                transform.push();
                transform.rotateY(ANGLE_BETWEEN_PETALS);

                Petal petal = Petal.loadFromCsv(applet, transform);
                addPoints(petal);
                this.petals.add(petal);
            }
        }
    }
}
