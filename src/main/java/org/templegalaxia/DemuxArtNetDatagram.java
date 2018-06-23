package org.templegalaxia;

import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXPoint;
import heronarts.lx.output.ArtNetDatagram;

import java.util.List;

public class DemuxArtNetDatagram extends ArtNetDatagram {
  private static final int ARTNET_HEADER_LENGTH = 18;
  private final int[] pointIndices;

  public DemuxArtNetDatagram(LXFixture fixture, int universe) {
    super(getIndices(fixture), getIndices(fixture).length, universe);
    pointIndices = getIndices(fixture);
  }

  // Stolen from LXOutput, ty @mcslee
  static int[] getIndices(LXFixture fixture) {
      return LXFixture.Utils.getIndices(fixture);
//      List<LXPoint> points = fixture.getPoints();
//      int[] indices = new int[points.size()];
//      int i = 0;
//      for (LXPoint p : points) {
//        indices[i++] = p.index;
//      }
//      return indices;
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

  @Override
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
