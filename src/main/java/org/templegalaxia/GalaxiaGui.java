package org.templegalaxia;

import heronarts.lx.model.LXModel;
import heronarts.lx.studio.LXStudio;
import org.templegalaxia.configuration.Outputs;
import org.templegalaxia.model.Temple;
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
    LXModel model = new Temple();

    // Initialize LX
    lx = new LXStudio(this, model, MULTITHREADED);

    // Configure UI
    lx.ui.setResizable(RESIZEABLE);

    // Open the default project if none is saved
    if (lx.getProject() == null) {
      System.out.println("Loading the Default project");
      lx.openProject(this.saveFile("projects/Default.lxp"));
    }

    // Build outputs
    Outputs outputs = new Outputs(lx, model);
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
