package org.templegalaxia;

import heronarts.lx.LX;
import heronarts.lx.LXLoopTask;
import heronarts.lx.model.LXModel;
import heronarts.lx.studio.LXStudio;
import org.templegalaxia.configuration.Outputs;
import org.templegalaxia.model.Temple;

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
    Outputs outputs = new Outputs(lx, model);

    lx.engine.addLoopTask(new LXLoopTask() {
      @Override
      public void loop(double v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHeartBeat > 1000.0) {
          lastHeartBeat = currentTime;
          System.out.println(lx.engine.getDefaultChannel().getOscAddress());
        }
      }
    });

    // Kick it!
    System.out.println("Starting engine!");
    lx.engine.start();
  }

}
