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

abstract class TemplePattern extends LXPattern {
  protected final Temple model;

  TemplePattern(LX lx) {
    super(lx);
    model = (Temple)lx.model;
  }
}

abstract class TempleLayer extends LXLayer{
  protected final Temple model;

  TempleLayer(LX lx) {
    super(lx);
    model = (Temple)lx.model;
  }
}
