package de.construkter.marvinjuniorbot;

import de.construkter.marvinjuniorbot.config.Config;
import de.construkter.marvinjuniorbot.logging.LogManager;
import de.construkter.marvinjuniorbot.modules.matches.GameSender;
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
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Main extends ListenerAdapter {

    public static final Config CONFIG = new Config("config.properties");
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static JDA jda;

    public static void main(String[] args) {
        // Create a JDA Builder
        JDABuilder builder = JDABuilder.createDefault(Token.get(), GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);
        builder.disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS);

        // Add the event listeners so the bot can reply to events such as Member joins
        builder.addEventListeners(new JoinListener(), new Main(), new SendPanel(), new ButtonListener());

        // Build the JDA instance and launch the Bot
        jda = builder.build();

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

        // Start the daily Gameday check
        JobDetail job = JobBuilder.newJob(GameSender.DailyJob.class).build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 1))
                .build();

        Scheduler scheduler = null;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);
        } catch (Exception e) {
            LOGGER.error("Could not schedule Job!: {}", e.getMessage());
        }
        // Run the job once if specified in the arguments
        if (args.length >= 1 && args[0].equalsIgnoreCase("--test") && jda != null) {
            GameSender.run();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        // Print the invite link to the console when to bot is fully init
        LOGGER.info("Bot is ready!");
        LOGGER.info(event.getJDA().getInviteUrl(Permission.ADMINISTRATOR));
        LogManager.log("ReadyEvent", "The Bot is now ready", new HashMap<>());
    }
}
