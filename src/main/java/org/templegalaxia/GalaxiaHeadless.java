package org.templegalaxia;

import heronarts.lx.LX;
import heronarts.lx.model.LXModel;
import heronarts.lx.studio.LXStudio;
import org.templegalaxia.configuration.Outputs;
import org.templegalaxia.configuration.PatternLoader;
import org.templegalaxia.model.Temple;
import org.templegalaxia.patterns.gerald.*;
import org.templegalaxia.patterns.matty.*;
import org.templegalaxia.patterns.testing.*;
import processing.core.PApplet;

public class GalaxiaHeadless extends PApplet {
    private long lastHeartBeat = System.currentTimeMillis();

    private LXModel model;
    private LX lx;

    public static void main(String args[]) {
        PApplet.main(new String[] {GalaxiaHeadless.class.getName()});
    }

    public void settings() {
        size(1, 1, P2D);
    }

    public void setup() {
        PApplet.println("Starting 'headless' Galaxia");

        // Load model
        LXModel model = new Temple(this);

        // Initialize LX
        lx = new LX(model);

        // Kick it!
        System.out.println("Starting engine!");
        lx.engine.start();

    }

    // NOTE(meawoppl) this wants to be a classpath scan for annotations.
    public void initialize(LXStudio lx, LXStudio.UI ui) {
        Outputs outputs = new Outputs(lx, model);
        PatternLoader loader = new PatternLoader(lx);
    }

    public void draw() {
        // Wipe the frame...
        background(0x000);

        lx.engine.onDraw();

        if (!lx.engine.isThreaded()) {
            lx.engine.run();
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHeartBeat > 1000.0) {
            lastHeartBeat = currentTime;
            System.out.println(lx.engine.getDefaultChannel().getOscAddress());
        }
    }
}
