package com.thetouchlinelab.service;

import com.thetouchlinelab.fpl.model.Player;
import com.thetouchlinelab.fpl.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;


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

//        System.out.println(positionList);

        return positionList;


    }
}
