package org.templegalaxia;

import heronarts.lx.LX;
import heronarts.lx.LXChannelBus;
import heronarts.lx.model.LXModel;
import org.templegalaxia.configuration.SiteConfiguration;
import org.templegalaxia.datagrams.MultiplexedArtNet;
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
    Temple model = new Temple();

    // Initialize LX
    lx = new LX(model);
    GalaxiaUtils.registerPatterns(lx);

    // TODO (meawoppl) Fix up outputs here
    GalaxiaGui.addMappedOutputs(lx, model);

    lx.openProject(new File("projects/Default.lxp"));

    LXChannelBus background = lx.engine.getChannel("Background");
    LXChannelBus ground = lx.engine.getChannel("Ground");
    LXChannelBus pattern = lx.engine.getChannel("Pattern");

    if (background == null || ground == null || pattern == null) {
      System.err.println("Expected channels not found, did Default.lxp load properly?");
    } else {
      // NOTE(mcslee): if there is a need to manually override the brightness levels set in the project file
      // pattern.fader.setNormalized(1.);
      // ground.fader.setNormalized(1.);
      // background.fader.setNormalized(0.5);
    }

    // Kick it!
    System.out.println("Starting engine!");
    lx.engine.start();
  }
}
