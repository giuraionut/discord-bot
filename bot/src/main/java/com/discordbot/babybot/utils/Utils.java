package com.discordbot.babybot.utils;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {
    public static String milliToTime(long milliseconds) {
        return String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
    }
    public static Color randomColor()
    {
        return new Color((int) (new Random().nextDouble() * 16777216)); //random number * 24 bit  true color (2^24) colors
    }

}
