package org.templegalaxia;

import heronarts.lx.model.LXModel;
import heronarts.lx.studio.LXStudio;
import org.templegalaxia.configuration.Outputs;
import org.templegalaxia.model.Temple;
import org.templegalaxia.patterns.gerald.*;
import org.templegalaxia.patterns.matty.*;
import org.templegalaxia.patterns.ping.Swirl;
import org.templegalaxia.patterns.ted.RingTest;
import org.templegalaxia.patterns.testing.*;
import processing.core.PApplet;

public class GalaxiaGui extends PApplet {
  // System configuration flags
  private static final boolean MULTITHREADED = true;
  private static final boolean RESIZEABLE = true;

  LXModel model;
  LXStudio lx;

  public static void main(String args[]) {
    PApplet.main(new String[] {GalaxiaGui.class.getName()});
  }

  public void settings() {
    size(displayWidth, displayHeight, P3D);
  }

  public void setup() {
    // Load model
    LXModel model = new Temple(this);

    // Initialize LX
    lx = new LXStudio(this, model, MULTITHREADED);

    // Configure UI
    lx.ui.setResizable(RESIZEABLE);
    lx.ui.preview.pointCloud.setPointSize(20);

    // Build outputs
    Outputs outputs = new Outputs(lx, model);
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
    lx.registerPattern(Swirl.class);
    lx.registerPattern(RingTest.class);
    lx.registerPattern(PetalPointIndex.class);
  }

  public void draw() {
    // Handled by LXStudio
  }
}
