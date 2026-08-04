package de.construkter.footinfobot.modules.versionCheck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VersionFetcher {
    public static String getRecentVersion() {
        URI uri = URI.create("https://api.construkter.de/projects/footinfobot/version/");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(uri.toURL().openStream(), StandardCharsets.UTF_8))) {

            String firstLine = reader.readLine();
            return firstLine;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
