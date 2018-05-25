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

