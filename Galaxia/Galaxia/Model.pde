private static final int NUMBER_OF_PETALS = 20;
private static final float ANGLE_BETWEEN_PETALS = radians(360/NUMBER_OF_PETALS);
private static final String PIXEL_FILE = "data/points.csv";

LXModel buildModel() {
  return new Temple(new PixelData());
}

class PixelData {
  
  private final Table table; 
  
  PixelData() {
    this.table = loadTable("data/points.csv", "header");
  }
}

public static class Temple extends LXModel {
  public final List<Petal> petals;
  
  public Temple(PixelData config){
    super(new Fixture(config));
    Fixture f = (Fixture) this.fixtures.get(0);
    this.petals = Collections.unmodifiableList(f.petals);
  }
  
  private static class Fixture extends LXAbstractFixture {
    private final List<Petal> petals = new ArrayList<Petal>();
    
    Fixture(PixelData config){
      for (int i = 0; i < NUMBER_OF_PETALS; ++i) {
        LXTransform transform = new LXTransform();
        transform.push();
        transform.rotateY(ANGLE_BETWEEN_PETALS * i);

        Petal petal = new Petal(config, transform);
        addPoints(petal);
        this.petals.add(petal);
      }
    }
  }
}

public static class Petal extends LXModel {
  
  public Petal(PixelData config, LXTransform transform) {
    super(new Fixture(config, transform));
  }
  
  public static class Fixture extends LXAbstractFixture {
    
    Fixture(PixelData config, LXTransform transform) {
      for (TableRow row : config.table.rows()) {
        float x = row.getFloat("x");
        float y = row.getFloat("y");
        float z = row.getFloat("z");

        transform.push();
        transform.translate(y, z, x);
        addPoint(new LXPoint(transform));
        transform.pop();
      }
    }
  }
}
