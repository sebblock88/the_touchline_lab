package com.thetouchlinelab.fpl.service;

import com.thetouchlinelab.fpl.model.Player;
import com.thetouchlinelab.service.PlayerService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerServiceTest {
//    good value for each position should be take price into consideration but only for their position

//    highest value Goalkeepers are no more than £6.1 but range from £4.0 so shorter gap

//    Defenders range from £4.0 - £6.5 with the outlier being Gabriel at £8.0

//    Midfielders and Forwards are more expensive due to their creativity/potential in assists and goals so values skew the points

//    Midfielders range from £4.4 - £11.9 - average would probably be about £7.0-£7.5

//    Forwards range from £4.6 - £9.1 with Haaland being an outlier at £15.6 and the lower values barely getting minutes


    @Test
    public void valueRating_goalkeepers_showsHighValueKeeper() {

        PlayerService playerService = new PlayerService();

        List<Player> playerList = new ArrayList<>();

        Player raya = Player.builder().webName("Raya").nowCost(61).totalPoints(30).build();
        Player pickford = Player.builder().webName("Pickford").nowCost(55).totalPoints(25).build();
        Player tzolakis = Player.builder().webName("Tzolakis").nowCost(47).totalPoints(34).build();

        playerList.add(raya);
        playerList.add(pickford);
        playerList.add(tzolakis);

        List<Player> ranked = playerService.rankByValue(playerList);

        assertEquals(tzolakis, ranked.get(0));
    }

    @Test
    public void valueRating_positionRelative_showsHighValuePlayersGrouped() {

        PlayerService playerService = new PlayerService();

        List<Player> playerList = new ArrayList<>();

        Player gabriel = Player.builder().webName("Gabriel").positionId("Defender").nowCost(80).totalPoints(25).minutes(450).build();
        Player bogle = Player.builder().webName("Bogle").positionId("Defender").nowCost(46).totalPoints(42).minutes(365).build();
        Player hall = Player.builder().webName("Hall").positionId("Defender").nowCost(53).totalPoints(31).minutes(449).build();

        Player haaland = Player.builder().webName("Haaland").positionId("Forward").nowCost(156).totalPoints(39).minutes(450).build();
        Player isak = Player.builder().webName("Isak").positionId("Forward").nowCost(91).totalPoints(33).minutes(413).build();
        Player kostoulas = Player.builder().webName("Kostoulas").positionId("Forward").nowCost(56).totalPoints(28).minutes(353).build();

        playerList.add(gabriel);
        playerList.add(bogle);
        playerList.add(hall);
        playerList.add(haaland);
        playerList.add(isak);
        playerList.add(kostoulas);

        Map<String, List<Player>> result = playerService.rankValueByPosition(playerList);

        assertEquals(bogle, result.get("Defender").getFirst());

        assertEquals(kostoulas, result.get("Forward").getFirst());

    }


}



