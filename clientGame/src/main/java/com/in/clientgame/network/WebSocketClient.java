package com.in.clientgame.network;
import com.in.clientgame.model.MessageType;
import com.in.clientgame.model.PlayerPositionMessage;
import javafx.application.Platform;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public class WebSocketClient {

    private StompSession stompSession;
    private Long player;
    private Consumer<PlayerPositionMessage> messageHandler;
    private PlayerPositionMessage playerPositionMessage;

    public WebSocketClient(Long player, Consumer<PlayerPositionMessage> messageHandler, PlayerPositionMessage playerPositionMessage) {
        this.player = player;
        this.messageHandler = messageHandler;
        this.playerPositionMessage = playerPositionMessage;
    }

    public void connect(String serverURL) {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(standardWebSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.connect(serverURL, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                WebSocketClient.this.stompSession = session;
                session.subscribe("/topic/game", new StompFrameHandler() {

                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return PlayerPositionMessage.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        PlayerPositionMessage playerPositionMessage = (PlayerPositionMessage) payload;
                        Platform.runLater(() -> messageHandler.accept(playerPositionMessage));
                    }
                });
                sendJoinMessage(playerPositionMessage);
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                System.out.println("❌ Błąd połączenia: " + exception.getMessage());
            }
        });
    }

    public void sendMessage(PlayerPositionMessage playerPositionMessage) {
        if (this.stompSession != null && this.stompSession.isConnected()) {
            stompSession.send("/app/player.action", playerPositionMessage);
        }
    }

    public void sendJoinMessage(PlayerPositionMessage msg) {
        if (this.stompSession != null && this.stompSession.isConnected()) {


            playerPositionMessage.setMessageType(MessageType.JOIN);
            stompSession.send("/app/player.join", msg);
        }
    }

    public void sendLeaveMessage() {
        if (this.stompSession != null && this.stompSession.isConnected()) {
            PlayerPositionMessage playerPositionMessage = new PlayerPositionMessage();
            playerPositionMessage.setPlayerId(this.player);
            playerPositionMessage.setMessageType(MessageType.LEAVE);
            stompSession.send("/app/player.leave", playerPositionMessage);
        }
    }
}

