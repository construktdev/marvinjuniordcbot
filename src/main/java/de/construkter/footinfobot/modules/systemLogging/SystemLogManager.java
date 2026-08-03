package de.construkter.footinfobot.modules.systemLogging;

import de.construkter.footinfobot.Main;
import de.construkter.footinfobot.logging.LoggingFileHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.HashMap;

public class SystemLogManager {

    private final TextChannel systemLogChannel = Main.CONFIG.get("systemLog") != null
            ? Main.jda.getTextChannelById(Main.CONFIG.get("systemLog")) : null;


    public void log(String title, String message, HashMap<String, String> args) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(title)
                .setDescription(message + "\n\n**Additional Information:**");

        for (String key : args.keySet()) {
            String value = args.get(key);
            if (value != null) {
                embedBuilder.addField(key, value, false);
            }
        }

        if (systemLogChannel != null) {
            systemLogChannel.sendMessageEmbeds(embedBuilder.build()).queue();
        } else {
            StringBuilder argsString = new StringBuilder();

            for (String key : args.keySet()) {
                argsString.append(key).append(": ").append(args.get(key)).append(" // ");
            }

            LoggingFileHandler.log("systemLogChannel is null",
                    "LogMessage: " + title + " | " + message + " | args: | " + argsString);
        }
    }
}
