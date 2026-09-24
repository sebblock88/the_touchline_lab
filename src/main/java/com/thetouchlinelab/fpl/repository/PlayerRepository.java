package com.thetouchlinelab.fpl.repository;

import com.thetouchlinelab.fpl.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByIsCurrentTrue();

    List<Player> findBySeason(String string);

    List<Player> findByWebNameIgnoreCaseOrderBySeasonAsc(String webName);

    Optional<Player> findByFplId(Long fplId);

    @Transactional
    void deleteByIsCurrentTrue();
}
