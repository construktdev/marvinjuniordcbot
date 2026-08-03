package de.construkter.footinfobot.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class LoggingFileHandler {
    private static final String BASE_DIR = "logs/";

    public static void log(final String title, final String message) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String fileName = BASE_DIR + "log_" + today.format(dateFormatter) + ".txt";
        StringBuilder logStringBuilder = new StringBuilder();

        logStringBuilder.append("[").append(LocalDate.now().format(timeFormatter)).append("] ")
                .append(title).append(": ").append(message);

        Path path = Paths.get(fileName);

        try {
            Files.writeString(path, logStringBuilder.toString(), StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            LogManager.log("Logging Error", "Failed to write log to file: " + e.getMessage(), new HashMap<>());
        }
    }
}
