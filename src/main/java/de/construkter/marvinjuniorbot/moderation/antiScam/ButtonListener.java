package de.construkter.marvinjuniorbot.moderation.antiScam;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;

public class ButtonListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(final ButtonInteractionEvent event) {
        if (event.getMember() == null || event.getGuild() != null) {
            return;
        }

        EmbedBuilder reply = new EmbedBuilder()
                .setTitle("✅ Erfolgreich")
                .setDescription("""
                        Du hast deinen Entbannungsantrag erfolgreich abgeschickt.
                        
                        Bitte habe etwas Geduld bis wir ihn bearbeiten!""")
                .setFooter("MarvinJuniorBot")
                .setTimestamp(Instant.now());
    }
}
