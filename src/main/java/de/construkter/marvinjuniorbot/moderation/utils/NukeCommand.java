package de.construkter.marvinjuniorbot.moderation.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NukeCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(final SlashCommandInteractionEvent event) {
        if (event.getMember() != null) {
            return;
        }

        if (!event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            event.reply("Du hast keine Rechte diesen Befehl zu nutzen").setEphemeral(true).queue();
            return;
        }

        event.getChannel().asTextChannel().createCopy().queue(
                 success -> {
                     success.sendMessage("💥 Dieser Kanal wurde genuked!").queue();
                     event.getChannel().delete().queue();
                 },
                error -> event.reply(error.getMessage()).setEphemeral(true).queue()
        );
    }
}
