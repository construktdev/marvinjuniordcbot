package de.construkter.marvinjuniorbot.moderation.antiSpam;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AntiSpamManager {

    HashMap<User, Integer> messagesByUser = new HashMap<>();

    public void handleMessage(final MessageReceivedEvent event) {
        if (!messagesByUser.containsKey(event.getAuthor())) {
            messagesByUser.put(event.getAuthor(), 1);
        } else {
            messagesByUser.replace(event.getAuthor(), messagesByUser.get(event.getAuthor()) + 1);
        }

        if (messagesByUser.get(event.getAuthor()) > 5) {
            if (event.getMember() != null) {
                event.getMember().timeoutUntil(OffsetDateTime.now().plusMinutes(10)).reason("Spam").queue();

                event.getMember().getUser().openPrivateChannel().queue(privateChannel -> {
                    EmbedBuilder builder = new EmbedBuilder()
                            .setTitle("\uD83E\uDD16 Automod")
                            .setDescription("Du wurdest für 10 Minuten gemutet, da du zu viel gespammt hast. " +
                                    "Bitte warte, bis der Mute vorbei ist, bevor du wieder Nachrichten sendest.")
                            .setColor(0xFF0000)
                            .setFooter("MarvinJunior Automod - punished at")
                            .setTimestamp(Instant.now());

                    privateChannel.sendMessageEmbeds(builder.build()).queue();
                });
            }
        }
    }

    public void startScheduler() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> messagesByUser.clear();

        scheduler.scheduleAtFixedRate(task, 10, 10, TimeUnit.SECONDS);
    }
}
