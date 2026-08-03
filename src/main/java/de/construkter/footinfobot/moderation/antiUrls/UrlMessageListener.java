package de.construkter.footinfobot.moderation.antiUrls;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;

public class UrlMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (AntiUrlManager.stringHasLink(event.getMessage().getContentRaw())) {
            event.getMessage().delete().queue();

            if (event.getMember() == null) {
                return;
            }

            event.getMember().getUser().openPrivateChannel().queue(channel -> {
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("\uD83E\uDD16 Automod")
                        .setDescription("Deine Nachricht wurde gelöscht, da sie einen Link enthält." +
                                " Bitte poste keine Links in diesem Server.")
                        .addField("Nachricht", event.getMessage().getContentRaw(), false)
                        .setColor(Color.RED)
                        .setFooter("FootInfoBot Automod - punished at")
                        .setTimestamp(Instant.now());

                channel.sendMessageEmbeds(builder.build()).queue();
            });
        }
    }
}
