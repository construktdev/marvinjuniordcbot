package de.construkter.marvinjuniorbot.modules.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PurgeCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("purge")) return;
        if (event.getGuild() == null || event.getMember() == null) {
            return;
        }

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("Du musst Admin sein um diesen Befehl auszuführen").setEphemeral(true).queue();
            return;
        }
        var amount = event.getOption("amount");

        if (amount == null) {
            event.reply("Bitte gebe an wie viel du löschen möchtest (2-100)").setEphemeral(true).queue();
            return;
        }

        int messages = amount.getAsInt();

        if (messages < 2 || messages > 100) {
            event.reply("Bitte gebe an wie viel du löschen möchtest (2-100)").setEphemeral(true).queue();
            return;
        }

        TextChannel channel = event.getChannel().asTextChannel();

        channel.getHistory().retrievePast(messages).queue(history -> {
            channel.deleteMessages(history).queue(
                    success -> event.reply("✅ Nachrichten erfolgreich gelöscht").setEphemeral(true).queue(),
                    error -> event.reply(error.getMessage()).setEphemeral(false).queue()
            );
        });
    }
}
