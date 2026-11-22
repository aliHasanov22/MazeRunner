# `GameWorld.java`

- **Package:** `com.mazerunner.core`
- **File:** `core/src/main/java/com/mazerunner/core/GameWorld.java`
- **Type:** `public final class GameWorld`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`GameWorld` is the central controller of a single MazeRunner session. It owns the `Player`, `EventBus`, `CollisionResolver`, active collectables, `GameState`, score, and timer, and coordinates with `GridMap`. It contains no rendering, UI, or audio code.

## Constants

| Constant | Value | Meaning |
|---|---:|---|
| `VICTORY_SCORE` | `500` | Base points awarded on winning. |
| `POINTS_PER_LIFE` | `100` | Bonus points for each remaining life on victory. |
| `MAX_TIME_BONUS` | `600` | Completion-time ceiling (seconds) for calculating the time bonus. |
| `POINTS_PER_SECOND` | `5` | Bonus points for each remaining second under `600`. |

## Fields

- `map : GridMap` — the maze grid and enemies.
- `player : Player` — spawned at `map.findEntry()` with `Player.DEFAULT_LIVES` (`3`).
- `eventBus : EventBus` — publish/subscribe bus for notifying external modules.
- `collisionResolver : CollisionResolver` — resolves item pickups, hazard hits, cooldown, and exit victory.
- `collectables : List<GameObject>` — items currently still on the map.
- `startingCollectables : List<GameObject>` — initial items saved so `resetForNewGame()` can put them back.
- `state : GameState` — starts as `GameState.RUNNING`.
- `score : int` — current score (`0` at start).
- `elapsedSeconds : float` — played time in seconds (excludes paused/menu/finished time).

## Constructor

- `public GameWorld(GridMap map)` — stores `map`, locates the `Entry` cell with `map.findEntry()`, and creates `player` at `(entry.getX(), entry.getY())` with `3` lives.

## Methods

### Getters
- `getMap() : GridMap` — returns the map.
- `getPlayer() : Player` — returns the player.
- `getEventBus() : EventBus` — returns the event bus.
- `getState() : GameState` — returns the current `GameState`.
- `getScore() : int` — returns the current score.
- `getElapsedSeconds() : float` — returns elapsed active play time in seconds.
- `getCollectables() : List<GameObject>` — returns an unmodifiable list of items still on the map.

### Collectables
- `addCollectable(GameObject item) : void` — checks `item instanceof Collectable` (throws `IllegalArgumentException` otherwise), and adds `item` to both `collectables` and `startingCollectables`.
- `removeCollectable(GameObject item) : void` — removes `item` from active `collectables` after pickup.

### Input & Lifecycle
- `setMovement(Direction direction, boolean sprint) : void` — forwards movement direction (`null` to stop) and sprint flag to `player` only while `state == GameState.RUNNING`.
- `pause() : void` — if `RUNNING`, sets state to `PAUSED` and calls `player.setMovement(null, false)`.
- `resume() : void` — if `PAUSED`, sets state back to `RUNNING`.
- `returnToMenu() : void` — if `state != RUNNING`, sets state to `MENU` and clears movement.
- `resetForNewGame() : void` — resets player to `Entry` with `3` lives, resets enemies (`map.resetEnemies()`), restores `startingCollectables`, sets `score = 0`, `elapsedSeconds = 0`, resets `collisionResolver`, sets `state = RUNNING`, and publishes `GameEvent.NewGameStartedEvent`.

### Simulation Loop (`tick`)
- `tick(float deltaSeconds) : void` — does nothing unless `state == RUNNING` and `deltaSeconds > 0`:
  1. Adds `deltaSeconds` to `elapsedSeconds`.
  2. Updates player power-up timers (`player.updateEffects`) and collision cooldown (`collisionResolver.update`).
  3. Updates every enemy (`enemy.update(deltaSeconds, this)`) and calls `collisionResolver.resolve(this)`.
  4. Adds movement time to the player (`player.addMovementTime(deltaSeconds)`) and while `state == RUNNING && player.canMoveOneCell()`:
     - Calculates `next = player.getPosition().move(player.getDirection())`.
     - If `!canPlayerEnter(next)`, calls `player.clearMovementTime()` and breaks.
     - Otherwise calls `player.stepTo(next)` and `collisionResolver.resolve(this)`.

### Movement Rules, Score, Damage & Victory
- `canPlayerEnter(GridPosition position) : boolean` — returns `true` if `tile != null && !tile.blocksMovement(player.hasKey())`.
- `canEnemyEnter(GridPosition position, Enemy movingEnemy) : boolean` — returns `true` if `tile != null`, `!tile.blocksMovement(false)` (walls and exits block enemies), and no other enemy occupies `position`.
- `addScore(int points, String reason) : void` — increments `score += points` and publishes `GameEvent.ScoreChangedEvent(score, points, reason)`.
- `damagePlayer(Damaging hazard, GridPosition position, String sourceName) : void` — ignored unless `RUNNING` and `!player.hasShield()`. Removes `hazard.getDamageAmount()` lives, publishes `GameEvent.LifeLostEvent`, and if `player.getLives() <= 0`, switches to `GAME_OVER`, stops movement, and publishes `GameEvent.GameOverEvent`.
- `finishVictory() : void` — ignored unless `RUNNING`. Sets `state = VICTORY`, stops movement, adds `500 + max(0, 600 - (int) elapsedSeconds) * 5 + lives * 100` (`"Victory bonus"`), and publishes `GameEvent.VictoryEvent`.

## How Teammates Use It

- **Member 2 (Map Loader & Save):** Build a `GridMap`, create `new GameWorld(map)`, and add pickups with `addCollectable(...)`.
- **Member 3 (Enemy AI):** Call `world.canEnemyEnter(nextPos, this)` before moving enemies.
- **Member 4 (Graphics & Audio) & Member 5 (Screens & HUD):** Drive the game with `setMovement(...)` and `tick(delta)`, subscribe to `getEventBus()`, and read HUD/render data from `getMap()`, `getPlayer()`, `getCollectables()`, `getScore()`, `getElapsedSeconds()`, and `getState()`.
