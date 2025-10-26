package com.in.clientgame.model;

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

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public void setPlayerName() {
        this.playerName = "Player " + playerId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
