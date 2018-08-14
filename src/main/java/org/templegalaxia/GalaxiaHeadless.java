package org.templegalaxia;

import heronarts.lx.LX;
import heronarts.lx.model.LXModel;
import heronarts.lx.studio.LXStudio;
import org.templegalaxia.configuration.Outputs;
import org.templegalaxia.model.Temple;
import processing.core.PApplet;

public class GalaxiaHeadless {
  private long lastHeartBeat = System.currentTimeMillis();

  private LXModel model;

  public static void main(String args[]) {
    PApplet.println("Starting 'headless' Galaxia");

    // Load model
    LXModel model = new Temple(this);

    // Initialize LX
    LX lx = new LX(model);

    // Enable the outputs
    Outputs outputs = new Outputs(lx, model);

    // Kick it!
    System.out.println("Starting engine!");
    lx.engine.start();
    lx.engine.run();
  }
}
