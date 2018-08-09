package org.templegalaxia.patterns.acjs;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.LXLayer;
import heronarts.lx.color.LXColor;
import heronarts.lx.modulator.LXPeriodicModulator;
import heronarts.lx.modulator.SinLFO;
import heronarts.lx.parameter.CompoundParameter;
import heronarts.lx.parameter.LXParameter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.templegalaxia.patterns.TemplePattern;

import heronarts.lx.studio.LXStudio;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.templegalaxia.patterns.TemplePattern;
import processing.core.PApplet;

/*
 * “Do you not know that a man is not dead while his name is still spoken?”
 *         -- Terry Pratchett, Going Postal
 */

@LXCategory("Acjs")
public class Spoken extends TemplePattern {
  private static final String NAME_DATA = "assets/Spoken.txt";
  private static final int DASH_MULTIPLIER = 3;

  public final CompoundParameter density =
      new CompoundParameter("Lights", 1.0, 0, colors.length)
          .setDescription("Number of lights to use (density).");

  public final CompoundParameter birthRate =
      new CompoundParameter("Rate", 1.0, 0, 1)
          .setDescription("How likely a new name is to appear (per second).");

  public final CompoundParameter dot =
      (CompoundParameter)
          new CompoundParameter("Dot speed", 1000, 0, 10000)
              .setUnits(LXParameter.Units.MILLISECONDS)
              .setDescription("Length of a 'dot' in morse code.");

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

  public Spoken(LX lx) {
    super(lx);

    addParameter(density);
    addParameter(birthRate);
    addParameter(dot);
    addParameter(dotVariance);

    loadMorse();

    for (int i = 0; i < colors.length; i++) availableLights.add(i);
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

  void loadNames() {
    // Load names using Processing's mechanism to do so
    // Not super happy with the cast but I can't find a better way to get access to
    // the PApplet and we can't access the method statically.
    String[] newNames = ((LXStudio)lx).applet.loadStrings(NAME_DATA);

    if (null == newNames) {
      // We failed to load our name data
      names.add("Larry Harvey");
    } else {
      names.addAll(Arrays.asList(newNames));
    }

    Collections.shuffle(names);
  }

  public void newPulser(String message) {
    double dotLength = dot.getValue();
    dotLength += (rand.nextDouble() - 0.5) * dotVariance.getValue();

    int light = availableLights.remove(rand.nextInt(availableLights.size()));

    MorsePulser pulser = new MorsePulser(lx, light, message, dotLength);
    pulsers.add(pulser);
    addLayer(pulser);
  }

  public void run(double deltaMs) {
    // Give each unlit 'slot' a chance to be born.
    for (int i = 0; i < (int) density.getValue() - pulsers.size(); i++) {
      if (names.size() == 0) loadNames(); // Reload names if we're out. XXX: Performance?

      if (rand.nextDouble() < birthRate.getValue() * deltaMs / 1000) {
        newPulser(names.remove(0));
      }
    }

    // Garbage collect completed pulsers
    // TODO: Do this with a callback on finish or something.
    for (Iterator<MorsePulser> iterator = pulsers.iterator(); iterator.hasNext(); ) {
      MorsePulser pulse = iterator.next();
      if (pulse.isFinished()) {
        availableLights.add(pulse.getLightIndex());
        removeLayer(pulse);
        iterator.remove();
      }
    }
  }

  public class MorsePulser extends LXLayer {
    private final int lightIndex;
    private final String message;
    private final String morsePattern;

    private final LXPeriodicModulator dotModulator;
    private final LXPeriodicModulator dashModulator;

    private int currentCharacter = 0;
    private boolean finished = false;

    public MorsePulser(LX lx, int lightIndex, String message, double dotLength) {
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

      dotModulator = new SinLFO(0, 100, dotLength).setLooping(false);
      dashModulator = new SinLFO(0, 100, dotLength * DASH_MULTIPLIER).setLooping(false);

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
