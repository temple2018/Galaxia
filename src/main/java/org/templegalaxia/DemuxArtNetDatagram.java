package org.templegalaxia;

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
        private boolean sequenceEnabled;
        private byte sequence;

        public DemuxArtNetDatagram(int[] var1) {
            this((int[])var1, 0);
        }

        public DemuxArtNetDatagram(LXFixture var1, int var2) {
            this(fixtureToIndices(var1), var2);
        }

        public DemuxArtNetDatagram(int[] var1, int var2) {
            this(var1, 3 * var1.length, var2);
        }

        public DemuxArtNetDatagram(LXFixture var1, int var2, int var3) {
            this(fixtureToIndices(var1), var2, var3);
        }

        // Stolen from LXOutput
        static int[] fixtureToIndices(LXFixture var0) {
            List var1 = var0.getPoints();
            int[] var2 = new int[var1.size()];
            int var3 = 0;

            LXPoint var5;
            for(Iterator var4 = var1.iterator(); var4.hasNext(); var2[var3++] = var5.index) {
                var5 = (LXPoint)var4.next();
            }

            return var2;
        }


        public DemuxArtNetDatagram(int[] var1, int var2, int var3) {
                super(18 + var2 + var2 % 2);
                this.sequenceEnabled = false;
                this.sequence = 1;
                this.pointIndices = var1;
                this.setPort(6454);
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
                this.buffer[14] = (byte)(var3 & 255);
                this.buffer[15] = (byte)(var3 >>> 8 & 255);
                this.buffer[16] = (byte)(var2 >>> 8 & 255);
                this.buffer[17] = (byte)(var2 & 255);

                for(int var4 = 18; var4 < this.buffer.length; ++var4) {
                    this.buffer[var4] = 0;
                }

            }

        public heronarts.lx.output.ArtNetDatagram setSequenceEnabled(boolean var1) {
            this.sequenceEnabled = var1;
            return this;
        }

        public void onSend(int[] var1) {
            this.copyPoints(var1, this.pointIndices, 18);
            if (this.sequenceEnabled) {
                if (++this.sequence == 0) {
                    ++this.sequence;
                }

                this.buffer[12] = this.sequence;
            }

        }
    }
}
