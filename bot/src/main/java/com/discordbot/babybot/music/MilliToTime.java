package com.discordbot.babybot.music;

import java.util.concurrent.TimeUnit;

public class MilliToTime {
    public static String convert(long milliseconds) {
        return String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
    }
}
