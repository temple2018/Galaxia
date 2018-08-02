package org.templegalaxia.patterns.alanpersak;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.BoundedParameter;

import java.util.Random;
import java.util.stream.IntStream;
import org.templegalaxia.model.Petal;
import org.templegalaxia.patterns.TemplePattern;
import processing.core.PApplet;


@LXCategory("Alan Persak")
public class MessagePattern extends TemplePattern {

    private final double BITS_PER_SEC = .2;
    private final double PIXELS_PER_BIT = 2;
    private final double REST_PERIOD = 10;
    private final double RAMP = .5;

    private Random random;

    private String[] messages = {
            "U",
            "The true delight is in the finding out rather than in the knowing.",
            "People who think they know everything are a great annoyance to those of us who do.",
            "Life is pleasant. Death is peaceful. It's the transition that's troublesome",
    };

    private class PetalController {
        public final int petalIndex;
        public String message;
        public double time;
        public byte[] bits;
        public int numBits;


        public PetalController(int thePetalIndex) {
            petalIndex = thePetalIndex;
            message = "";
            time = 0;
        }

        public void reset(String newMessage){
            message = newMessage;
            bits = MessageUtil.toBitArray(message);
            numBits = bits.length;
            time = 0;

            System.out.println("reset petal " + petalIndex + ": " + message + " " + MessageUtil.bitArrayToString(bits));
        }

        public boolean done() {
            double maxTime = numBits / BITS_PER_SEC + BITS_PER_SEC * PIXELS_PER_BIT * 20 + REST_PERIOD;
            return time > maxTime;
        }

        public void run(double deltaMs) {
            System.out.println("run " + deltaMs);
            time += deltaMs / 1000;
            Petal petal = model.petals.get(petalIndex);
            for (int pixelIndex = 0; pixelIndex < petal.numPixels; pixelIndex++) {
                double bitIndex = -time * BITS_PER_SEC * PIXELS_PER_BIT + pixelIndex / PIXELS_PER_BIT + numBits;
                double bitValue = MessageUtil.interpolateBit(bitIndex, bits);

                //int pointIndex = petalIndexToPointIndex(petal.numPixels - pixelIndex -1, petalIndex);
                int pointIndex = petalIndexToPointIndex(pixelIndex, petalIndex);
                colors[pointIndex] = LXColor.hsb(0, 0, 100 * bitValue);
            }
        }
    }

    private PetalController[] petalControllers;

    public MessagePattern(LX lx) {
        super(lx);
        random = new Random();
        petalControllers = new PetalController[model.petals.size()];

        for (int petalIndex = 0; petalIndex < model.petals.size(); petalIndex++){
            petalControllers[petalIndex] = new PetalController(petalIndex);
            //int messageIndex = random.nextInt(messages.length);
            int messageIndex = petalIndex % messages.length;
            petalControllers[petalIndex].reset(messages[messageIndex]);
        }
    }

    public void run(double deltaMs) {
        // Turn everything off
        for (LXPoint p : model.points) {
            colors[p.index] = LXColor.BLACK;
        }

        for (int petalIndex = 0; petalIndex < model.petals.size(); petalIndex++){
            PetalController petalController = petalControllers[petalIndex];
            if(petalController.done()){
                int messageIndex = random.nextInt(messages.length);
                petalController.reset(messages[messageIndex]);
            }
            petalController.run(deltaMs);

        }
    }
}
