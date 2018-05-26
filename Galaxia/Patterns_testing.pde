class MoveXPosition extends TemplePattern {
  private final BoundedParameter xPos = new BoundedParameter("XPos", 0, model.xMin, model.xMax);

  public MoveXPosition(LX lx) {
    super(lx);
      addParameter(xPos);
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      float brightnessValue = max(0, 100 - abs(p.x - xPos.getValuef()));
      colors[p.index] = lx.hsb(0, 0, brightnessValue);
    }  
  }
}

class MoveYPosition extends TemplePattern {
  private final BoundedParameter yPos = new BoundedParameter("XPos", 0, model.yMin, model.yMax);

  public MoveYPosition(LX lx) {
    super(lx);
      addParameter(yPos);
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      float brightnessValue = max(0, 100 - abs(p.y - yPos.getValuef()));
      colors[p.index] = lx.hsb(0, 0, brightnessValue);
    }  
  }
}

class TestYPattern extends TemplePattern {
  private final SinLFO yPos = new SinLFO(model.yMin, model.yMax, 4000);
  public TestYPattern(LX lx) {
    super(lx);
    addModulator(yPos).trigger();
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      float bv = max(0, 100 - abs(p.y - yPos.getValuef()));
      colors[p.index] = lx.hsb(0, 0, bv);
    }
  }
}
