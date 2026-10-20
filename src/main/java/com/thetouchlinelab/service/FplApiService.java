package com.thetouchlinelab.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thetouchlinelab.fpl.model.Player;
import com.thetouchlinelab.fpl.repository.PlayerRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
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
            JsonNode teamsNode = rootNode.get("teams");
            JsonNode positionNode = rootNode.get("element_types");

            Map<Integer, String> teams = new HashMap<>();

            Map<Integer, String> positions = new HashMap<>();

            Map<Long, Player> playersByFplId = new HashMap<>();

            List<Player> livePlayers = new ArrayList<>();

            List<Player> existingPlayers = playerRepository.findAll();

            for (Player existingPlayer : existingPlayers) {
                playersByFplId.put(existingPlayer.getFplId(), existingPlayer);
            }

            for (JsonNode node : teamsNode ) {
                Integer teamId = node.get("id").asInt();
                String teamName = node.get("name").asText();

                teams.put(teamId, teamName);
            }

            for (JsonNode node : positionNode ) {
                Integer posId = node.get("id").asInt();
                String posName = node.get("singular_name").asText();

                positions.put(posId, posName);
            }



            for (JsonNode node : elementsNode) {


                Long fplId = node.get("id").asLong();

                Player player = playersByFplId.get(fplId);

                if (player == null) {
                    player = new Player();
                }

                int teamId = node.get("team").asInt();
                int posId = node.get("element_type").asInt();

                player.setFplId(fplId);
                player.setFirstName(node.get("first_name").asText());
                player.setSecondName(node.get("second_name").asText());
                player.setWebName(node.get("web_name").asText());
                player.setPositionId(positions.get(posId));
                player.setTeam(teams.get(teamId));
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
}


