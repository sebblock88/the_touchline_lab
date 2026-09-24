package com.thetouchlinelab.fpl.controller;

import com.thetouchlinelab.fpl.model.Player;
import com.thetouchlinelab.fpl.repository.PlayerRepository;
import com.thetouchlinelab.service.FplApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;

@Controller
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;

    private final FplApiService fplApiService;

    public PlayerController(FplApiService fplApiService) {
        this.fplApiService = fplApiService;
    }

    @GetMapping("/players")
    public ModelAndView playersPage(
            @RequestParam(required = false, defaultValue = "ALL") String position,
            @RequestParam(required = false, defaultValue = "ppmDesc") String sort) {

        ModelAndView modelAndView = new ModelAndView("players");

        // 1. Fetch live active players from database
        List<Player> players = playerRepository.findByIsCurrentTrue();

        // Debug output in terminal to verify list size
        System.out.println("DEBUG - Total live players fetched from DB: " + players.size());

        // 2. Filter by position safely
        if (position != null && !position.isBlank() && !"ALL".equalsIgnoreCase(position)) {
            players = players.stream()
                    .filter(p -> p.getPositionId() != null && p.getPositionId().equalsIgnoreCase(position))
                    .toList();
        }

        // 3. Sort
        if ("ppmDesc".equals(sort)) {
            players = players.stream()
                    .sorted(Comparator.comparing(Player::pointsPerMillion).reversed())
                    .toList();
        }

        modelAndView.addObject("players", players);
        modelAndView.addObject("selectedPosition", position);
        modelAndView.addObject("selectedSort", sort);

        return modelAndView;
    }

    @PostMapping("/players/sync")
    public String syncPlayerData() {
        fplApiService.syncLiveFplData();

        return "redirect:/players";
    }

//    private User getCurrentUser() {
//        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
//                .getContext()
//                .getAuthentication()
//                .getPrincipal();
//        String email = (String) principal.getAttributes().get("email");
//        return userRepository.findUserByEmail(email).orElseThrow();
//    }

}
