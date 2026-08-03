package de.construkter.footinfobot.moderation.antiScam;

import de.construkter.footinfobot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.MessageTopLevelComponent;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class HoneypotMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (event.getChannel().asTextChannel()
                != event.getGuild().getTextChannelById(Main.CONFIG.get("honeypotChannel")) ||
                event.getMember() == null || event.getMember().getUser().isBot()) {
            return;
        }

        Member target = event.getMember();

        target.ban(7, TimeUnit.DAYS).queue();

        event.getGuild().unban(target.getUser()).queue();

        target.getUser().openPrivateChannel().queue(channel -> {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("\uD83E\uDD16 Automod")
                    .setDescription("""
                            Du wurdest gebannt, da du in dem Honeypot Channel geschrieben hast.
                            Bitte schreibe nicht in diesem Channel.
                            
                            Wenn dir dies ausversehen passiert ist, kannst du über den Button unten,
                            ein Entbannungsantrag stellen.
                            Wir halten uns allerdings vor diesen abzulehnen. Dies wird anhand der gesendeten Nachricht
                            entschieden.
                            """)
                    .setColor(0xFF0000)
                    .setFooter("FootInfoBot Automod - punished at")
                    .setTimestamp(java.time.Instant.now());

            Button unban = Button.primary("unban", "Entbannungsantrag");

            channel.sendMessageEmbeds(builder.build())
                    .addComponents((Collection<? extends MessageTopLevelComponent>) unban)
                    .queue();
        });
    }
}
