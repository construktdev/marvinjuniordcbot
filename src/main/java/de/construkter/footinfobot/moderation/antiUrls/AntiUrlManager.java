package de.construkter.footinfobot.moderation.antiUrls;

import java.util.regex.Pattern;

public class AntiUrlManager {
    public static boolean stringHasLink(String string) {
        Pattern pattern = Pattern.compile("((https?|ftp|gopher|telnet|file|Unsure|http|ws|wss)://)" +
                "?([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})(:[0-9]+)?(/\\S*)?");

        String[] parts = string.split("\\s+");

        for (String part : parts) {
            if (pattern.matcher(part).matches()) {
                return true;
            }
        }

        return false;
    }
}
