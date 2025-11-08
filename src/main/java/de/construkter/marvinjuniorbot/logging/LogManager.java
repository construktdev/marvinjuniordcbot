package de.construkter.marvinjuniorbot.logging;

import de.construkter.marvinjuniorbot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;

import java.util.HashMap;

public class LogManager {
    public static void log(String title, String message, HashMap<String, String> arguments) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message + "\n\n" +
                "**Additional Information:**");

        for (String key : arguments.keySet()) {
            String value = arguments.get(key);
            if (value != null) {
                embedBuilder.addField(key, value, false);
            }
        }

        TextChannel log = Main.jda.getTextChannelById(Main.CONFIG.get("mainLog"));

        if (log != null) {
            log.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }

    public static void log(String title, String message, HashMap<String, String> arguments, Logger logger) {
        StringBuilder logMessage = new StringBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(message + "\n\n" +
                "**Additional Information:**");

        logMessage.append(message);

        for (String key : arguments.keySet()) {
            String value = arguments.get(key);
            if (value != null) {
                embedBuilder.addField(key, value, false);
                logMessage.append(" ").append(key).append(": ").append(value).append(" //");
            }
        }

        TextChannel log = Main.jda.getTextChannelById(Main.CONFIG.get("mainLog"));

        if (log != null) {
            log.sendMessageEmbeds(embedBuilder.build()).queue();
        }

        logger.info(logMessage.toString());
    }
}
