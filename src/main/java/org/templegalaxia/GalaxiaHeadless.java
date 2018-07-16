package org.templegalaxia;

import heronarts.lx.LX;
import heronarts.lx.model.LXModel;
import heronarts.lx.output.LXDatagramOutput;
import heronarts.lx.studio.LXStudio;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.templegalaxia.model.Temple;
import org.templegalaxia.patterns.gerald.*;
import org.templegalaxia.patterns.matty.*;
import org.templegalaxia.patterns.testing.*;
import processing.core.PApplet;

public class GalaxiaHeadless extends PApplet {
    // System configuration flags
    private static final boolean MULTITHREADED = true;
    private static final boolean RESIZEABLE = true;

    private long lastHeartBeat = System.currentTimeMillis();

    private LX lx;

    public static void main(String args[]) {
        PApplet.main(new String[] {"--present", GalaxiaHeadless.class.getName()});
    }

    public void settings() {
        size(1, 1, P2D);
    }

    public void setup() {
        PApplet.println("Starting 'headless' Galaxia");

        GalaxiaHeadless p = this;

        // Load model
        LXModel model = new Temple(p);

        // Initialize LX
        lx = new LX(model);

        // Kick it!
        System.out.println("Starting engine!");
        lx.engine.start();

    }

    // NOTE(meawoppl) this wants to be a classpath scan for annotations.
    public void initialize(LXStudio lx, LXStudio.UI ui) {
        lx.registerPattern(MoveXPosition.class);
        lx.registerPattern(MoveYPosition.class);
        lx.registerPattern(MoveZPosition.class);
        lx.registerPattern(PetalIterator.class);
        lx.registerPattern(Teleport.class);
        lx.registerPattern(PetalChase.class);
        lx.registerPattern(Sparkle.class);
        lx.registerPattern(DebugOrder.class);
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
