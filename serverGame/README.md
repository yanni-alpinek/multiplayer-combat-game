# Game Server

Spring Boot backend for multiplayer combat game. Handles all player connections and game logic.

## Running

Open project in IntelliJ and run `ServerGameApplication.java`

Server starts on `localhost:8080`

## What it does

- Manages all connected players through WebSocket
- Calculates damage when players attack (server-authoritative)
- Broadcasts position updates to all clients
- Provides REST API to check who's online

## REST API

**Get online players:**

- GET `/api/game/players` *(Returns JSON with all connected players)*

Example response:


{<br>
&nbsp; "playerId": 31,<br>
&nbsp; "playerName": "Player 31",<br>
&nbsp;"playerHealth": 100,<br>
&nbsp;"targetId": null,<br>
&nbsp; "damage": 0,<br>
&nbsp; "x": 1500,<br>
&nbsp; "y": 1500,<br>
&nbsp; "messageType": "JOIN"<br>
}

## How it works

**WebSocket connection:**
- Client connects to `ws://localhost:8080/game-websocket`
- Client subscribes to `/topic/game` (receives all updates)
- Client sends messages to `/app/player.join` or `/app/player.action` or `/app/player.leave`

**Message types:**
- `JOIN` - player joins the game
- `MOVE` - player moved
- `ATTACK` - player attacked (server calculates damage)
- `HEALTH` - player's HP changed
- `LEAVE` - player disconnected

**Server is authoritative:**
- When player attacks, server decides who gets hit and how much damage
- Clients can't fake HP or damage
- All game state stored in `ConcurrentHashMap`

## Tech used

- Spring Boot
- Spring WebSocket (STOMP protocol)
- Spring Web (REST)
- Lombok
- ConcurrentHashMap for thread-safe player storage

## Project structure

servergame/ <br>
├── controller/<br>
│ └── GameController.java  *(WebSocket message handlers)* <br>
├── config/<br>
│ ├── WebSocketConfig.java *(WebSocket setup)* <br>
│ └── WebController.java *(REST API)* <br>
└── model/<br>
├── PlayerPositionMessage.java<br>
└── MessageType.java<br>

## What I learned

- Real-time communication with WebSocket
- STOMP protocol for pub/sub messaging
- Thread safety with multiple connections (ConcurrentHashMap)
- Server-authoritative game design
- Combining WebSocket + REST in one application

## Known issues

- Player IDs are random 1-100 (can collide)
- Attack hits everyone (no range checking)
- No authentication
- Console logging instead of proper logger
- Business logic in controller (should be in service layer)

These are things I'd fix if I continue working on this.