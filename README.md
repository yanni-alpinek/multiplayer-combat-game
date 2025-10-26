# Multiplayer Combat Game

**Real-time multiplayer game built with Spring Boot and JavaFX.** Players move around, attack each other, and see live updates via WebSocket.

---

## Gameplay
*Multiple players in combat with health bars*

![Gameplay with multiple players](gameplay.png)


Players can move freely, attack, and observe others’ movements in real time.

---

## Quick Start

### 1. Start Server

* Open **servergame** in IntelliJ
* Run **ServerGameApplication.java**
* Server starts on `localhost:8080`

### 2. Start Client

* Open **clientgame** in IntelliJ
* Run **ClientApplicationRun.java**
* Launch multiple instances to play together

### 3. Controls

* `W / A / S / D` — Move
* `B` — Attack

---

## How It Works

```
JavaFX Client  ←→  WebSocket (STOMP)  ←→  Spring Boot Server
                         ↓
                       REST API
```

The server is authoritative — it calculates damage and manages all game state. Clients send inputs and render live updates.

---

## Tech Stack

* **Backend:** Spring Boot, WebSocket (STOMP), REST API
* **Client:** JavaFX, Canvas rendering, STOMP client

---

## Features

* Real-time multiplayer (up to 100 players)
* Server-authoritative combat (prevents cheating)
* Health bars and attack range indicators
* Camera system that follows player
* REST API for querying game state

---

## Documentation

- [Game Server](serverGame/README.md) - WebSocket handlers, game logic
- [Game Client](clientGame/README.md) - Graphics, controls

---

## REST API Example

**GET** `/api/game/players`

Returns all online players as JSON:

{<br>
"playerId": 80,<br>
"playerName": "Player 80",<br>
"playerHealth": 64,<br>
"x": 1255,<br>
"y": 1270<br>
},<br>

{<br>
"playerId": 48,<br>
"playerName": "Player 48",<br>
"playerHealth": 100,<br>
"x": 1500,<br>
"y": 1500<br>
},<br>

{<br>
"playerId": 63,<br>
"playerName": "Player 63",<br>
"playerHealth": 79,<br>
"x": 1275,<br>
"y": 1595<br>
}

## What I Learned

* Real-time communication with WebSocket/STOMP
* Server-authoritative game architecture
* Thread-safe state management (`ConcurrentHashMap`)
* JavaFX Canvas rendering and event handling
* Combining WebSocket + REST in one application

---

## Known Issues

* Player IDs are random 1–100 (can collide)
* Attack hits everyone (no targeting)
* No authentication or persistence
* Missing error handling for disconnects

> Built as a learning project to understand real-time networking and game development.

---

## Other Projects

* **Fluid Simulation SPH** — Physics simulation with 1000+ particles
* **Mass-Spring Simulation** - A simple physics simulation of a spring-mass system with a bouncing ball
* **Real-Time Chat** - A real-time chat application using WebSockets for instant messaging
* **ChessApp (WIP)** -  Work In Progress
