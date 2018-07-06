package org.templegalaxia;

import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXFixture;
import heronarts.lx.output.ArtNetDatagram;

public class DemuxArtNetDatagram extends ArtNetDatagram {
  private static final int ARTNET_HEADER_LENGTH = 18;
  private final int[] pointIndices;

  public static DemuxArtNetDatagram fromFixture(LXFixture fixture, int universe) {
    return new DemuxArtNetDatagram(LXFixture.Utils.getIndices(fixture), universe);
  }

  private DemuxArtNetDatagram(int[] pointIndices, int universe) {
    super(pointIndices, pointIndices.length, universe);
    this.pointIndices = pointIndices;
  }

  public byte luminance(int rgb) {
    float r = LXColor.red(rgb);
    float g = LXColor.green(rgb);
    float b = LXColor.blue(rgb);

    int lum = Math.round(((r + g + b) / 3) * 255 / 100);

    if (lum < 0) lum = 0;
    if (lum > 255) lum = 255;

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
