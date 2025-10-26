package com.in.servergame.controller;

import com.in.servergame.model.MessageType;
import com.in.servergame.model.PlayerPositionMessage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class GameController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Getter
    private Map<Long, PlayerPositionMessage> activePlayers = new ConcurrentHashMap<>();

    @MessageMapping("/player.join")
    @SendTo("/topic/game")
    public PlayerPositionMessage newPlayerJoin(@Payload PlayerPositionMessage playerPositionMessage, SimpMessageHeaderAccessor headerAccessor) {
        activePlayers.put(playerPositionMessage.getPlayerId(), playerPositionMessage);
        headerAccessor.getSessionAttributes().put("player",playerPositionMessage.getPlayerId());
        System.out.println("Player of id: " + playerPositionMessage.getPlayerId() + " joined the game");
        return playerPositionMessage;
    }

    @MessageMapping("/player.action")
    @SendTo("/topic/game")
    public PlayerPositionMessage getPlayerAction(@Payload PlayerPositionMessage playerPositionMessage) {

        switch (playerPositionMessage.getMessageType()) {
            case MOVE:
                PlayerPositionMessage existingPlayer = activePlayers.get(playerPositionMessage.getPlayerId());
                if (existingPlayer != null) {
                    existingPlayer.setX(playerPositionMessage.getX());
                    existingPlayer.setY(playerPositionMessage.getY());
                    activePlayers.put(playerPositionMessage.getPlayerId(), existingPlayer);
                }
                System.out.println("Player: " + playerPositionMessage.getPlayerId() + " moved to x=" + playerPositionMessage.getX() +
                        " y=" + playerPositionMessage.getY());
                return playerPositionMessage;
            case ATTACK:
                for (Map.Entry<Long, PlayerPositionMessage> entry : activePlayers.entrySet()) {
                    Long playerId = entry.getKey();
                    PlayerPositionMessage msg = entry.getValue();

                    if (playerId.equals(playerPositionMessage.getPlayerId())) {
                        //pomin siebie
                        continue;
                    }
                    int newHealth = msg.getPlayerHealth() - playerPositionMessage.getDamage();
                    if (newHealth < 0) {
                        newHealth = 0;
                    }
                    msg.setPlayerHealth(newHealth);
                    activePlayers.put(playerId, msg);

                    PlayerPositionMessage newHealthMsg =  new PlayerPositionMessage();
                    newHealthMsg.setPlayerId(playerId);
                    newHealthMsg.setPlayerHealth(newHealth);
                    newHealthMsg.setMessageType(MessageType.HEALTH);
                    messagingTemplate.convertAndSend("/topic/game", newHealthMsg);
                    System.out.println("player health: " + newHealth);

                }
                System.out.println("Player " + playerPositionMessage.getPlayerId() + " attacked");
                break;
        }

        return playerPositionMessage;
    }

    @MessageMapping("/player.leave")
    @SendTo("/topic/game")
    public PlayerPositionMessage getPlayerLeft(@Payload PlayerPositionMessage playerPositionMessage) {
        activePlayers.remove(playerPositionMessage.getPlayerId());
        System.out.println("Player: " + playerPositionMessage.getPlayerId() + " left the game");
        return playerPositionMessage;
    }


}
