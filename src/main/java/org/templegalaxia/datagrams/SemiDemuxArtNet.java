package org.templegalaxia.datagrams;

import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXFixture;
import heronarts.lx.output.ArtNetDatagram;

public class SemiDemuxArtNet extends ArtNetDatagram {
    private static final int ARTNET_HEADER_LENGTH = 18;
    private final int[] demuxed;
    private final int[] muxed;

    public static SemiDemuxArtNet fromFixture(LXFixture demuxed, LXFixture muxed, int universe) {
        return new SemiDemuxArtNet(
                LXFixture.Utils.getIndices(demuxed),
                LXFixture.Utils.getIndices(muxed),
                universe);
    }

    private int[] nastyArrayAdd(int[] a, int[] b){
        int[] r = new int[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }

    private SemiDemuxArtNet(int[] demuxed, int[] muxed, int universe) {
        super(
            this.nastyArrayAdd(demuxed, muxed),
            demuxed.length + muxed.length,
            universe);
        this.demuxed = demuxed;
        this.muxed = muxed;
    }

    public byte luminance(int rgb) {
        int r = (rgb >> LXColor.R_SHIFT) & 0xFF;
        int g = (rgb >> LXColor.G_SHIFT) & 0xFF;
        int b = (rgb) & LXColor.B_MASK;

        return (byte) ((r + g + b) / 3);
    }

    @Override
    public void onSend(int[] colors) {
        // colors[] and this.demuxed are 1:1 shape wise
        // Here we map colors[0] -> "R", colors[1] -> "G", colors[2] -> "B"

        int offset = ARTNET_HEADER_LENGTH;
        for (int ptIndex : this.demuxed) {
            this.buffer[offset] = luminance(colors[ptIndex]);
            ++offset;
        }

        for (int ptIndex : this.muxed) {
            this.buffer[offset] = LXColor.blue(colors[ptIndex]);
            ++offset;

            this.buffer[offset] = LXColor.green(colors[ptIndex]);
            ++offset;

            this.buffer[offset] = LXColor.red(colors[ptIndex]);
            ++offset;
        }
    }
}
