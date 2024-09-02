package org.example;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class NBAStatsService {
    private static final String API_BASE_URL = "https://api-nba-v1.p.rapidapi.com/";
    private static final String API_KEY = "bd2c81e03cmsh8a8d7dffc4442a0p1b8612jsn20a02d70f095"; // Replace with your actual API key

    private final HttpClient httpClient;

    public NBAStatsService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    private HttpRequest buildRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + endpoint))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", "api-nba-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
    }

    public String getPlayerInfo(String playerName) throws IOException, InterruptedException {
        String encodedName = URLEncoder.encode(playerName, StandardCharsets.UTF_8);
        HttpRequest request = buildRequest("players?search=" + encodedName);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getPlayerStats(int playerId) throws IOException, InterruptedException {
        HttpRequest request = buildRequest("players/statistics?player=" + playerId);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getTeamStats(int teamId) throws IOException, InterruptedException {
        HttpRequest request = buildRequest("teams?id=" + teamId);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getGameStats(int gameId) throws IOException, InterruptedException {
        HttpRequest request = buildRequest("games?id=" + gameId);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

}