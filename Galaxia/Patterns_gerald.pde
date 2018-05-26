class Bounce extends TemplePattern {

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


class PetalChase extends TemplePattern {
  private final BoundedParameter speed = new BoundedParameter("Speed", 2000, 2000, -100);
  private final BoundedParameter fadeDistance = new BoundedParameter("Length", 2, 1, 30);
  private final SawLFO petalPos = new SawLFO(0, 60, speed);

  public PetalChase(LX lx) {
    super(lx);
    addModulator(petalPos).trigger();
    addParameter(speed);
    addParameter(fadeDistance);
  }

  public void run(double deltaMs) {
    int index = int(petalPos.getValuef());
    ArrayList<Integer> brightPoints = new ArrayList<Integer> ();

    for ( Petal petal : model.petals ) {
      brightPoints.add(petal.points[index].index);
 
      for ( LXPoint point: petal.points ) {
        colors[point.index] = LXColor.BLACK;
      }
  
      int fadeLength = int( fadeDistance.getValuef() );
      for ( int point: brightPoints ) {
        for (int ii = 1; ii < fadeLength; ++ii ) {
          float bv = 100 / ii;
          colors[max(0, point - ii)] = lx.hsb(0, 0, bv);
        }
      }
    }
  }
}
