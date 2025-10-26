module com.in.clientgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires spring.messaging;
    requires spring.websocket;
    requires spring.core;
    requires com.fasterxml.jackson.databind;
    requires tyrus.standalone.client.jdk;
    requires java.desktop;
    requires spring.context;

    opens com.in.clientgame.controller to javafx.fxml;
    opens com.in.clientgame.model to com.fasterxml.jackson.databind;

    exports com.in.clientgame;
}