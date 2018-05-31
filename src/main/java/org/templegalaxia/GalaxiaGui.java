package org.templegalaxia;

import heronarts.lx.model.LXModel;
import heronarts.lx.studio.LXStudio;
import org.templegalaxia.model.Temple;
import org.templegalaxia.patterns.testing.*;
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
        startupInfo();

        // Load model
        LXModel model = new Temple(this);

        // Initialize LX
        LXStudio lx = new LXStudio(this, model, MULTITHREADED);

        // Configure UI
        lx.ui.setResizable(RESIZEABLE);
        lx.ui.preview.pointCloud.setPointSize(20);
    }

    public void initialize(LXStudio lx, LXStudio.UI ui) {
        lx.registerPattern(MoveXPosition.class);
        lx.registerPattern(MoveYPosition.class);
        lx.registerPattern(MoveZPosition.class);
    }

    public void draw() {
        // Handled by LXStudio
    }

    private void startupInfo() {
        PApplet.println("Running sketch from ", sketchPath());
    }
}