package de.construkter.marvinjuniorbot.modules.news;

import com.rometools.rome.feed.synd.SyndEntry;
import de.construkter.marvinjuniorbot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NewsSender {

    private static final Logger log = LoggerFactory.getLogger(NewsSender.class);

    public static void run() {
        SyndEntry newest = RssHandler.getNewestFeed();
        if (newest == null) {
            return;
        }
        String title = newest.getTitle();

        if (title.equals(getLastTitle())) {
            return;
        }
        sendEmbed(newest);
    }

    public static class HourlyJob implements Job {

        private static final Logger logger = LoggerFactory.getLogger("NewsHandler");
        @Override
        public void execute(final JobExecutionContext jobExecutionContext) {
            logger.info("Running Hourly News Check...");
            if (Main.todayWasGame) {
                return;
            }
            run();
        }
    }

    public static String getLastTitle() {
        String filePath = "last.txt";
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            return content.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static void sendEmbed(final SyndEntry feed) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(feed.getTitle());
        embed.setDescription(feed.getDescription().getValue());
        embed.setThumbnail("https://cdn.construkter.de/sgd.png");
        embed.addField("Voller Artikel (kicker.de)", "[Klicke hier](" + feed.getLink() +  ")",
                false);
        embed.setTimestamp(feed.getPublishedDate().toInstant());
        embed.setFooter("Kicker News Feed. Veröffentlicht:");

        TextChannel newsChannel = Main.jda.getTextChannelById(Main.CONFIG.get("newsChannel"));

        if (newsChannel == null) {
            log.error("News Channel is null");
            log.info("Channel ID: {}", Main.CONFIG.get("newsChannel"));
            return;
        }

        newsChannel.sendMessageEmbeds(embed.build()).queue();
    }
}
