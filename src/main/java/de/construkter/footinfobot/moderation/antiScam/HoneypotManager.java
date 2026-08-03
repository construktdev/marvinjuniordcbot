package de.construkter.footinfobot.moderation.antiScam;

import java.awt.Color;
import java.util.List;

import de.construkter.footinfobot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HoneypotManager extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(final SlashCommandInteractionEvent event) {
        TextChannel honeypotChannel = Main.jda.getTextChannelById(Main.CONFIG.get("honeypotChannel"));

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Achtung");
        embedBuilder.setDescription("""
                Bitte schreibe in diesem Channel nix!
                
                Dieser Channel wird nur genutzt um spam bots aufzuhalten und zu bannen.
                
                Wenn du hier etwas schreibst, wirst du gebannt.""");
        embedBuilder.setFooter("FootInfoBot");
        embedBuilder.setColor(Color.RED);

        if (honeypotChannel == null) {
            event.reply("Honeypot Channel not found").setEphemeral(true).queue();
            return;
        }

        honeypotChannel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (!Main.messageCache.containsKey(event.getAuthor())) {
            Main.messageCache.put(event.getAuthor(), List.of(event.getMessage()));
        } else {
            List<Message> messages = Main.messageCache.get(event.getAuthor());
            messages.add(event.getMessage());
            Main.messageCache.put(event.getAuthor(), messages);
        }
    }
}
