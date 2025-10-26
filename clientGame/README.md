# Game Client

JavaFX desktop client for multiplayer combat game. Connects to server and renders game in real-time.

## Running

1. Make sure server is running on `localhost:8080`
2. Open project in IntelliJ
3. Run `ClientApplicationRun.java`
4. Launch multiple instances to test multiplayer

## Controls

| Key | Action |
|-----|--------|
| W | Move up |
| S | Move down |
| A | Move left |
| D | Move right |
| B | Attack |

## What it does

- Connects to server WebSocket
- Renders all players on canvas in real-time
- Shows health bars below players
- Camera follows your player
- Attack range circle (150px radius)
- Each player has unique color based on their ID

## How it works

**On startup:**
1. Generates random player ID (1-100)
2. Connects to `ws://localhost:8080/game-websocket`
3. Sends JOIN message with starting position (1500, 1500)

**During gameplay:**
- Press W/A/S/D → updates local position → sends MOVE to server
- Press B → sends ATTACK to server
- Receives updates from server → updates other players → redraws canvas
- Camera follows local player around the map

**Rendering:**
- Each player = colored circle (hue based on ID)
- Health bar below player (green, width based on HP)
- Player name above circle
- Black circle shows attack range (150px)
- Camera viewport culling (only draws visible players)

## Tech used

- JavaFX 21 (Canvas, FXML)
- Spring WebSocket Client (STOMP)
- Jackson (JSON serialization)
- HashMap for local player state

## Project structure

clientgame/ <br>
├── controller/<br>
│ └── GameController.java *(UI controller + message handling)*<br>
├── network/<br>
│ └── WebSocketClient.java *(STOMP client connection)*<br>
├── model/<br>
│ ├── PlayerPositionMessage.java<br>
│ └── MessageType.java<br>
└── resources/<br>
└── game.fxml   *(JavaFX layout)*

## Message flow

User presses W<br>
↓<br>
handleKeyPressed()<br>
↓<br>
Update local position<br>
↓<br>
sendMoveToServer()<br>
↓<br>
WebSocketClient sends to server<br>
↓<br>
Server broadcasts to all clients<br>
↓<br>
handleIncomingMessages()<br>
↓<br>
Update other player's position<br>
↓<br>
redrawCanvas()


## What I learned

- JavaFX Canvas rendering and event handling
- WebSocket client with STOMP protocol
- Camera system implementation (viewport follows player)
- Real-time state synchronization
- Using `Platform.runLater()` for thread-safe UI updates

## Known issues

- Player ID collision possible (random 1-100)
- No interpolation (movement looks choppy)
- Attack range circle shown but not used for targeting
- Camera bounds hardcoded (not based on world size)
- Missing reconnection logic if server disconnects

These are things I'd fix if I continue working on this.