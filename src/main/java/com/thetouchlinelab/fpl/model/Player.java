package com.thetouchlinelab.fpl.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "players")

public class Player {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "fpl_id", unique = true)
    private Long fplId;

    private String firstName;
    private String secondName;
    private String webName;
    private String team;
    private String positionId;

    private Integer nowCost;
    private Integer totalPoints;
    private Integer bonus;
    private Integer goalsScored;
    private Integer assists;
    private Integer cleanSheets;
    private Integer saves;
    private Integer starts;
    private Integer minutes;
    private Integer yellowCards;
    private Integer redCards;

    @Column(name = "defensive_contribution_per_90")
    private Double defCon;

    @Column(name = "expected_goal_involvements_per_90")
    private Double xGPer90;

    @Column(name = "expected_assists_per_90")
    private Double xAPer90;

    private String season;
    private Boolean isCurrent;

    private Integer gamesHitDefCon;
    private Integer gamesWithBonus;

    public Double getCostInMillions() {
        return nowCost != null ? nowCost / 10.0 : 0.0;
    }

    public double pointsPerMillion() {
        if (nowCost == null || nowCost == 0 || totalPoints == null) return 0.0;
        double ppm = (double) totalPoints / getCostInMillions();
        return Math.round(ppm * 100.0) / 100.0;
    }



}
