package org.templegalaxia.patterns.alanpersak;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BoundedParameter;

import java.util.Random;
import org.templegalaxia.patterns.TemplePattern;

/**
 * An animation that converts a series of messages into binary waveforms that travel up the petals. If the pattern is
 * accepted I would like to invite people to submit messages to drive the animation. It would be a nice way to leave a
 * temple offering, especially if they can't make it in person.
 */
@LXCategory("Alan Persak")
public class MessagePattern extends TemplePattern {

    private BoundedParameter rateParam = new BoundedParameter("Rate", .2, .1, 10).setDescription("Rate of transmission in bits per second.");
    private BoundedParameter sizeParam = new BoundedParameter("Size", 2, 1, 10).setDescription("Size of each bit, in pixels.");
    // Top is not yet implemented. It might be added later but it seems unnecessary for now.
    //private BoundedParameter topParam = new BoundedParameter("Top", .2, .1, 10).setDescription("Size of the top of each bit in the waveform. At zero, the shape is a triangle. At size, it is a square wave. In between, a trapezoid.");
    private BoundedParameter restParam = new BoundedParameter("Rest", 10, 0, 60).setDescription("Period between message transmission.");
    private BoundedParameter minParam = new BoundedParameter("Min", 0, 0, 100).setDescription("Brightness of a zero.");
    private BoundedParameter maxParam = new BoundedParameter("Max", 100, 0, 100).setDescription("Brightness of a one.");

    private double rate;
    private double size;
    //private double top;
    private double rest;
    private double minBrightness;
    private double maxBrightness;
    private Random random;

    // The list of messages that are transmitted. This is placeholder for real messages that will come later. Perhaps
    // they should be read from a file, or input the UI somehow.
    private String[] messages = {
            //"U", // 01010101 used for debugging.
            "Isaac Asimov",
            "Ursula K. Le Guin",
            "Arthur Clark",
            "Philip K. Dick",
            "Robert A. Heinlein",
            "Jules Verne",
            "Mary Shelley",
    };

    private PetalController[] petalControllers;

    public MessagePattern(LX lx) {
        super(lx);

        addParameter(rateParam);
        addParameter(sizeParam);
        //addParameter(topParam);
        addParameter(restParam);
        addParameter(minParam);
        addParameter(maxParam);

        random = new Random();
        petalControllers = new PetalController[model.petals.size()];

        // Initialize message controllers with random messages.
        for (int petalIndex = 0; petalIndex < model.petals.size(); petalIndex++){
            int messageIndex = 0;//random.nextInt(messages.length);
            petalControllers[petalIndex] = new PetalController(petalIndex, messages[messageIndex]);
        }
    }

    public void run(double deltaMs) {
        rate = rateParam.getValue();
        size = sizeParam.getValue();
        //top = topParam.getValue();
        rest = restParam.getValue();
        minBrightness = minParam.getValue();
        maxBrightness = maxParam.getValue();

        // Turn everything off
        for (LXPoint p : model.points) {
            colors[p.index] = LXColor.hsb(0, 0, minBrightness);
        }

        // Reset done controller and animate all controllers.
        for (int petalIndex = 0; petalIndex < model.petals.size(); petalIndex++){
            PetalController petalController = petalControllers[petalIndex];
            if(petalController.done()){
                int messageIndex = random.nextInt(messages.length);
                petalController.reset(messages[messageIndex]);
            }
            petalController.run(deltaMs);
        }
    }

    private class PetalController {
        private final int petalIndex;
        private final int numPixels;

        public double time;
        public byte[] bits;
        public int numBits;

        public PetalController(int petalIndex, String message) {
            this.petalIndex = petalIndex;
            numPixels = model.petals.get(petalIndex).size;
            reset(message);
        }

        public void reset(String message){
            bits = toBitArray(message);
            numBits = bits.length;
            time = 0;
            System.out.println("reset petal " + petalIndex + ": " + message + " " + bitArrayToString(bits));
        }

        public boolean done() {
            return time > numBits / rate + numPixels / (rate * size)  + rest;
        }

        public void run(double deltaMs) {
            time += deltaMs / 1000;
            for (int pixelIndex = 0; pixelIndex < numPixels; pixelIndex++) {
                double position = time * rate - pixelIndex / size;
                double bitValue = interpolateBit(bits, position, size);
                double brightness = minBrightness + bitValue * (maxBrightness - minBrightness);
                int pointIndex = petalIndexToPointIndex(pixelIndex, petalIndex);
                colors[pointIndex] = LXColor.hsb(0, 0, brightness);
            }
        }
    }

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

    /**
     * Converts a bit array to a string of 1's and 0's.
     */
    public static String bitArrayToString(byte[] bits){
        StringBuilder builder = new StringBuilder();
        for(byte bit : bits){
            builder.append(bit == 0 ? '0' : '1');
        }
        return builder.toString();
    }

    /**
     *  Get's the waveform value of a position in a bit array.
     */
    public static double interpolateBit(byte[] bits, double position, double top){
        int numBits = bits.length;
        if (position <= -1 || position >= numBits) {
            return 0;
        }
        if (position <= 0){
            return (1+position) * bits[0];
        }
        if (position >= numBits - 1) {
            return (numBits - position) * bits[numBits-1];
        }

        double x = position;
        int x0 = (int)Math.floor(x);
        byte y0 = bits[x0];
        int x1 = (int)Math.ceil(x);
        byte y1 = bits[x1];
        return Util.linearInterpolate(x, x0, y0, x1, y1);
    }
}
