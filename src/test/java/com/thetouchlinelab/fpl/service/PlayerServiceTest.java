package com.thetouchlinelab.fpl.service;

import com.thetouchlinelab.fpl.model.Player;
import com.thetouchlinelab.service.PlayerService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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


}



