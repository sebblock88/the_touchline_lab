package com.thetouchlinelab.fpl.service;

import com.thetouchlinelab.fpl.model.Player;
import com.thetouchlinelab.service.PlayerFilter;
import com.thetouchlinelab.service.PlayerService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerFilterTest {

    @Test
    public void nullMinMinutes_nullMaxPrice_returnsAllPlayers() {

        PlayerService playerService = new PlayerService();

        List<Player> playerList = new ArrayList<>();

        PlayerFilter noConditions = new PlayerFilter(null, null);

        Player raya = Player.builder().webName("Raya").nowCost(61).minutes(450).totalPoints(30).build();
        Player pickford = Player.builder().webName("Pickford").nowCost(55).minutes(290).totalPoints(25).build();
        Player tzolakis = Player.builder().webName("Tzolakis").nowCost(47).minutes(90).totalPoints(34).build();

        playerList.add(raya);
        playerList.add(pickford);
        playerList.add(tzolakis);

        List<Player> result = playerService.filterPlayers(playerList, noConditions);

        assertEquals(3, result.size());
    }
}
