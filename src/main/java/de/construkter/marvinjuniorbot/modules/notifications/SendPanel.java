package de.construkter.marvinjuniorbot.modules.notifications;

import de.construkter.marvinjuniorbot.Main;
import de.construkter.marvinjuniorbot.logging.LogManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.HashMap;

public class SendPanel extends ListenerAdapter {
    Logger LOGGER = LoggerFactory.getLogger(SendPanel.class);

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("notifications")) return; // Only reply to /notifications
        if (event.getGuild() ==  null) return; // Only reply if it's run in a guild
        if (event.getMember() ==  null) return; // Only reply if it's run by a real member

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("Du musst mindestens Admin sein um diesen Befehl zu nutzen").setEphemeral(true).queue();
            return;
        }

        var channel = event.getOption("channel");

        if (channel == null) {
            event.reply("Du musst einen Kanal angeben, wo der Embed gesendet werden soll!").setEphemeral(true).queue();
            return;
        }

        TextChannel target = channel.getAsChannel().asTextChannel();

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("\uD83D\uDCE2 Stream Benachrichtigungen ändern")
                .setColor(Color.YELLOW)
                .setDescription("Nutze den Button unten um zu ändern ob du Benachrichtigt wirst wenn Marvin live geht.")
                .setFooter("TheMarvinJunior", event.getGuild().getIconUrl());

        Button change = Button.primary("notifications", "\uD83D\uDCE2 Ändern");

        target.sendMessageEmbeds(embed.build())
                .setComponents(ActionRow.of(change))
                .queue();

        HashMap<String, String> args = new HashMap<>();
        args.put("User", event.getMember().getAsMention());
        args.put("Time", LocalDateTime.now().toString());
        LogManager.log("Notifications Panel", "Someone sent the notifications panel", args, LoggerFactory.getLogger(SendPanel.class));
    }
}
