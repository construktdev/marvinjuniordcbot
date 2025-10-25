package de.construkter.marvinjuniorbot;

import de.construkter.marvinjuniorbot.config.Config;
import de.construkter.marvinjuniorbot.modules.notifications.ButtonListener;
import de.construkter.marvinjuniorbot.modules.notifications.SendPanel;
import de.construkter.marvinjuniorbot.modules.welcome.JoinListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends ListenerAdapter {

    public static final Config CONFIG = new Config("config.properties");
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Create a JDA Builder
        JDABuilder builder = JDABuilder.createDefault(Token.get(), GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);
        builder.disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS);

        // Add the event listeners so the bot can reply to events such as Member joins
        builder.addEventListeners(new JoinListener(), new Main(), new SendPanel(), new ButtonListener());

        // Build the JDA instance and launch the Bot
        JDA jda = builder.build();

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted while waiting for jda to start: {}", e.getMessage());
        }

        // Update the SlashCommands for guilds only
        for (Guild guild : jda.getGuilds()) {
            LOGGER.info("Updating Commands for Guild: {}", guild.getName());
            guild.updateCommands().addCommands(
                    Commands.slash("notifications", "[ADMIN] Sendet ein Benachrichtigung's Panel")
                            .addOption(OptionType.CHANNEL, "channel", "Der Kanal wo es gesendet werden soll")
            ).queue();
        }

        LOGGER.info("All Commands updated!");
    }

    @Override
    public void onReady(ReadyEvent event) {
        // Print the invite link to the console when to bot is fully init
        LOGGER.info("Bot is ready!");
        LOGGER.info(event.getJDA().getInviteUrl(Permission.ADMINISTRATOR));
    }
}
