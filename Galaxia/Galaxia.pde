import java.util.*;

// Reference to top-level LX instance
heronarts.lx.studio.LXStudio lx;

LXPattern[] patterns(P3LX lx) {
  return new LXPattern[] {
    // Initial test patterns
    new TestYPattern(lx),
    new MoveXPosition(lx),
    new MoveYPosition(lx),
    new Bounce(lx),
  };
}

void setup() {
  // Processing setup, constructs the window and the LX instance
  size(800, 60, P3D);
  lx = new heronarts.lx.studio.LXStudio(this, buildModel(), MULTITHREADED);
  lx.ui.setResizable(RESIZABLE);
  
  setupPatterns();
  
}

void initialize(final heronarts.lx.studio.LXStudio lx, heronarts.lx.studio.LXStudio.UI ui) {
  // Add custom components or output drivers here
}

void onUIReady(heronarts.lx.studio.LXStudio lx, heronarts.lx.studio.LXStudio.UI ui) {
  ui.preview.pointCloud.setPointSize(10);
}

void draw() {
  // All is handled by LX Studio
}

// Configuration flags
final static boolean MULTITHREADED = true;
final static boolean RESIZABLE = true;

void setupPatterns() {
  LXPattern[] templePatterns = patterns(lx);
  LXChannel channel = (LXChannel)lx.engine.getFocusedChannel();
  LXPattern iteratorPattern= channel.getPatterns().get(0);
  
  // Add all patterns from the main list.
  for (LXPattern pattern: templePatterns) {    
    channel.addPattern(pattern);    
  }
  
  channel.removePattern(iteratorPattern);
}
