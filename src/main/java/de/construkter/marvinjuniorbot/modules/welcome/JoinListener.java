package de.construkter.marvinjuniorbot.modules.welcome;

import de.construkter.marvinjuniorbot.Main;
import de.construkter.marvinjuniorbot.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.Instant;

public class JoinListener extends ListenerAdapter {

    Config config = Main.CONFIG;
    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        /*
        * Send the JoinMessage
        */
        Guild guild = event.getGuild();
        TextChannel channel = guild.getTextChannelById(config.get("welcomeChannel"));
        if (channel == null) { log.warn("No text channel found"); return; }

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("\uD83D\uDC4B Herzlich Willkommen");
        embed.setDescription(String.format("Hallo %s, \nschön, dass du hierher gefunden hast.\n\nBitte lese dir die <#1431530962990137355> durch und sage gerne im <#1431539789693456384> Hallo!\nAnsonsten wünschen wir dir hier viel Spaß. Forza SGD \uD83D\uDDA4\uD83D\uDC9B", event.getMember().getAsMention()));
        embed.setThumbnail(event.getMember().getUser().getAvatarUrl());
        embed.setFooter("TheMarvinJunior", guild.getIconUrl());
        embed.setTimestamp(Instant.now());
        embed.setColor(Color.YELLOW);

        channel.sendMessageEmbeds(embed.build()).queue();

        /*
        * Assign the verified Role to the User
        */

        Member member = event.getMember();
        Role verifiedRole = guild.getRoleById(config.get("verifiedRole"));
        Role memberRole = guild.getRoleById(config.get("memberRole"));

        if (memberRole == null || verifiedRole == null) {
            log.warn("No roles found");
            return;
        }

        // Try to add the member and verified Role to the joined user
        try {
            guild.addRoleToMember(member, verifiedRole).queue();
            guild.addRoleToMember(member, memberRole).queue();
        } catch (Exception e) {
            member.getUser().openPrivateChannel().queue(privateChannel -> {
                // notify the user if there is a permission error
                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle("❌ Fehler")
                        .setColor(Color.RED)
                        .setDescription("Ich konnte dir leider keine Mitglied Rolle geben. Bitte kontaktiere entweder Marvin oder [C0n.strukt](https://support.construkter.de)")
                        .setFooter("TheMarvinJunior", guild.getIconUrl());
                privateChannel.sendMessageEmbeds(builder.build()).queue();
            });

            // Print a log to the console
            log.error("Could not add role to member: {}", e.getMessage());
        }
    }
}
