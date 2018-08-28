package org.templegalaxia.patterns.acjs;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.LXLayer;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.modulator.LXPeriodicModulator;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.FunctionalParameter;
import heronarts.lx.parameter.LXParameter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.templegalaxia.patterns.TemplePattern;

/*
 * “Do you not know that a man is not dead while his name is still spoken?”
 *         -- Terry Pratchett, Going Postal
 */

@LXCategory("Acjs")
public class Spoken extends TemplePattern {
  private static final String NAME_DATA = "assets/Spoken.txt";
  private static final int DASH_MULTIPLIER = 3;
  private static final int NAME_RELOAD_INTERVAL = 10 * 60 * 1000; // Ten minutes

  // NOTE XXX BROKEN !!!  Ground lights were scrapped :(
  //
  //  public final CompoundParameter density =
  //      new CompoundParameter("Lights", 1.0, 0, model.groundPoints.size())
  //          .setDescription("Number of lights to use (density).");

  public final CompoundParameter birthRate =
      new CompoundParameter("Rate", 1.0, 0, 1)
          .setDescription("How likely a new name is to appear (per second).");

  public final CompoundParameter dot =
      (CompoundParameter)
          new CompoundParameter("Dot speed", 1000, 0, 10000)
              .setUnits(LXParameter.Units.MILLISECONDS)
              .setDescription("Length of a 'dot' in morse code.");

  public final FunctionalParameter dash = new FunctionalParameter("Dash") {
    @Override
    public double getValue() {
      return dot.getValue() * DASH_MULTIPLIER;
    }
  };

  public final CompoundParameter dotVariance =
      (CompoundParameter)
          new CompoundParameter("Dot variance", 0, 0, 1000)
              .setUnits(LXParameter.Units.MILLISECONDS)
              .setDescription("How variable dot length will be between names.");

  private final HashMap<Character, String> morseLookup = new HashMap<Character, String>();
  private final List<String> names = new ArrayList<String>();
  private final List<MorsePulser> pulsers = new ArrayList<MorsePulser>();
  private final List<Integer> availableLights = new ArrayList<Integer>();

  private final Random rand = new Random();

  private long nameLastLoadTime = 0;
  private List<String> allNames = new ArrayList<String>();

  public Spoken(LX lx) {
    super(lx);

    // MRG BROKEN
    // addParameter(density);
    addParameter(birthRate);
    addParameter(dot);
    addParameter(dotVariance);

    loadMorse();

    // So we have some data for names regardless.
    allNames.add("Larry Harvey");

    // TODO MRG BROKEN
    //    for (LXPoint p : model.groundPoints) {
    //      availableLights.add(p.index);
    //    }
  }

  void loadMorse() {
    // Shamelessly boosted from the model load code.
    CSVParser parser;
    try {
      parser =
          new CSVParser(
              new BufferedReader(
                  new InputStreamReader(Spoken.class.getResourceAsStream("morse.csv"))),
              CSVFormat.EXCEL.withHeader());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    for (CSVRecord csvRecord : parser) {
      morseLookup.put(csvRecord.get("Letter").charAt(0), csvRecord.get("Morse"));
    }
  }

  void updateNames() {
    // Only reload after a set interval has passed, otherwise use the last-loaded name set.
    if (nameLastLoadTime + NAME_RELOAD_INTERVAL < System.currentTimeMillis()) {
      try {
        List<String> newNames = Files.readAllLines(Paths.get(NAME_DATA), Charset.forName("UTF-8"));
        if (null != newNames && newNames.size() > 0) {
          allNames = newNames;
        }

        System.out.println("Reloaded");
        for (String name : allNames) {
          System.out.println(name);
        }

        nameLastLoadTime = System.currentTimeMillis();

      } catch (IOException iox) {
        System.err.println("Error loading names: " + iox.getMessage());
      }
    }

    names.addAll(allNames);
    Collections.shuffle(names);

  }

  public void newPulser(String message) {
    int light = availableLights.remove(rand.nextInt(availableLights.size()));

    MorsePulser pulser = new MorsePulser(lx, light, message);
    pulsers.add(pulser);
    addLayer(pulser);
  }

  public void run(double deltaMs) {
    // Give each unlit 'slot' a chance to be born.

    // TODO MRG BROKEN :(
    //    for (int i = 0; i < (int) density.getValue() - pulsers.size(); i++) {
    //
    //      if (rand.nextDouble() < birthRate.getValue() * deltaMs / 1000) {
    //        if (names.isEmpty()) {
    //          updateNames();
    //        }
    //        newPulser(names.remove(0));
    //      }
    //    }

    // Garbage collect completed pulsers
    // TODO: Do this with a callback on finish or something.
    //    for (Iterator<MorsePulser> iterator = pulsers.iterator(); iterator.hasNext(); ) {
    //      MorsePulser pulse = iterator.next();
    //      if (pulse.isFinished()) {
    //        availableLights.add(pulse.getLightIndex());
    //        removeLayer(pulse);
    //        iterator.remove();
    //      }
    //    }
  }

  public class MorsePulser extends LXLayer {
    private final int lightIndex;
    private final String message;
    private final String morsePattern;

    private final LXPeriodicModulator dotModulator;
    private final LXPeriodicModulator dashModulator;

    private int currentCharacter = 0;
    private boolean finished = false;

    public MorsePulser(LX lx, int lightIndex, String message) {
      super(lx);

      message = message.toUpperCase();

      StringBuffer morse = new StringBuffer();
      for (int i = 0; i < message.length(); i++) {
        if (morseLookup.containsKey(message.charAt(i))) {
          if (i != 0) {
            morse.append(" ");
          }

          morse.append(morseLookup.get(message.charAt(i)));
        }
      }

      this.lightIndex = lightIndex;
      this.message = message;
      this.morsePattern = morse.toString();

      dotModulator = new SinLFO(0, 100, dot).setLooping(false);
      dashModulator = new SinLFO(0, 100, dash).setLooping(false);

      addModulator(dotModulator);
      addModulator(dashModulator);

      currentModulator().trigger();
    }

    LXPeriodicModulator currentModulator() {
      if (morsePattern.charAt(currentCharacter) == '-') return dashModulator;
      else return dotModulator;
    }

    @Override
    public void run(double v) {
      if (finished) return;

      if (currentModulator().finished()) {
        currentCharacter++;

        if (currentCharacter >= morsePattern.length()) {
          finished = true;
          return;
        }

        currentModulator().trigger();
      }

      if (morsePattern.charAt(currentCharacter) == ' ') {
        colors[lightIndex] = LXColor.BLACK;
      } else {
        colors[lightIndex] = LXColor.gray(currentModulator().getValue());
      }
    }

    public boolean isFinished() {
      return finished;
    }

    public int getLightIndex() {
      return lightIndex;
    }
  }
}
