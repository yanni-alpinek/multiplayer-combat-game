package com.in.clientgame.controller;
import com.in.clientgame.model.MessageType;
import com.in.clientgame.model.PlayerPositionMessage;
import com.in.clientgame.network.WebSocketClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;
import java.util.*;

public class GameController {

    private static final double WORLD_WIDTH = 3000;
    private static final double WORLD_HEIGHT = 3000;
    private double cameraX;
    private double cameraY;

    private Map<Long, PlayerPositionMessage> players =  new HashMap<>();
    private Set<Long> playerInGame = new HashSet<>();
    private WebSocketClient webSocketClient;
    private Long myPlayerID;



    @FXML
    private Canvas gameCanvas;

    @FXML
    public void initialize() {
        myPlayerID = (long) Math.floor(Math.random()*100);

        PlayerPositionMessage me = new PlayerPositionMessage(myPlayerID, 1500,1500, MessageType.JOIN);
        players.put(myPlayerID, me);
        playerInGame.add(myPlayerID);

        webSocketClient = new WebSocketClient(myPlayerID,
                this::handleIncomingMessages,
                me);
        webSocketClient.connect("ws://localhost:8080/game-websocket");
    }

    public void handleIncomingMessages(PlayerPositionMessage playerPositionMessage) {

        PlayerPositionMessage player = players.get(playerPositionMessage.getPlayerId());

        switch (playerPositionMessage.getMessageType()) {
            case JOIN:
                if (!playerPositionMessage.getPlayerId().equals(myPlayerID) &&
                     !playerInGame.contains(playerPositionMessage.getPlayerId())) {

                  playerInGame.add(playerPositionMessage.getPlayerId());
                  sendMyCurrentPosition();
                }

                players.put(playerPositionMessage.getPlayerId(), playerPositionMessage);
                redrawCanvas();
                break;
            case MOVE:
                if (player != null) {
                    player.setX(playerPositionMessage.getX());
                    player.setY(playerPositionMessage.getY());
                    redrawCanvas();
                }
                break;
            case HEALTH:
                if (player != null) {
                    player.setPlayerHealth(playerPositionMessage.getPlayerHealth());
                    redrawCanvas();
                }
                break;
            case LEAVE:
                players.remove(playerPositionMessage.getPlayerId());
                redrawCanvas();
                break;
        }
    }

    public void sendMyCurrentPosition() {
        PlayerPositionMessage me = players.get(myPlayerID);

        if (me == null) return;

        //double myXposition = me.getX();
        //double myYposition = me.getY();

        webSocketClient.sendJoinMessage(me);
    }

    public void handleKeyPressed(KeyEvent keyEvent) {

        PlayerPositionMessage me = players.get(myPlayerID);

        if (me == null) return;

        switch (keyEvent.getCode()) {
            case W -> {
                me.setY(me.getY() - 5);
                break;
            }
            case S  -> {
                me.setY(me.getY() + 5);
                break;
            }
            case A -> {
                me.setX(me.getX() - 5);
                break;
            }
            case D -> {
                me.setX(me.getX() + 5);
                break;
            }
            case B -> {
                sendAttackToServer(3);
                break;
            }
            default -> {
                return;
            }
        }
            sendMoveToServer(me.getX(), me.getY());
    }


    private void sendMoveToServer(double x, double y) {
        PlayerPositionMessage moveMsg = new PlayerPositionMessage();
        moveMsg.setPlayerId(myPlayerID);
        moveMsg.setX(x);
        moveMsg.setY(y);
        moveMsg.setMessageType(MessageType.MOVE);

        webSocketClient.sendMessage(moveMsg);
    }

    private void redrawCanvas() {
        PlayerPositionMessage me = players.get(myPlayerID);
        if (me == null) return;

        cameraX = me.getX() - 600;
        cameraY = me.getY() - 400;

        if (cameraX < 0 ) {cameraX = 0;}
        if (cameraX > 1800) {cameraX = 1800;}
        if (cameraY < 0 ) {cameraY = 0;}
        if (cameraY > 2200)  {cameraY = 2200;}

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.setFill(Color.GREY);

        gc.fillRect(0, 0, 1200, 800);


        for (PlayerPositionMessage player :  players.values()) {
            double hue = player.getPlayerId() * 137.5 % 360;
            gc.setFill(Color.hsb(hue, 1f, 1f));

            double screenX = player.getX() - cameraX;
            double screenY = player.getY() - cameraY;

            if (screenX < 0 | screenX > 1200) {
                continue;
            }
            if (screenY < 0 | screenY > 800) {
                continue;
            }
            double playerSize = 30;
            double rangeRadius = 150;

            double centerX = screenX + playerSize / 2;
            double centerY = screenY + playerSize / 2;

            gc.fillOval(screenX, screenY,playerSize,playerSize);


            gc.setLineWidth(1);
            gc.setStroke(Color.BLACK);
            gc.setFill(Color.TRANSPARENT);
            gc.fillOval(centerX - rangeRadius, centerY - rangeRadius, rangeRadius * 2, rangeRadius * 2);
            gc.strokeOval(centerX - rangeRadius, centerY - rangeRadius, rangeRadius * 2, rangeRadius * 2);

            gc.setFont(Font.font(12));
            gc.setFill(Color.BLACK);
            gc.fillText(player.getPlayerName(), screenX - 10, screenY - 10);

            gc.setFill(Color.GREEN);
            gc.fillRect(screenX , screenY + 35, player.getPlayerHealth() * 0.3, 10);

        }
    }

    private void sendAttackToServer(int dmg) {
        PlayerPositionMessage msg = new PlayerPositionMessage();
        msg.setPlayerId(myPlayerID);
        msg.setDamage(dmg);
        msg.setMessageType(MessageType.ATTACK);

        webSocketClient.sendMessage(msg);
    }



    public void cleanup() {
        if (webSocketClient != null) {
            webSocketClient.sendLeaveMessage();
        }
    }
}
