# MazeRunner - Core Gameplay (`core/src`)

This is the core gameplay part of MazeRunner inside `core/src/main/java/com/mazerunner/`.
It handles the maze grid, map validation, player movement, enemies, traps, collisions, collectables, power-ups, score, game states, and an event bus.

Everything in `com.mazerunner` is written in plain Java using grid coordinates `(x, y)` instead of screen pixels, so other parts of the project (map loading, AI, graphics, audio, menus, HUD) can plug into it without changing the core rules.

---

## Folder Structure (`core/src`)

```text
core/src/
├── main/java/com/mazerunner/
│   ├── collision/
│   │   └── CollisionResolver.java    # Item pickups, trap/enemy damage, hit cooldown, exit win check
│   ├── core/
│   │   ├── GameState.java            # RUNNING, PAUSED, VICTORY, GAME_OVER, MENU
│   │   └── GameWorld.java            # Main game loop (tick), score, damage, pause/resume/reset
│   ├── entities/
│   │   ├── Collidable.java           # Interface for checking same-cell collisions
│   │   ├── Collectable.java          # Interface for items the player can pick up
│   │   ├── Damaging.java             # Interface for objects that hurt the player
│   │   ├── GameObject.java           # Base class for all maze objects (id, x, y)
│   │   ├── Player.java               # Lives, keys, walk/sprint speed, shield & speed boost timers
│   │   ├── Enemy.java                # Moving enemy (currently a simple patrol, ready for full AI)
│   │   ├── Entry.java                # Starting cell for the player (must be 1 per map)
│   │   ├── Exit.java                 # Border exit cell (blocked until player picks up a key)
│   │   ├── FreePath.java             # Normal walkable cell
│   │   ├── Wall.java                 # Solid wall cell
│   │   ├── Trap.java                 # Static hazard tile (removes 1 life)
│   │   ├── Key.java                  # Unlocks the exit (+100 points)
│   │   ├── ExtraLife.java            # Gives +1 life up to 5 (+150 points, or +300 if already full)
│   │   └── PowerUp.java              # Temporary SPEED_BOOST or SHIELD (+200 points)
│   ├── events/
│   │   ├── EventBus.java             # Publish/subscribe bus for notifying UI/audio
│   │   └── GameEvent.java            # Event interface + 8 event record types
│   ├── input/
│   │   └── Direction.java            # UP(0,1), DOWN(0,-1), LEFT(-1,0), RIGHT(1,0)
│   ├── items/
│   │   └── PowerUpType.java          # SPEED_BOOST, SHIELD
│   └── map/
│       ├── GridPosition.java         # Simple (x, y) record with move(Direction)
│       ├── GridMap.java              # 2D grid of tiles, enemy list, and map validation
│       └── ObjectFactory.java        # Creates maze objects from map IDs 0-5
└── test/java/com/mazerunner/
    └── CoreGameplaySelfTest.java     # Standalone smoke tests for the core rules
```

---

## UML Diagrams

- PlantUML file: [CoreGameplay_UML.puml](CoreGameplay_UML.puml)
- Text / ASCII version: [CoreGameplay_UML.txt](CoreGameplay_UML.txt)

Here is the class diagram directly in Markdown (Mermaid):

```mermaid
classDiagram
  direction TB

  namespace core {
    class GameState {
      <<enum>>
      RUNNING
      PAUSED
      VICTORY
      GAME_OVER
      MENU
    }

    class GameWorld {
      -GridMap map
      -Player player
      -EventBus eventBus
      -CollisionResolver collisionResolver
      -List~GameObject~ collectables
      -List~GameObject~ collectableTemplates
      -GameState state
      -int score
      -float elapsedSeconds
      +tick(float deltaSeconds) void
      +setMovement(Direction direction, boolean sprint) void
      +pause() void
      +resume() void
      +returnToMenu() void
      +resetForNewGame() void
      +addCollectable(GameObject item) void
      +canPlayerEnter(GridPosition position) boolean
      +canEnemyEnter(GridPosition position, Enemy movingEnemy) boolean
      +addScore(int points, String reason) void
      +damagePlayer(Damaging hazard, GridPosition position, String sourceName) void
      +finishVictory() void
    }
  }

  namespace collision {
    class CollisionResolver {
      +float DEFAULT_HIT_COOLDOWN_SECONDS
      -float hitCooldownSeconds
      -float cooldownRemaining
      -Set~Long~ activeDamageContacts
      +update(float deltaSeconds) void
      +resolve(GameWorld world) void
      +reset() void
      +getCooldownRemaining() float
    }
  }

  namespace map {
    class GridPosition {
      <<record>>
      +int x
      +int y
      +move(Direction direction) GridPosition
    }

    class GridMap {
      -int width
      -int height
      -GameObject[][] cells
      -List~Enemy~ enemies
      +setTile(GameObject object) void
      +getTileAt(int x, int y) GameObject
      +getEnemies() List~Enemy~
      +findEntry() Entry
      +getExits() List~Exit~
      +findNearestExit(GridPosition from) Exit
      +validate() void
      +isWalkable(int x, int y, boolean hasKey) boolean
      +resetEnemies() void
    }

    class ObjectFactory {
      +int FREE_PATH_ID
      +int WALL_ID
      +int ENTRY_ID
      +int EXIT_ID
      +int TRAP_ID
      +int ENEMY_ID
      +create(int typeId, int x, int y) GameObject
    }
  }

  namespace entities {
    class Collidable {
      <<interface>>
      +collidesWith(GameObject other) boolean
    }

    class Damaging {
      <<interface>>
      +getDamageAmount() int
    }

    class Collectable {
      <<interface>>
      +collect(GameWorld world, Player player) void
    }

    class GameObject {
      <<abstract>>
      -long id
      -int x
      -int y
      +getPosition() GridPosition
      +blocksMovement(boolean hasKey) boolean
      +update(float deltaSeconds, GameWorld world) void
      +collidesWith(GameObject other) boolean
    }

    class Player {
      +int DEFAULT_LIVES
      +int MAX_LIVES
      +float NORMAL_SPEED
      +float SPRINT_SPEED
      +float POWERED_SPEED_MULTIPLIER
      -int lives
      -int keyCount
      -Direction requestedDirection
      -float speedBoostTime
      -float shieldTime
      +hasKey() boolean
      +hasShield() boolean
      +loseLives(int amount) int
      +addLife() boolean
      +addKey() void
      +activateSpeedBoost(float durationSeconds) void
      +activateShield(float durationSeconds) void
    }

    class Enemy {
      +getDamageAmount() int
      +update(float deltaSeconds, GameWorld world) void
      +resetForNewGame() void
    }

    class Wall {
      +blocksMovement(boolean hasKey) boolean
    }

    class FreePath
    class Entry

    class Exit {
      +blocksMovement(boolean hasKey) boolean
    }

    class Trap {
      +getDamageAmount() int
    }

    class Key {
      +collect(GameWorld world, Player player) void
    }

    class ExtraLife {
      +collect(GameWorld world, Player player) void
    }

    class PowerUp {
      -PowerUpType type
      -float durationSeconds
      +collect(GameWorld world, Player player) void
    }
  }

  namespace events {
    class EventBus {
      -Map~Class, CopyOnWriteArrayList~ listeners
      +subscribe(Class~E~ eventType, Consumer~E~ listener) Subscription
      +publish(Object event) void
      +clear() void
    }

    class Subscription {
      <<interface>>
      +close() void
    }

    class GameEvent {
      <<interface>>
    }

    class LifeLostEvent {
      <<record>>
      +String source
      +GridPosition position
      +int livesRemaining
      +int livesLost
    }

    class KeyCollectedEvent {
      <<record>>
      +GridPosition position
      +int totalKeys
    }

    class ScoreChangedEvent {
      <<record>>
      +int newScore
      +int pointsAdded
      +String reason
    }

    class ExtraLifeCollectedEvent {
      <<record>>
      +int livesNow
      +boolean lifeWasAdded
    }

    class PowerUpCollectedEvent {
      <<record>>
      +PowerUpType type
      +float durationSeconds
    }

    class VictoryEvent {
      <<record>>
      +int finalScore
      +float completionTimeSeconds
      +int livesRemaining
    }

    class GameOverEvent {
      <<record>>
      +int finalScore
    }

    class NewGameStartedEvent {
      <<record>>
    }
  }

  namespace input {
    class Direction {
      <<enum>>
      UP
      DOWN
      LEFT
      RIGHT
      +getDx() int
      +getDy() int
    }
  }

  namespace items {
    class PowerUpType {
      <<enum>>
      SPEED_BOOST
      SHIELD
    }
  }

  Collidable <|.. GameObject
  GameObject <|-- Player
  GameObject <|-- Enemy
  GameObject <|-- Wall
  GameObject <|-- FreePath
  GameObject <|-- Entry
  GameObject <|-- Exit
  GameObject <|-- Trap
  GameObject <|-- Key
  GameObject <|-- ExtraLife
  GameObject <|-- PowerUp

  Damaging <|.. Trap
  Damaging <|.. Enemy
  Collectable <|.. Key
  Collectable <|.. ExtraLife
  Collectable <|.. PowerUp

  EventBus +-- Subscription
  GameEvent <|.. LifeLostEvent
  GameEvent <|.. KeyCollectedEvent
  GameEvent <|.. ScoreChangedEvent
  GameEvent <|.. ExtraLifeCollectedEvent
  GameEvent <|.. PowerUpCollectedEvent
  GameEvent <|.. VictoryEvent
  GameEvent <|.. GameOverEvent
  GameEvent <|.. NewGameStartedEvent

  GameWorld *-- Player
  GameWorld *-- CollisionResolver
  GameWorld o-- GridMap
  GameWorld o-- EventBus
  GameWorld o-- Collectable
  GameWorld --> GameState

  GridMap *-- GameObject
  GridMap o-- Enemy

  Player --> Direction
  PowerUp --> PowerUpType
  ObjectFactory ..> GameObject : creates IDs 0-5
  CollisionResolver ..> GameWorld : resolves
  EventBus ..> GameEvent : dispatches
```

---

## How the Core Gameplay Works

### Game States (`GameState`)

A `GameWorld` starts in `RUNNING`. Whenever the state is anything other than `RUNNING`, `tick(...)` does nothing so timers, enemies, and movement stay paused.

- `RUNNING`: normal gameplay. Can go to `PAUSED` via `pause()`, `VICTORY` via `finishVictory()`, or `GAME_OVER` when lives reach `0`.
- `PAUSED`: movement input is cleared so the player doesn't keep moving on resume. Call `resume()` to go back to `RUNNING`, or `returnToMenu()` to go to `MENU`.
- `VICTORY` / `GAME_OVER`: end states. Call `returnToMenu()` or `resetForNewGame()`.
- `MENU`: idle state until `resetForNewGame()` is called.

Note: `returnToMenu()` only works from `PAUSED`, `VICTORY`, or `GAME_OVER` (not directly from `RUNNING`).

### What Happens Each `GameWorld.tick(deltaSeconds)`

1. Checks that `deltaSeconds` is finite and `>= 0` (throws `IllegalArgumentException` if not). Returns early if `deltaSeconds == 0` or state is not `RUNNING`.
2. Adds `deltaSeconds` to `elapsedSeconds`, counts down player power-up timers (`updateEffects`), and counts down the hit cooldown (`collisionResolver.update`).
3. Updates every enemy in `map.getEnemies()` and runs `collisionResolver.resolve(this)`. If an enemy causes `GAME_OVER`, the tick stops there.
4. Adds `deltaSeconds` to the player's movement accumulator and steps cell-by-cell (capped at 32 steps per tick so a lag spike can't teleport the player):
   - Checks `canPlayerEnter(next)`. If blocked, clears leftover movement progress and stops.
   - Moves the player to `next` and immediately runs `collisionResolver.resolve(this)`.

### Map Grid, Coordinates & `ObjectFactory` IDs

- `GridMap(width, height)` starts filled with `FreePath` tiles.
- Directions use standard math axes: `UP(0, +1)`, `DOWN(0, -1)`, `LEFT(-1, 0)`, `RIGHT(+1, 0)`.
- When you call `map.setTile(new Enemy(x, y))`, `GridMap` puts the enemy into its separate `enemies` list and leaves a `FreePath(x, y)` on the grid underneath.
- `map.validate()` checks four rules and throws `IllegalStateException` if any fail:
  1. Exactly 1 `Entry` tile.
  2. At least 1 `Exit` tile.
  3. Every `Exit` is on the outer border of the map.
  4. At least one `Exit` is reachable from `Entry` (checked with BFS).

`ObjectFactory.create(typeId, x, y)` maps IDs `0-5` (any other ID throws `IllegalArgumentException`):

| ID | Constant | Class | Blocks Movement? |
|---|---|---|---|
| `0` | `FREE_PATH_ID` | `FreePath` | No |
| `1` | `WALL_ID` | `Wall` | Always |
| `2` | `ENTRY_ID` | `Entry` | No |
| `3` | `EXIT_ID` | `Exit` | Yes until player has a key |
| `4` | `TRAP_ID` | `Trap` | No (1 damage on entry) |
| `5` | `ENEMY_ID` | `Enemy` | No (patrols, 1 damage on entry) |

### Movement, Lives, Collectables & Score

- **Speed:** `4.0` cells/sec walking (`NORMAL_SPEED`), `7.0` cells/sec sprinting (`SPRINT_SPEED`), multiplied by `1.5x` while `SPEED_BOOST` is active.
- **Lives & Damage:** Player starts with `3` lives (`DEFAULT_LIVES`), max `5` (`MAX_LIVES`). `Trap` and `Enemy` deal `1` damage on **contact entry** with a `1.0s` hit cooldown. Standing still on the same trap will not keep draining lives; you have to leave and step back on after the cooldown. Active `SHIELD` blocks damage.
- **Collectables & Points:**
  - `Key`: unlocks the exit, `+100` points.
  - `ExtraLife`: `+1` life and `+150` points (if already at `5` lives, converts to `+300` points instead).
  - `PowerUp`: activates `SPEED_BOOST` or `SHIELD` for its duration, `+200` points.
  - **Victory bonus:** `500 + (livesRemaining * 100) + max(0, 600 - ceil(elapsedSeconds)) * 5`.

---

## How Teammates Can Connect to This Module

### Map Loader (Member 2)

```java
ObjectFactory factory = new ObjectFactory();
GridMap map = new GridMap(width, height);

// For each cell from the .properties map file:
map.setTile(factory.create(typeId, x, y));

// Check map rules before starting the game:
map.validate();

GameWorld world = new GameWorld(map);
world.addCollectable(new Key(keyX, keyY));
world.addCollectable(new ExtraLife(lifeX, lifeY));
world.addCollectable(new PowerUp(boostX, boostY, PowerUpType.SPEED_BOOST, 5.0f));
world.addCollectable(new PowerUp(shieldX, shieldY, PowerUpType.SHIELD, 5.0f));
```

Heads-up: `resetForNewGame()` restores collectables using `copyCollectable(...)`, which currently supports `Key`, `ExtraLife`, and `PowerUp`. If we add a new `Collectable` class later, we should add it there too.

### Obstacles & Enemy AI (Member 3)

- `Enemy.update(float deltaSeconds, GameWorld world)` currently runs a basic 4-direction patrol (`RIGHT -> DOWN -> LEFT -> UP`, `0.8s` per step) as a placeholder for the full Patrol / Attack / Evade / Retreat state machine.
- Use `world.canEnemyEnter(nextPos, this)` before moving an enemy so it respects walls, map bounds, and doesn't overlap other enemies.
- Useful helpers already available: `world.getPlayer().getPosition()`, `world.getPlayer().hasShield()`, `world.getMap().isWalkable(x, y, hasKey)`, `world.getMap().findNearestExit(from)`, `world.getMap().getEnemies()`.

### Graphics, Audio, Screens & HUD (Members 4 & 5)

Core gameplay never calls UI or audio classes directly. Instead, subscribe to events on `world.getEventBus()`:

```java
EventBus bus = world.getEventBus();

EventBus.Subscription scoreSub = bus.subscribe(GameEvent.ScoreChangedEvent.class, event -> {
    // event.newScore(), event.pointsAdded(), event.reason()
});

EventBus.Subscription hitSub = bus.subscribe(GameEvent.LifeLostEvent.class, event -> {
    // play hit sound / flash screen
});

// Remember to unsubscribe when closing a screen:
scoreSub.close();
hitSub.close();
```

Events published by `GameWorld` and collectables (`GameEvent.*`):

| Event | Fields | Fired When |
|---|---|---|
| `LifeLostEvent` | `source, position, livesRemaining, livesLost` | Player takes damage |
| `KeyCollectedEvent` | `position, totalKeys` | Player picks up a key |
| `ScoreChangedEvent` | `newScore, pointsAdded, reason` | Score increases |
| `ExtraLifeCollectedEvent` | `livesNow, lifeWasAdded` | Player picks up an extra life |
| `PowerUpCollectedEvent` | `type, durationSeconds` | Player picks up a power-up |
| `VictoryEvent` | `finalScore, completionTimeSeconds, livesRemaining` | Player reaches unlocked exit |
| `GameOverEvent` | `finalScore` | Player reaches `0` lives |
| `NewGameStartedEvent` | *(none)* | `resetForNewGame()` is called |

For the game loop and HUD polling:
- Pass input each frame with `world.setMovement(directionOrNull, sprintHeld)` and update with `world.tick(Math.min(deltaSeconds, 0.1f))`.
- Use `world.pause()`, `world.resume()`, `world.returnToMenu()`, and `world.resetForNewGame()` for screen transitions.
- Read HUD values from `world.getState()`, `world.getScore()`, `world.getElapsedSeconds()`, `world.getPlayer()` (`getLives()`, `hasKey()`, `getShieldTime()`, `getSpeedBoostTime()`), and `world.getMap().findNearestExit(player.getPosition())` for the exit arrow.

---

## Running the Tests

Run the core gameplay smoke test (`core/src/test/java/com/mazerunner/CoreGameplaySelfTest.java`):

```bash
./gradlew coreGameplaySmokeTest
```

If everything works, it prints:

```text
All core gameplay checks passed.
```

To build the whole project:

```bash
./gradlew build
```

### What `CoreGameplaySelfTest` Checks
1. `ObjectFactory` creates the right classes for IDs `0-5` and rejects unknown IDs.
2. `Wall` blocks the player.
3. Locked `Exit` blocks the player; picking up `Key` unlocks it and reaching it triggers `VICTORY` + score.
4. `Trap` removes 1 life on entry, does not keep draining lives while standing on it, and hurts again if you step off and back on after cooldown.
5. Three logical hits bring lives to `0` and trigger `GAME_OVER`.
6. `ExtraLife` adds a life and `PowerUp(SHIELD)` activates the shield.
7. `resetForNewGame()` resets state, position, lives, keys, score, and collectables.
8. `EventBus` delivers events to active listeners and stops after `Subscription.close()`.
