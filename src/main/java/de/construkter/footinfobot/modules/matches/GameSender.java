package de.construkter.footinfobot.modules.matches;

import com.fasterxml.jackson.databind.JsonNode;
import de.construkter.footinfobot.Main;
import de.construkter.footinfobot.config.Config;
import de.construkter.footinfobot.logging.LogManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
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
        HTTPHandler httpHandler = new HTTPHandler();
        List<JsonNode> games = new ArrayList<>();
        HashMap<String, String> args = new HashMap<>();

        for (int i = 1; i <= 38; i++) {
            games.add(httpHandler.getGame(i));
        }

        for (JsonNode game : games) {
            if (game == null) {
                return;
            }
            String dateStr = game.get("matchDateTime").asText();

            LocalDateTime gameDateTime = LocalDateTime.parse(dateStr);
            LocalDate gameDate = gameDateTime.toLocalDate();
            LocalDate today = LocalDate.now();

            if (gameDate.equals(today)) {
                Main.todayWasGame = true;
                sendEmbed(game, gameDateTime);
                args.put("gameToday", "true");
                return;
            } else if (Main.todayWasGame) {
                Main.todayWasGame = false;
                sendResultEmbed(httpHandler.getGame(Main.currentSpieltag));
                args.put("resultToday", "true");
            } else {
                args.put("gameToday", "true");
            }
        }

        LogManager.log("Gameday Checker", "Running daily Gameday check", args);
    }

    public static class DailyJob implements Job {

        private static final Logger logger = LoggerFactory.getLogger("GamedayHandler");
        @Override
        public void execute(final JobExecutionContext jobExecutionContext) {
            logger.info("Running Daily Gameday Check...");
            run();
        }
    }

    @SuppressWarnings("checkstyle:Indentation")
    private static void sendEmbed(final JsonNode match, final LocalDateTime date) {
        JDA jda = Main.jda;
        log.info("Sending Gameday Embed");
        TextChannel target = jda.getTextChannelById(config.get("gamedayChannel"));

        Main.currentSpieltag = match.get("group").get("groupOrderID").asInt();

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
        embedBuilder.setFooter("FootInfoBot", jda.getSelfUser().getAvatarUrl());
        embedBuilder.setThumbnail("https://cdn.construkter.de/SGD.png");

        target.sendMessageEmbeds(embedBuilder.build()).queue();
        target.sendMessage("@everyone").queue( message -> message.delete().queue());
    }

    public static void sendResultEmbed(final JsonNode match) {
        JDA jda = Main.jda;
        log.info("Sending Result Embed");
        TextChannel target = jda.getTextChannelById(config.get("gamedayChannel"));

        if (target == null) {
            log.warn("Could not find channel for sending Result Embed");
            return;
        }

        String team1 = match.get("team1").get("teamName").asText();
        String team2 = match.get("team2").get("teamName").asText();

        int points1 = 0;
        int points2 = 0;

        for (JsonNode result : match.get("matchResults")) {
            if (result.get("resultName").asText().equals("Endergebnis")) {
                points1 = result.get("pointsTeam1").asInt();
                points2 = result.get("pointsTeam2").asInt();
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("\uD83D\uDDA4\uD83D\uDC9B Spieltag (Endergebnis) ⚽");
        embedBuilder.setColor(Color.YELLOW);
        embedBuilder.setDescription("Ergebnis des vergangenen Spiels\n" +
                "**" + team1 + " - " + points1 + ":" + points2 + " - " + team2 +
                "-# Die Daten kommen von einer öffentlichen API - keine Gewähr auf Richtig- oder Vollständigheit");
        embedBuilder.setFooter("FootInfoBot", jda.getSelfUser().getAvatarUrl());
        embedBuilder.setThumbnail("https://cdn.construkter.de/SGD.png");

        target.sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
