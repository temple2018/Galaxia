package org.templegalaxia.configuration;

import heronarts.lx.LX;
import heronarts.lx.model.LXModel;
import heronarts.lx.output.LXDatagramOutput;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.templegalaxia.DemuxArtNetDatagram;

public class Outputs {

  private LXDatagramOutput output;
  private DemuxArtNetDatagram datagram;

  public Outputs(LX lx, LXModel model) {
    init(lx, model);
  }

  void init(LX lx, LXModel model) {
    try {
      output = new LXDatagramOutput(lx);
      datagram = DemuxArtNetDatagram.fromFixture(model, 0);
      datagram.setAddress("192.168.0.50");
      output.addDatagram(datagram);
      lx.addOutput(output);
    } catch (UnknownHostException | SocketException e) {
      throw new RuntimeException(e);
    }
  }
}
