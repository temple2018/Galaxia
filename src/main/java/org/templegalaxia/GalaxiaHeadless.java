package org.templegalaxia;

import heronarts.lx.LX;
import heronarts.lx.LXLoopTask;
import heronarts.lx.model.LXModel;
import org.templegalaxia.configuration.Outputs;
import org.templegalaxia.model.Temple;

import java.io.File;

public class GalaxiaHeadless {
  private long lastHeartBeat = System.currentTimeMillis();

  private LXModel model;
  private LX lx;

  public static void main(String args[]) {
    new GalaxiaHeadless();
  }

  public GalaxiaHeadless() {
    System.out.println("Starting 'headless' Galaxia");

    // Load model
    LXModel model = new Temple();

    // Initialize LX
    lx = new LX(model);
    GalaxiaUtils.registerPatterns(lx);
    Outputs outputs = new Outputs(lx, model);
    lx.openProject(new File("projects/Default.lxp"));

    // Kick it!
    System.out.println("Starting engine!");
    lx.engine.start();
  }
}
