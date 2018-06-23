package org.templegalaxia;

import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXPoint;
import heronarts.lx.output.LXDatagram;
import java.util.Iterator;
import java.util.List;

public class DemuxArtNetDatagram extends LXDatagram {
  private static final int DEFAULT_UNIVERSE = 0;
  private static final int ARTNET_HEADER_LENGTH = 18;
  private static final int ARTNET_PORT = 6454;
  private static final int SEQUENCE_INDEX = 12;
  private final int[] pointIndices;
  private byte sequence;

  public DemuxArtNetDatagram(int[] var1) {
    this((int[]) var1, DEFAULT_UNIVERSE);
  }

  public DemuxArtNetDatagram(LXFixture fixture, int universe) {
    this(fixtureToIndices(fixture), universe);
  }

  public DemuxArtNetDatagram(int[] indices, int universe) {
    this(indices, 3 * indices.length, universe);
  }

  public DemuxArtNetDatagram(LXFixture var1, int dataLength, int universe) {
    this(fixtureToIndices(var1), dataLength, universe);
  }

  // Stolen from LXOutput
  static int[] fixtureToIndices(LXFixture var0) {
    List points = var0.getPoints();
    int[] indices = new int[points.size()];
    int var3 = 0;

    // This loop is unrolled weirdly, but I believe returns a int[] of
    // the indices represented by the fixture
    LXPoint var5;
    for (Iterator pointsIter = points.iterator();
        pointsIter.hasNext();
        indices[var3++] = var5.index) {
      var5 = (LXPoint) pointsIter.next();
    }

    return indices;
  }

  public DemuxArtNetDatagram(int[] indices, int dataLength, int universe) {
    super(ARTNET_HEADER_LENGTH + indices.length);
    if (indices.length % 3 != 0) {
      throw new AssertionError("Indicies has the wrong length:" + indices.length);
    }
    this.sequence = 1;
    this.pointIndices = indices;

    this.setPort(ARTNET_PORT);
    this.buffer[0] = 65;
    this.buffer[1] = 114;
    this.buffer[2] = 116;
    this.buffer[3] = 45;
    this.buffer[4] = 78;
    this.buffer[5] = 101;
    this.buffer[6] = 116;
    this.buffer[7] = 0;
    this.buffer[8] = 0;
    this.buffer[9] = 80;
    this.buffer[10] = 0;
    this.buffer[11] = 14;
    this.buffer[12] = 0;
    this.buffer[13] = 0;
    this.buffer[14] = (byte) (universe & 255);
    this.buffer[15] = (byte) (universe >>> 8 & 255);
    this.buffer[16] = (byte) (dataLength >>> 8 & 255);
    this.buffer[17] = (byte) (dataLength & 255);

    for (int var4 = ARTNET_HEADER_LENGTH; var4 < this.buffer.length; ++var4) {
      this.buffer[var4] = 0;
    }
  }

  public byte luminance(int rgb) {
    int r = LXColor.red(rgb);
    int g = LXColor.green(rgb);
    int b = LXColor.blue(rgb);

    float lum = Math.round((r+g+b)/3);

    if (lum < 0)
      lum = 0;

    if(lum > 255)
      lum = 255;


    System.out.println(String.format("R %d G %d B%d -> (%d)", r, g, b, (byte) lum));
    return (byte) lum;
  }

  public void onSend(int[] colors) {
    // colors[] and this.pointIndices are 1:1 shape wise
    // Here we map colors[0] -> "R", colors[1] -> "G", colors[2] -> "B"

    int offset = 0;
    for (int ptIndex : this.pointIndices) {
      this.buffer[ARTNET_HEADER_LENGTH + offset] = (byte) (255 - luminance(colors[ptIndex]));
      ++offset;
    }
  }
}
