package com.in.servergame.config;

import com.in.servergame.controller.GameController;
import com.in.servergame.model.PlayerPositionMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;



@RestController
@RequestMapping("/api/game")
public class WebController {


    @Autowired
    private GameController gameController;


    @GetMapping("/players")
    public Collection<PlayerPositionMessage> getAllPlayers() {
        Map<Long, PlayerPositionMessage> player = gameController.getActivePlayers();
        return player.values();
    }

}
