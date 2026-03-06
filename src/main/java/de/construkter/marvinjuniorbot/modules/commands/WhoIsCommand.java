package de.construkter.marvinjuniorbot.modules.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class WhoIsCommand extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        var target = event.getOption("user");

        if (target == null) {
            event.reply("Usage: /whois <user>").setEphemeral(true).queue();
            return;
        }

        User user = target.getAsUser();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("\uD83E\uDDD1 WhoIs Lookup");
        builder.setThumbnail(user.getAvatarUrl());
        builder.setColor(Color.GREEN);
        builder.addField("Name", user.getName(), false);
        builder.addField("Effective Name", user.getEffectiveName(), false);
        builder.addField("Mention", user.getAsTag(), false);
        builder.addField("User ID", user.getId(), false);
        builder.addField("Erstellt", user.getTimeCreated().format(DateTimeFormatter.ofPattern("dd:MM:yyyy HH:mm")), false);
        builder.addField("Ist Bot", String.valueOf(user.isBot()), false);
        builder.addField("User Flags", user.getFlags().toString(), false);
        builder.setTimestamp(Instant.now());
        builder.setFooter("TheMarvinJunior", event.getJDA().getSelfUser().getAvatarUrl());

        event.replyEmbeds(builder.build()).queue();
    }
}
