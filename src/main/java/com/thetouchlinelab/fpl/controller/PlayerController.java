package com.thetouchlinelab.fpl.controller;

import com.thetouchlinelab.fpl.model.Player;
import com.thetouchlinelab.fpl.repository.PlayerRepository;
import com.thetouchlinelab.service.FplApiService;
import com.thetouchlinelab.service.PlayerFilter;
import com.thetouchlinelab.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;

@Controller
public class PlayerController {

    private final PlayerRepository playerRepository;
    private final FplApiService fplApiService;
    private final PlayerService playerService;

    public PlayerController(PlayerRepository playerRepository, FplApiService fplApiService, PlayerService playerService) {
        this.playerRepository = playerRepository;
        this.fplApiService = fplApiService;
        this.playerService = playerService;
    }

    @GetMapping("/players")
    public ModelAndView playersPage(
            @RequestParam(required = false, defaultValue = "ALL") String position,
            @RequestParam(required = false, defaultValue = "ppmDesc") String sort,
            @RequestParam(required = false) Integer minMinutes,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minDefCon) {

        ModelAndView modelAndView = new ModelAndView("players");

        // This fetches live active players from database
        List<Player> players = playerRepository.findByIsCurrentTrue();

        String positionFilter = (position == null || position.isBlank() || "ALL".equalsIgnoreCase(position)
                ? null : position);

        PlayerFilter filter = new PlayerFilter(minMinutes, maxPrice, minDefCon, positionFilter);

        players = playerService.filterPlayers(players, filter);



//        // This filters by position safely
//        if (position != null && !position.isBlank() && !"ALL".equalsIgnoreCase(position)) {
//            players = players.stream()
//                    .filter(p -> p.getPositionId() != null && p.getPositionId().equalsIgnoreCase(position))
//                    .toList();
//        }

        // 3. Sort
        if ("ppmDesc".equals(sort)) {
            players = players.stream()
                    .sorted(Comparator.comparing(Player::pointsPerMillion).reversed())
                    .toList();
        }

        modelAndView.addObject("players", players);
        modelAndView.addObject("selectedPosition", position);
        modelAndView.addObject("selectedSort", sort);
        modelAndView.addObject("minMinutes", minMinutes);
        modelAndView.addObject("maxPrice", maxPrice);
        modelAndView.addObject("minDefCon", minDefCon);


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
