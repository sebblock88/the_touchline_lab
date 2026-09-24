package com.thetouchlinelab.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetouchlinelab.fpl.model.Player;
import com.thetouchlinelab.fpl.repository.PlayerRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class FplApiService {

    private final PlayerRepository playerRepository;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;



    public FplApiService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
        this.restClient = RestClient.create();
        this.objectMapper = new ObjectMapper();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void syncLiveFplData() {
        try {
            String jsonResponse = restClient.get()
                    .uri("https://fantasy.premierleague.com/api/bootstrap-static/")
                    .header("User-Agent", "Mozilla/5.0")
                    .retrieve()
                    .body(String.class);

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode elementsNode = rootNode.get("elements");

            List<Player> livePlayers = new ArrayList<>();

            for (JsonNode node : elementsNode) {
                Long fplId = node.get("id").asLong();

                Player player = playerRepository.findByFplId(fplId)
                        .orElseGet(Player::new);

                player.setFplId(fplId);
                player.setFirstName(node.get("first_name").asText());
                player.setSecondName(node.get("second_name").asText());
                player.setWebName(node.get("web_name").asText());
                player.setTeam(mapTeamIdToName(node.get("team").asInt()));
                player.setPositionId(mapPositionIdToName(node.get("element_type").asInt()));

                player.setNowCost(node.get("now_cost").asInt());
                player.setTotalPoints(node.get("total_points").asInt());
                player.setBonus(node.get("bonus").asInt());
                player.setGoalsScored(node.get("goals_scored").asInt());
                player.setAssists(node.get("assists").asInt());
                player.setCleanSheets(node.get("clean_sheets").asInt());
                player.setSaves(node.get("saves").asInt());
                player.setStarts(node.get("starts").asInt());
                player.setMinutes(node.get("minutes").asInt());
                player.setYellowCards(node.get("yellow_cards").asInt());
                player.setRedCards(node.get("red_cards").asInt());

                player.setDefCon(node.has("defensive_contribution_per_90")
                        ? node.path("defensive_contribution_per_90").asDouble() : 0.0);
                player.setXGPer90(node.has("expected_goal_involvements_per_90")
                        ? node.path("expected_goal_involvements_per_90").asDouble() : 0.0);
                player.setXAPer90(node.has("expected_assists_per_90")
                        ? node.path("expected_assists_per_90").asDouble() : 0.0);

                player.setSeason("2026-27");
                player.setIsCurrent(true);

                livePlayers.add(player);
            }

            playerRepository.saveAll(livePlayers);
            System.out.println("Successfully imported " + livePlayers.size() + " live FPL players!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String mapPositionIdToName(int posId) {
        return switch (posId) {
            case 1 -> "Goalkeeper";
            case 2 -> "Defender";
            case 3 -> "Midfielder";
            case 4 -> "Forward";
            default -> "Unknown";
        };
    }

    private String mapTeamIdToName(int teamId) {
        return switch (teamId) {
            case 1 -> "Arsenal";
            case 2 -> "Aston Villa";
            case 3 -> "Bournemouth";
            case 4 -> "Brentford";
            case 5 -> "Brighton & Hove Albion";
            case 6 -> "Chelsea";
            case 7 -> "Coventry City";
            case 8 -> "Crystal Palace";
            case 9 -> "Everton";
            case 10 -> "Fulham";
            case 11 -> "Hull City";
            case 12 -> "Ipswich Town";
            case 13 -> "Leeds United";
            case 14 -> "Liverpool";
            case 15 -> "Manchester City";
            case 16 -> "Manchester United";
            case 17 -> "Newcastle United";
            case 18 -> "Nottingham Forest";
            case 19 -> "Sunderland";
            case 20 -> "Tottenham Hotspur";
            default -> "Team " + teamId;
        };
    }
}
