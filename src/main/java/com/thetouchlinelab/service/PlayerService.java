package com.thetouchlinelab.service;

import com.thetouchlinelab.fpl.model.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class PlayerService {


    public List<Player> rankByValue(List<Player> players) {

        List<Player> rankedPlayers = new ArrayList<Player>(players);

        rankedPlayers.sort(Comparator.comparingDouble( Player::pointsPerMillion).reversed());

        return rankedPlayers;
    }
}
