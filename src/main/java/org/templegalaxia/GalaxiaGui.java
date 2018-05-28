package org.templegalaxia;

import heronarts.lx.model.LXModel;
import heronarts.lx.studio.LXStudio;
import org.templegalaxia.model.GridModel3D;
import processing.core.PApplet;

public class GalaxiaGui extends PApplet {
    // System configuration flags
    private static final boolean MULTITHREADED = true;
    private static final boolean RESIZEABLE = true;


    static public void main(String args[]) {

        PApplet.main(new String[]{"--present", GalaxiaGui.class.getName()});
    }

    public void settings() {

        size(displayWidth, displayHeight, P3D);
    }

    public void setup() {
        // Load model
        LXModel model = new GridModel3D();

        // Initialize LX
        LXStudio lx = new LXStudio(this, model, MULTITHREADED);

        // Configure UI
        lx.ui.setResizable(RESIZEABLE);
        lx.ui.preview.pointCloud.setPointSize(30);
    }

    public void draw() {
        // Handled by LXStudio
    }
}