package com.thetouchlinelab.fpl.repository;

import com.thetouchlinelab.fpl.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByIsCurrentTrue();

    List<Player> findBySeason(String string);

    List<Player> findByWebNameIgnoreCaseOrderBySeasonAsc(String webName);

}
