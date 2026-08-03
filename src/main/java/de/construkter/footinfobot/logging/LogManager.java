package de.construkter.footinfobot.logging;

import de.construkter.footinfobot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;

import java.util.HashMap;

public class LogManager {
    public static void log(final String title, final String message,
                           final HashMap<String, String> arguments) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(message).append(" | ");

        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message + "\n\n"
                + "**Additional Information:**");

        for (String key : arguments.keySet()) {
            String value = arguments.get(key);
            if (value != null) {
                embedBuilder.addField(key, value, false);
                stringBuilder.append(key).append(": ").append(value).append(" // ");
            }
        }

        TextChannel log = Main.jda.getTextChannelById(Main.CONFIG.get("mainLog"));

        if (log != null) {
            log.sendMessageEmbeds(embedBuilder.build()).queue();
        }

        LoggingFileHandler.log(title, stringBuilder.toString());
    }

    public static void log(final String title, final String message,
                           final HashMap<String, String> arguments, final Logger logger) {
        StringBuilder logMessage = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message + "\n\n"
                + "**Additional Information:**");
        stringBuilder.append(message).append(" | ");

        logMessage.append(message);

        for (String key : arguments.keySet()) {
            String value = arguments.get(key);
            if (value != null) {
                embedBuilder.addField(key, value, false);
                logMessage.append(" ").append(key).append(": ").append(value).append(" // ");
                stringBuilder.append(key).append(": ").append(value).append(" // ");
            }
        }

        TextChannel log = Main.jda.getTextChannelById(Main.CONFIG.get("mainLog"));

        if (log != null) {
            log.sendMessageEmbeds(embedBuilder.build()).queue();
        }

        logger.info(logMessage.toString());
        LoggingFileHandler.log(title, stringBuilder.toString());
    }
}
