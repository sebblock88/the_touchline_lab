package com.thetouchlinelab.fpl.service;

import com.thetouchlinelab.fpl.model.Player;
import com.thetouchlinelab.service.PlayerFilter;
import com.thetouchlinelab.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {
//    good value for each position should be take price into consideration but only for their position

//    highest value Goalkeepers are no more than £6.1 but range from £4.0 so shorter gap

//    Defenders range from £4.0 - £6.5 with the outlier being Gabriel at £8.0

//    Midfielders and Forwards are more expensive due to their creativity/potential in assists and goals so values skew the points

//    Midfielders range from £4.4 - £11.9 - average would probably be about £7.0-£7.5

//    Forwards range from £4.6 - £9.1 with Haaland being an outlier at £15.6 and the lower values barely getting minutes

    private PlayerService playerService;
    private List<Player> playerList;
    private Player gabriel;
    private Player bogle;
    private Player hall;
    private Player haaland;
    private Player isak;
    private Player kostoulas;

    @BeforeEach
    void setup() {
        playerService = new PlayerService();
        playerList = new ArrayList<>();

        // Defenders
        gabriel = Player.builder().webName("Gabriel").positionId("Defender").nowCost(80).totalPoints(25).minutes(450).build();
        bogle = Player.builder().webName("Bogle").positionId("Defender").nowCost(46).totalPoints(42).minutes(365).build();
        hall = Player.builder().webName("Hall").positionId("Defender").nowCost(53).totalPoints(31).minutes(449).build();
        // Forwards
        haaland = Player.builder().webName("Haaland").positionId("Forward").nowCost(156).totalPoints(39).minutes(450).build();
        isak = Player.builder().webName("Isak").positionId("Forward").nowCost(91).totalPoints(33).minutes(413).build();
        kostoulas = Player.builder().webName("Kostoulas").positionId("Forward").nowCost(56).totalPoints(28).minutes(353).build();

        playerList.add(gabriel);
        playerList.add(bogle);
        playerList.add(hall);
        playerList.add(haaland);
        playerList.add(isak);
        playerList.add(kostoulas);

    }

    @Test
    public void valueRating_goalkeepers_showsHighValueKeeper() {

        List<Player> keepers = new ArrayList<>();

        Player raya = Player.builder().webName("Raya").nowCost(61).totalPoints(30).build();
        Player pickford = Player.builder().webName("Pickford").nowCost(55).totalPoints(25).build();
        Player tzolakis = Player.builder().webName("Tzolakis").nowCost(47).totalPoints(34).build();

        keepers.add(raya);
        keepers.add(pickford);
        keepers.add(tzolakis);

        List<Player> ranked = playerService.rankByValue(keepers);

        assertEquals(tzolakis, ranked.getFirst());
    }


    @Test
    public void valueRating_positionRelative_showsHighValuePlayersGrouped() {

        Map<String, List<Player>> result = playerService.rankValueByPosition(playerList);

        assertEquals(bogle, result.get("Defender").getFirst());

        assertEquals(kostoulas, result.get("Forward").getFirst());

    }

    @Test
    public void filterPlayers_NullMins_NullMaxPrice_ReturnsAllPlayers() {

        PlayerFilter playerFilter = new PlayerFilter(null, null, null, null);

        List<Player> filteredPlayers = playerService.filterPlayers(playerList, playerFilter);

        assertEquals(6, filteredPlayers.size());
        assertTrue(filteredPlayers.containsAll(playerList));

    }

    @Test
    public void filterPlayers_400Mins_NullMaxPrice_ReturnsFourPlayers() {

        PlayerFilter playerFilter = new PlayerFilter(400, null, null, null);

        List<Player> filteredPlayers = playerService.filterPlayers(playerList, playerFilter);

        assertEquals(4, filteredPlayers.size());
        assertFalse(filteredPlayers.contains(bogle));
        assertFalse(filteredPlayers.contains(kostoulas));

    }

    @Test
    public void filterPlayers_400Mins_60MaxPrice_ReturnsOnePlayer() {

        PlayerFilter playerFilter = new PlayerFilter(400, 6.0, null, null);

        List<Player> filteredPlayers = playerService.filterPlayers(playerList, playerFilter);

        assertEquals(1, filteredPlayers.size());
        assertTrue(filteredPlayers.contains(hall));
        assertFalse(filteredPlayers.contains(isak));

    }

    @Test
    public void nullMinsNewPlayer_400minMins_nullMaxPrice_ExcludesNewPlayer() {

        PlayerFilter playerFilter = new PlayerFilter(400, null, null, null);

        Player newPlayer = Player.builder().webName("New Player").positionId("Forward").nowCost(56).totalPoints(28).build();

        playerList.add(newPlayer);

        List<Player> filteredPlayers = playerService.filterPlayers(playerList, playerFilter);

        // hall is a control test
        assertTrue(filteredPlayers.contains(hall));
        assertFalse(filteredPlayers.contains(newPlayer));

    }

    @Test
    public void zeroMinsNewPlayer_400minMins_nullMaxPrice_ExcludesNewPlayer() {

        PlayerFilter playerFilter = new PlayerFilter(400, null, null,null);

        Player newPlayer = Player.builder().webName("New Player").positionId("Forward").nowCost(56).minutes(0).totalPoints(28).build();

        playerList.add(newPlayer);

        List<Player> filteredPlayers = playerService.filterPlayers(playerList, playerFilter);

        // hall is a control test
        assertTrue(filteredPlayers.contains(hall));
        assertFalse(filteredPlayers.contains(newPlayer));

    }

    @Test
    public void nullMinsFilter_nullNowCost_MaxPriceSet_ExcludesNewPlayer() {

        PlayerFilter playerFilter = new PlayerFilter(null, 6.0, null, null);

        Player newPlayer = Player.builder().webName("New Player").positionId("Forward").totalPoints(28).build();

        playerList.add(newPlayer);

        List<Player> filteredPlayers = playerService.filterPlayers(playerList, playerFilter);

        // hall is a control test
        assertTrue(filteredPlayers.contains(hall));
        assertFalse(filteredPlayers.contains(newPlayer));

    }




}



