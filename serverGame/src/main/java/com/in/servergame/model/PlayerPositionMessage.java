package com.in.servergame.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerPositionMessage {

    private Long playerId;
    private String playerName;
    private int playerHealth;
    private Long targetId;
    private int damage;
    private double x;
    private double y;
    private MessageType messageType;

    public PlayerPositionMessage() {}

    public PlayerPositionMessage(Long playerId, double x, double y, MessageType messageType) {
        this.playerId = playerId;
        this.playerName = "Player " + playerId;
        this.playerHealth = 100;
        this.x = x;
        this.y = y;
        this.messageType = messageType;
    }

}
