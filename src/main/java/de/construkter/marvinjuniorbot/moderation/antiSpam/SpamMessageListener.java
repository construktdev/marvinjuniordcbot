package de.construkter.marvinjuniorbot.moderation.antiSpam;

import de.construkter.marvinjuniorbot.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class SpamMessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull final MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || (event.getMember() != null &&
                event.getMember().hasPermission(Permission.ADMINISTRATOR))) {
            return;
        }

        Main.spamManager.handleMessage(event);
    }
}
