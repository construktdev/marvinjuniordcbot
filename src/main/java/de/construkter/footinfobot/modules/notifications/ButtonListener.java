package de.construkter.footinfobot.modules.notifications;

import de.construkter.footinfobot.Main;
import de.construkter.footinfobot.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class ButtonListener extends ListenerAdapter {

    private final Config config = Main.CONFIG;

    @Override
    public void onButtonInteraction(final ButtonInteractionEvent event) {
        if (!event.getComponentId().equalsIgnoreCase("notifications")) {
            return;
        }
        // Only reply to the notifications change button

        Member member = event.getMember();
        Role streamRole = event.getJDA().getRoleById(config.get("streamRole"));
        Guild guild = event.getGuild();

        if (streamRole == null) {
            event.reply("❌ Es gab einen Fehler!").setEphemeral(true).queue();
            return;
        }

        if (member == null) {
            return;
        }

        if (guild == null) {
            return;
        } // Don't need feedback cuz the button can only be in a guild (SendPanel.java)

        if (member.getRoles().contains(streamRole)) {
            guild.removeRoleFromMember(member, streamRole).queue();
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("✅ Benachrichtigungen")
                    .setDescription("Du erhältst nun keine Benachrichtigungen wenn Marvin live geht!")
                    .setColor(Color.RED)
                    .setFooter("FootInfoBot", guild.getIconUrl());

            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        } else {
            guild.addRoleToMember(member, streamRole).queue();
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("✅ Benachrichtigungen")
                    .setDescription("Du erhältst nun Benachrichtigungen wenn Marvin live geht!")
                    .setColor(Color.GREEN)
                    .setFooter("FootInfoBot", guild.getIconUrl());

            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        }
    }
}
