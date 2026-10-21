package com.thetouchlinelab.service;

import com.thetouchlinelab.fpl.model.Player;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


public class PlayerService {

    private static final Comparator<Player> BY_PPM_DESCENDING = Comparator.comparingDouble(Player::pointsPerMillion).reversed();

    public List<Player> rankByValue(List<Player> players) {

        List<Player> rankedPlayers = new ArrayList<Player>(players);

        rankedPlayers.sort(BY_PPM_DESCENDING);

        return rankedPlayers;
    }

    public Map<String, List<Player>> rankValueByPosition(List<Player> players) {

        Map<String, List<Player>> positionList = players.stream().collect(groupingBy(Player::getPositionId));

        for (List<Player> group : positionList.values()) {
            group.sort(BY_PPM_DESCENDING);
        }

        return positionList;
    }

    public List<Player> filterPlayers(List<Player> players, PlayerFilter filter) {

        return players.stream().filter(player -> filter.minMinutes() == null
                        || (player.getMinutes() != null && player.getMinutes() >= filter.minMinutes()))
                .filter(player -> filter.maxPrice() == null || (player.getNowCost() != null
                        && player.getCostInMillions() <= filter.maxPrice()))
            .toList();
        }

    }
