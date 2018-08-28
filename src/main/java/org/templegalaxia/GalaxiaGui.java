package org.templegalaxia;

import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.studio.LXStudio;
import org.templegalaxia.configuration.SiteConfiguration;
import org.templegalaxia.datagrams.MultiplexedArtNet;
import org.templegalaxia.model.Temple;
import processing.core.PApplet;

import java.util.Map;

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
    Temple model = new Temple();

    // Initialize LX
    lx = new LXStudio(this, model, MULTITHREADED);

    // Configure UI
    lx.ui.setResizable(RESIZEABLE);

    // Open the default project if none is saved
    if (lx.getProject() == null) {
      System.out.println("Loading the Default project");
      lx.openProject(this.saveFile("projects/Default.lxp"));
    }

    // Universe debugging tool
    // addTestOutput(19, model);
     addMappedOutputs(model);
  }

  public void addTestOutput(int universe, Temple model){
    MultiplexedArtNet.addDatagramForFixture(lx, model, SiteConfiguration.IPS[1], universe);
  }

  public void addMappedOutputs(Temple model){
    Map<Integer, String> petalIPMap = SiteConfiguration.getPetalToIPAddress();
    Map<Integer, Integer> petalToLowerUniMap = SiteConfiguration.getLowerConfigs();
    Map<Integer, Integer> petalToUpperUniMap = SiteConfiguration.getUpperConfigs();

    // Add the outputs for the upper petals
    for(int i=0; i < Temple.NUMBER_OF_PETALS; i++) {
      MultiplexedArtNet.addDatagramForFixture(
              lx,
              model.upper.get(i),
              petalIPMap.get(i),
              petalToUpperUniMap.get(i));

      MultiplexedArtNet.addDatagramForFixture(
              lx,
              model.lower.get(i),
              petalIPMap.get(i),
              petalToLowerUniMap.get(i)
      );
    }

  }

  public void initialize(LXStudio lx, LXStudio.UI ui) {
    GalaxiaUtils.registerPatterns(lx);
  }

  public void onUIReady(LXStudio lx, LXStudio.UI ui) {
    lx.ui.preview.pointCloud.setPointSize(20);
  }

  public void draw() {
    // Handled by LXStudio
  }
}
