package org.templegalaxia.datagrams;

import heronarts.lx.LX;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXFixture;
import heronarts.lx.model.LXModel;
import heronarts.lx.output.ArtNetDatagram;
import heronarts.lx.output.LXDatagramOutput;

import java.net.SocketException;
import java.net.UnknownHostException;

public class MultiplexedArtNet extends ArtNetDatagram {
  private static final int ARTNET_HEADER_LENGTH = 18;
  private final int[] pointIndices;

  public static MultiplexedArtNet fromFixture(LXFixture fixture, int universe) {
    return new MultiplexedArtNet(LXFixture.Utils.getIndices(fixture), universe);
  }

  public MultiplexedArtNet(int[] pointIndices, int bufferSize, int universe) {
    super(pointIndices, bufferSize, universe);
    this.pointIndices = pointIndices;
  }

  public MultiplexedArtNet(int[] pointIndices, int universe) {
    super(pointIndices, pointIndices.length, universe);
    this.pointIndices = pointIndices;
  }

  public byte luminance(int rgb) {
    int r = (rgb >> LXColor.R_SHIFT) & 0xFF;
    int g = (rgb >> LXColor.G_SHIFT) & 0xFF;
    int b = (rgb) & LXColor.B_MASK;

    // Average and invert it
    return (byte) (255 - ((r + g + b) / 3));
  }

  @Override
  public void onSend(int[] colors) {
    // colors[] and this.pointIndices are 1:1 shape wise
    // Here we map colors[0] -> "R", colors[1] -> "G", colors[2] -> "B"

    int offset = 0;
    for (int ptIndex : this.pointIndices) {
      this.buffer[ARTNET_HEADER_LENGTH + offset] = luminance(colors[ptIndex]);
      ++offset;
    }
  }

  public static void addDatagramForFixture(LX lx, LXModel model, String ipAddress, int universe) {
    try {
      LXDatagramOutput output = new LXDatagramOutput(lx);
      MultiplexedArtNet datagram = MultiplexedArtNet.fromFixture(model, universe);
      datagram.setAddress(ipAddress);
      output.addDatagram(datagram);
      lx.addOutput(output);
    } catch (UnknownHostException | SocketException e) {
      throw new RuntimeException(e);
    }
  }
}
