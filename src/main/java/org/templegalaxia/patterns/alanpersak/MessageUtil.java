package org.templegalaxia.patterns.alanpersak;

public class MessageUtil {
    /**
     * Converts a string into a sequence of bits, returning an array of bytes where each bytes holds a 0 or 1.
     */
    public static byte[] toBitArray(String str){
        byte[] bits = new byte[str.length()*8];
        for (int i=0; i< str.length(); i++){
            char c = str.charAt(i);
            for (int j=0; j<8; j++){
                bits[i*8+j] = (c & (1<<j)) > 0 ? (byte)1 : (byte)0;
            }
        }
        return bits;
    }

    public static String bitArrayToString(byte[] bits){
        StringBuilder builder = new StringBuilder();
        for(byte bit : bits){
            builder.append(bit == 0 ? '0' : '1');
        }
        return builder.toString();
    }

    public static double interpolateBit(double bitIndex, byte[] bits){
        int numBits = bits.length;
        if (bitIndex <= -1 || bitIndex >= numBits) {
            return 0;
        }
        if (bitIndex <= 0){
            return (1+bitIndex) * bits[0];
        }
        if (bitIndex >= numBits - 1) {
            return (numBits - bitIndex) * bits[numBits-1];
        }

        // linear interpolation between two points
        double x = bitIndex;
        int x0 = (int)Math.floor(x);
        byte y0 = bits[x0];
        int x1 = (int)Math.ceil(x);
        byte y1 = bits[x1];
        double y = (y0*(x1-x)+y1*(x-x0))/(x1-x0);
        return y;
    }
}
