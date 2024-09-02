package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NBAStatsApp {
    private static String lastApiResponse = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        NBAStatsService statsService = new NBAStatsService();

        while (true) {
            System.out.println("\nWelcome to the NBA Stats Application!");
            System.out.println("1. View Player Stats");
            System.out.println("2. View Team Stats");
            System.out.println("3. View Game Stats");
            System.out.println("4. View Last API Response");
            System.out.println("Enter your choice (or 'q' to quit): ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("q")) {
                System.out.println("Thank you for using the NBA Stats Application. Goodbye!");
                break;
            }

            switch (choice) {
                case "1":
                    viewPlayerStats(scanner, statsService);
                    break;
                case "2":
                    System.out.println("Team stats functionality not implemented yet.");
                    break;
                case "3":
                    System.out.println("Game stats functionality not implemented yet.");
                    break;
                case "4":
                    viewLastApiResponse();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void viewPlayerStats(Scanner scanner, NBAStatsService statsService) {
        System.out.print("Enter player name (full name or last name): ");
        String playerName = scanner.nextLine();
        try {
            String playerInfo = statsService.getPlayerInfo(playerName);
            lastApiResponse = playerInfo; // Store the API response

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(playerInfo);

            if (rootNode.path("results").asInt() > 0) {
                JsonNode playersNode = rootNode.path("response");
                List<JsonNode> matchingPlayers = new ArrayList<>();

                for (JsonNode player : playersNode) {
                    String fullName = player.path("firstname").asText() + " " + player.path("lastname").asText();
                    if (fullName.toLowerCase().contains(playerName.toLowerCase()) ||
                            player.path("lastname").asText().toLowerCase().contains(playerName.toLowerCase())) {
                        matchingPlayers.add(player);
                    }
                }

                if (matchingPlayers.size() == 1) {
                    displayPlayerInfo(matchingPlayers.get(0));
                }
                else if (matchingPlayers.size() > 1) {
                    System.out.println("\nMultiple players found:");
                    for (int i = 0; i < matchingPlayers.size(); i++) {
                        JsonNode player = matchingPlayers.get(i);
                        System.out.println((i + 1) + ". " + player.path("firstname").asText() + " " + player.path("lastname").asText());
                    }
                    System.out.print("Enter the number of the player you want to view: ");
                    int choice = Integer.parseInt(scanner.nextLine()) - 1;
                    if (choice >= 0 && choice < matchingPlayers.size()) {
                        displayPlayerInfo(matchingPlayers.get(choice));
                    }
                    else {
                        System.out.println("Invalid choice.");
                    }
                }
                else {
                    System.out.println("No players found matching: " + playerName);
                }
            }
            else {
                System.out.println("No players found matching: " + playerName);
            }

        }
        catch (IOException | InterruptedException e) {
            System.out.println("An error occurred while fetching player info: " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void displayPlayerInfo(JsonNode playerNode) {
        String firstName = playerNode.path("firstname").asText();
        String lastName = playerNode.path("lastname").asText();
        String position = playerNode.path("leagues").path("standard").path("pos").asText();
        String jersey = playerNode.path("leagues").path("standard").path("jersey").asText();
        String college = playerNode.path("college").asText();
        String birthDate = playerNode.path("birth").path("date").asText();

        System.out.println("\nPlayer found:");
        System.out.println("---------------------------");
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Position: " + (position.isEmpty() ? "N/A" : position));
        System.out.println("Jersey Number: " + (jersey.isEmpty() ? "N/A" : jersey));
        System.out.println("College: " + (college.isEmpty() ? "N/A" : college));
        System.out.println("Birth Date: " + (birthDate.isEmpty() ? "N/A" : birthDate));
        System.out.println("Team: N/A (Information not available)");
        System.out.println("---------------------------");
    }

    private static void viewLastApiResponse() {
        if (lastApiResponse != null) {
            System.out.println("\nLast API Response:");
            System.out.println(lastApiResponse);
        } else {
            System.out.println("No API response available. Please view player stats first.");
        }
    }
}