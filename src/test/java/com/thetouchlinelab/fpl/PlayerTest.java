package com.thetouchlinelab.fpl;

import com.thetouchlinelab.fpl.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    @Test
    public void costInMillions_numberCost_returnsDouble() {
        Player player = Player.builder()
                .nowCost(100)
                .build();

        Double actual = player.getCostInMillions();

        assertEquals(10.0, actual, 0.001);

    }

    @Test
    public void costInMillions_nullCost_returnsZero() {
        Player player = Player.builder()
                .nowCost(null)
                .build();

        Double actual = player.getCostInMillions();

        assertEquals(0.0, actual, 0.001);
    }

    @Test
    public void costInMillions_zeroCost_returnsZero() {
        Player player = Player.builder()
                .nowCost(0)
                .build();

        Double actual = player.getCostInMillions();

        assertEquals(0.0, actual, 0.001);
    }

    @Test
    public void pointsPerMillion_costAndPoints_returnsDoubleRounded() {
        Player player = Player.builder()
                .nowCost(150)
                .totalPoints(178)
                .build();

        Double actual = player.pointsPerMillion();

        assertEquals(11.87, actual, 0.001);
    }

    @Test
    public void pointsperMillion_numberCost_zeroPoints_returnsZero() {
        Player player = Player.builder()
                .nowCost(150)
                .totalPoints(0)
                .build();

        Double actual = player.pointsPerMillion();

        assertEquals(0.0, actual, 0.001);
    }

    @Test
    public void pointsPerMillion_nullPoints_returnsZero() {
        Player player = Player.builder()
                .nowCost(150)
                .totalPoints(null)
                .build();

        Double actual = player.pointsPerMillion();

        assertEquals(0.0, actual, 0.001);
    }

    @Test
    public void pointsPerMillion_nullCost_returnsZero() {
        Player player = Player.builder()
                .nowCost(null)
                .totalPoints(150)
                .build();

        Double actual = player.pointsPerMillion();

        assertEquals(0.0, actual, 0.001);
    }

    @Test
    public void pointsPerMillion_zeroCost_returnsZero() {
        Player player = Player.builder()
                .nowCost(0)
                .totalPoints(150)
                .build();

        Double actual = player.pointsPerMillion();

        assertEquals(0.0, actual, 0.001);
    }
}
