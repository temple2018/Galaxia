import heronarts.lx.parameter.BoundedParameter;

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

class MoveXPosition extends TemplePattern {
  private final BoundedParameter xPos = new BoundedParameter("XPos", 0, model.xMin, model.xMax);

  public MoveXPosition(LX lx) {
    super(lx);
      addParameter(xPos);
  }

  public void run(double deltaMs) {
    for (LXPoint p : model.points) {
      float brightnessValue = max(0, 100 - abs(p.x - xPos.getValuef()));
      colors[p.index] = lx.hsb(255, 0, brightnessValue);
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
      colors[p.index] = lx.hsb(255, 0, brightnessValue);
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
      colors[p.index] = lx.hsb(360, 0, bv);
    }
  }
}

class Bounce extends LXPattern {

  private final SinLFO yPos = new SinLFO(0, model.yMax, model.yMin);
  private final BoundedParameter thickness = new BoundedParameter("thick", .01, .01, .06);

  public Bounce(LX lx) {
    super(lx);
    addModulator(yPos).trigger();
    addParameter(thickness);
  }
  public void run(double deltaMs) {
    float hv = palette.getHuef();
    for (LXPoint p : model.points) {
      float bv = max(0, 100 - abs(p.y - yPos.getValuef()) * thickness.getValuef());
      colors[p.index] = lx.hsb(hv, 0, bv);
    }
  }
}

//class Swirling extends TemplePattern {
//  public Swirling(LX lx){
//    super(lx);
//  }
  
//  public void run(double deltaMs) {
//    for (Petal petal: model.petals) {
      
//  }
//}
