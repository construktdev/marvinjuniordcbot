package de.construkter.marvinjuniorbot.modules.matches;

import com.fasterxml.jackson.databind.JsonNode;
import de.construkter.marvinjuniorbot.Main;
import de.construkter.marvinjuniorbot.config.Config;
import de.construkter.marvinjuniorbot.logging.LogManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameSender {

    private static final Logger log = LoggerFactory.getLogger(GameSender.class);
    private static final Config config = Main.CONFIG;

    public static void run() {
        LogManager.log("Gameday Checker", "Running daily Gameday check", new HashMap<>());
        HTTPHandler httpHandler = new HTTPHandler();
        List<JsonNode> games = new ArrayList<>();

        for (int i = 1; i <= 38; i++) {
            games.add(httpHandler.getGames(i));
        }

        for (JsonNode game : games) {
            if (game == null) return;
            String dateStr = game.get("matchDateTime").asText();

            LocalDateTime gameDateTime = LocalDateTime.parse(dateStr);
            LocalDate gameDate = gameDateTime.toLocalDate();
            LocalDate today = LocalDate.now();

            if (gameDate.equals(today)) {
                sendEmbed(game, gameDateTime);
                break;
            }
        }
    }

    public static class DailyJob implements Job {

        private static final Logger logger = LoggerFactory.getLogger("GamedayHandler");
        @Override
        public void execute(JobExecutionContext jobExecutionContext) {
            logger.info("Running Daily Gameday Check...");
            run();
        }
    }

    private static void sendEmbed(JsonNode match, LocalDateTime date) {
        JDA jda = Main.jda;
        log.info("Sending Gameday Embed");
        TextChannel target = jda.getTextChannelById(config.get("gamedayChannel"));

        if (target == null) {
            log.warn("Could not find channel for sending Gameday Embed");
            return;
        }

        String team1 = match.get("team1").get("teamName").asText();
        String team2 = match.get("team2").get("teamName").asText();
        String gameType;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = date.format(formatter);

        if (team1.equals("Dynamo Dresden")) {
            gameType = "Heimspiel";
        } else {
            gameType = "Auswärtsspiel";
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("\uD83D\uDDA4\uD83D\uDC9B Spieltag ⚽");
        embedBuilder.setColor(Color.YELLOW);
        embedBuilder.setDescription("Dynamo Dresden hat heute ein " + gameType + "!\n\n" +
                "**" + team1 + "** vs. **" + team2 + "**\n" +
                "Gebt in diesem Channel gerne eure Spieltag-Tips ab.\n\n" +
                "Spielbeginn: " + time + "Uhr\n\n" +
                "Forza Dynamo \uD83D\uDDA4\uD83D\uDC9B");
        embedBuilder.setFooter("TheMarvinJunior", jda.getSelfUser().getAvatarUrl());
        embedBuilder.setThumbnail("https://cdn.construkter.de/SGD.png");

        target.sendMessageEmbeds(embedBuilder.build()).queue();
        target.sendMessage("@everyone").queue( message -> {
            message.delete().queue();
        });
    }
}
