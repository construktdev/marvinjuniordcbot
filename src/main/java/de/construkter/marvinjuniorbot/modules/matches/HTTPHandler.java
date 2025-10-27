package de.construkter.marvinjuniorbot.modules.matches;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPHandler {

    Logger logger = LoggerFactory.getLogger(HTTPHandler.class);

    public JsonNode getGames(int spieltag) {
        String liga = "bl2";
        int year = 2025;

        String url = "https://api.openligadb.de/getmatchdata/" + liga + "/" + year + "/" + spieltag;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage());
            return null;
        }

        if (response == null) {
            return null;
        }

        String body = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;

        try {
            jsonNode = objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            return null;
        }

        for (JsonNode match : jsonNode) {
            try {
                String team1 = match.get("team1").get("teamName").asText();
                String team2 = match.get("team2").get("teamName").asText();

                if ("Dynamo Dresden".equals(team1) || "Dynamo Dresden".equals(team2)) {
                    return match;
                }
            } catch (NullPointerException e) {
                // logger.error(e.getMessage());
            }
        }
        return null;
    }
}
