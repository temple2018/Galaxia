package org.templegalaxia;

import heronarts.lx.LX;
import org.templegalaxia.patterns.acjs.Spoken;
import org.templegalaxia.patterns.alanpersak.MessagePattern;
import org.templegalaxia.patterns.alanpersak.StarPattern;
import org.templegalaxia.patterns.alanpersak.WavePattern;
import org.templegalaxia.patterns.cbarnes.RingFall;
import org.templegalaxia.patterns.gerald.PetalChase;
import org.templegalaxia.patterns.matty.DebugOrder;
import org.templegalaxia.patterns.matty.Sparkle;
import org.templegalaxia.patterns.matty.Teleport;
import org.templegalaxia.patterns.mcslee.*;
import org.templegalaxia.patterns.pandores.Drahciug;
import org.templegalaxia.patterns.pandores.EiffelTower;
import org.templegalaxia.patterns.pandores.SpaceshipLoading;
import org.templegalaxia.patterns.ping.Swirl;
import org.templegalaxia.patterns.ted.RingTest;
import org.templegalaxia.patterns.ted.Sines;
import org.templegalaxia.patterns.testing.*;

public class GalaxiaUtils {
  private GalaxiaUtils() {
    // Static utility only
  }

  public static void registerPatterns(LX lx) {
    // NOTE(meawoppl) this wants to be a classpath scan for annotations.
    // NOTE(mcslee) but hey, Burning Man is this week! :)

    lx.registerPattern(MoveXPosition.class);
    lx.registerPattern(MoveYPosition.class);
    lx.registerPattern(MoveZPosition.class);
    lx.registerPattern(PetalIterator.class);
    lx.registerPattern(RingIterator.class);
    lx.registerPattern(Teleport.class);
    lx.registerPattern(PetalChase.class);
    lx.registerPattern(Drahciug.class);
    lx.registerPattern(EiffelTower.class);
    lx.registerPattern(SpaceshipLoading.class);
    lx.registerPattern(Sparkle.class);
    lx.registerPattern(DebugOrder.class);
    lx.registerPattern(Swirl.class);
    lx.registerPattern(RingTest.class);
    lx.registerPattern(Sines.class);

    lx.registerPattern(Crawlers.class);
    lx.registerPattern(Synchronicity.class);
    lx.registerPattern(Clouds.class);
    lx.registerPattern(Lava.class);
    lx.registerPattern(Spoken.class);
    lx.registerPattern(RingFall.class);

    // Alan Persak's patterns:
    lx.registerPattern(MessagePattern.class);
    lx.registerPattern(StarPattern.class);
    lx.registerPattern(WavePattern.class);

  }
}
