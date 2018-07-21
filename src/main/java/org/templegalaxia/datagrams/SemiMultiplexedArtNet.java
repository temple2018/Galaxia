package org.templegalaxia.datagrams;

import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXFixture;

public class SemiMultiplexedArtNet extends MultiplexedArtNet {
  private static final int ARTNET_HEADER_LENGTH = 18;

  // Point index lists for
  private final int[] multiplexed;
  private final int[] notMultiplexed;

  public static SemiMultiplexedArtNet fromFixture(
      LXFixture demuxed, LXFixture muxed, int universe) {
    return new SemiMultiplexedArtNet(
        LXFixture.Utils.getIndices(demuxed), LXFixture.Utils.getIndices(muxed), universe);
  }

  private static int[] nastyArrayAdd(int[] a, int[] b) {
    int[] r = new int[a.length + b.length];
    System.arraycopy(a, 0, r, 0, a.length);
    System.arraycopy(b, 0, r, a.length, b.length);
    return r;
  }

  private SemiMultiplexedArtNet(int[] demuxed, int[] muxed, int universe) {
    super(
        SemiMultiplexedArtNet.nastyArrayAdd(demuxed, muxed),
        demuxed.length + muxed.length,
        universe);
    this.multiplexed = demuxed;
    this.notMultiplexed = muxed;
  }

  @Override
  public void onSend(int[] colors) {
    // colors[] and this.multiplexed are 1:1 shape wise
    // Here we map colors[0] -> "R", colors[1] -> "G", colors[2] -> "B"

    super.onSend(colors);
    int offset = ARTNET_HEADER_LENGTH + multiplexed.length;

    for (int ptIndex : this.notMultiplexed) {
      this.buffer[offset] = LXColor.blue(colors[ptIndex]);
      ++offset;

      this.buffer[offset] = LXColor.green(colors[ptIndex]);
      ++offset;

      this.buffer[offset] = LXColor.red(colors[ptIndex]);
      ++offset;
    }
  }
}
