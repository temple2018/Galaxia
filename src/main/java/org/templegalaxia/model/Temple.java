package org.templegalaxia.model;

import heronarts.lx.model.LXAbstractFixture;
import heronarts.lx.model.LXModel;

public class Temple extends LXModel {

    public Temple() {
        super(new Fixture());
    }

    private static class Fixture extends LXAbstractFixture {

        Fixture() {

        }
    }
}
