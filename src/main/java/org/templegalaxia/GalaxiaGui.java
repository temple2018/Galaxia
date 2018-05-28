package org.templegalaxia;

import heronarts.lx.model.LXModel;
import heronarts.lx.model.StripModel;
import heronarts.lx.studio.LXStudio;
import processing.core.PApplet;

public class GalaxiaGui extends PApplet {
    // Configuration flags
    private static final boolean MULTITHREADED = true;

    LXModel model;
    LXStudio lxStudio;

    static public void main(String args[]) {
        PApplet.main(new String[]{"--present", GalaxiaGui.class.getName()});
    }

    public void settings() {
        size(displayWidth, displayHeight, P3D);
    }

    public void setup() {
        GalaxiaGui applet = this; // Reference to PApplet

        model = new StripModel(10);

        lxStudio = new LXStudio(this, model, MULTITHREADED) {
            public void initialize(LXStudio lx, LXStudio.UI ui) {

            }

            public void onUIReady(LXStudio lx, LXStudio.UI ui) {

            }
        };
    }

    public void draw() {
        // Handled by LXStudio
    }
}