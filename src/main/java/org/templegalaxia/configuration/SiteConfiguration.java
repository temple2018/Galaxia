package org.templegalaxia.configuration;

import java.util.HashMap;
import java.util.Map;

public class SiteConfiguration {
  public static final String LED_0_IP = "192.168.1.20";
  // public static final String LED_1_IP = "192.168.1.21";
  public static final String LED_1_IP = "10.0.0.1";

  public static final String[] IPS = new String[] {LED_0_IP, LED_1_IP};

  public static final int UNMAPPED = 23456;
  public static Map<Integer, String> getPetalToIPAddress() {
    Map<Integer, String> petalIPMap = new HashMap<>();
    petalIPMap.put(0, LED_1_IP);

    petalIPMap.put(1, LED_0_IP);
    petalIPMap.put(2, LED_0_IP);
    petalIPMap.put(3, LED_0_IP);
    petalIPMap.put(4, LED_0_IP);
    petalIPMap.put(5, LED_0_IP);
    petalIPMap.put(6, LED_0_IP);
    petalIPMap.put(7, LED_0_IP);
    petalIPMap.put(8, LED_0_IP);
    petalIPMap.put(9, LED_0_IP);
    petalIPMap.put(10, LED_0_IP);

    petalIPMap.put(11, LED_1_IP);
    petalIPMap.put(12, LED_1_IP);
    petalIPMap.put(13, LED_1_IP);
    petalIPMap.put(14, LED_1_IP);
    petalIPMap.put(15, LED_1_IP);
    petalIPMap.put(16, LED_1_IP);
    petalIPMap.put(17, LED_1_IP);
    petalIPMap.put(18, LED_1_IP);
    petalIPMap.put(19, LED_1_IP);

    return  petalIPMap;
  }

  // Map from Integer Petal to Integer Universe for the upper strings
  public static Map<Integer, Integer> getUpperConfigs(){
    final Map<Integer, Integer> outputMap = new HashMap<>();
    outputMap.put(0, 18);
    outputMap.put(1, 2);
    outputMap.put(2, 1);
    outputMap.put(3, 4);
    outputMap.put(4, 6);
    outputMap.put(5, 8);
    outputMap.put(6, 11);
    outputMap.put(7, 14);
    outputMap.put(8, 12);
    outputMap.put(9, 17);
    outputMap.put(10, UNMAPPED );
    outputMap.put(11, 2 );
    outputMap.put(12, UNMAPPED );
    outputMap.put(13, UNMAPPED );
    outputMap.put(14, UNMAPPED );
    outputMap.put(15, 8 );
    outputMap.put(16, 10 );
    outputMap.put(17, 12 );
    outputMap.put(18, 14 );
    outputMap.put(19, 16 );

    return outputMap;
  }


  // Map from Integer Petal to Integer Universe for the lower strings
  public static Map<Integer, Integer> getLowerConfigs(){
    final Map<Integer, Integer> outputMap = new HashMap<>();
    outputMap.put(0, 19);
    outputMap.put(1, 3);
    outputMap.put(2, UNMAPPED );
    outputMap.put(3, UNMAPPED );
    outputMap.put(4, 7);
    outputMap.put(5, 9);
    outputMap.put(6, 10);
    outputMap.put(7, 15);
    outputMap.put(8, 13);
    outputMap.put(9, 16);
    outputMap.put(10, 19);
    outputMap.put(11, 3 );
    outputMap.put(12, 1 );
    outputMap.put(13, UNMAPPED );
    outputMap.put(14, UNMAPPED );
    outputMap.put(15, 9 );
    outputMap.put(16, 11 );
    outputMap.put(17, 13 );
    outputMap.put(18, 15 );
    outputMap.put(19, 17 );

    return outputMap;
  }


}
