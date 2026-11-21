# `GameWorld.java`

- **Package:** `com.mazerunner.core`
- **File:** `core/src/main/java/com/mazerunner/core/GameWorld.java`
- **Type:** `public final class GameWorld`

## What This File Does

`GameWorld` is the main controller of a running MazeRunner game. It connects the `GridMap`, `Player`, `CollisionResolver`, `EventBus`, collectable items, score, timer, and `GameState`. It does not depend on rendering, keyboard classes, or sound—other modules drive it by calling `setMovement(...)` and `tick(deltaSeconds)`.

## Constants & Fields

- **Scoring constants:**
  - `VICTORY_BASE_SCORE = 500` (base points for winning)
  - `POINTS_PER_REMAINING_LIFE = 100` (points per remaining life on victory)
  - `MAX_TIME_BONUS = 600` (600-second threshold for the time bonus)
  - `POINTS_PER_REMAINING_SECOND = 5` (points per remaining second under 600s)
- **Main fields:**
  - `map : GridMap` — the validated maze grid
  - `player : Player` — the player character (spawned at the map's `Entry`)
  - `startingLives : int` — starting lives saved so `resetForNewGame()` can restore them
  - `eventBus : EventBus` — publishes gameplay events to HUD/audio/achievements
  - `collisionResolver : CollisionResolver` — handles item pickups, hazard hits, cooldown, and exit victory
  - `collectables : List<GameObject>` — active items still on the map
  - `collectableTemplates : List<GameObject>` — saved copies of items so `resetForNewGame()` can respawn them
  - `state : GameState` — current game state (starts at `RUNNING`)
  - `score : int` — current score
  - `elapsedSeconds : float` — active play time (does not increase while paused)

## Constructors

- `GameWorld(GridMap map)` — starts a game with `Player.DEFAULT_LIVES` (`3`) and a new `EventBus`.
- `GameWorld(GridMap map, int startingLives, EventBus eventBus)` — validates `map` and `eventBus`, calls `map.validate()`, finds the `Entry` cell, and spawns the `Player` there.

## Key Methods

### Getters
- `getMap()` — returns the `GridMap`.
- `getPlayer()` — returns the `Player`.
- `getEventBus()` — returns the `EventBus`.
- `getCollisionResolver()` — returns the `CollisionResolver`.
- `getState()` — returns the current `GameState`.
- `getScore()` — returns the current score.
- `getElapsedSeconds()` — returns elapsed active game time in seconds.
- `getCollectables()` — returns an unmodifiable list of active collectables on the map.

### Collectables
- `addCollectable(GameObject item)` — adds a `Key`, `ExtraLife`, or `PowerUp` inside the map bounds and saves a template clone for resets.
- `removeCollectable(GameObject item)` — removes an item from the active list after pickup.

### Input & Lifecycle
- `setMovement(Direction direction, boolean sprint)` — sets the player's movement direction (`null` when no key is held) and sprint flag while `RUNNING`.
- `pause()` — switches `RUNNING` -> `PAUSED` and clears held movement.
- `resume()` — switches `PAUSED` -> `RUNNING`.
- `returnToMenu()` — switches `PAUSED`, `VICTORY`, or `GAME_OVER` -> `MENU` and clears movement.
- `resetForNewGame()` — resets player to `Entry`, resets enemies, respawns all collectables, resets score/timer/cooldown, sets state to `RUNNING`, and publishes `NewGameStartedEvent`.

### Main Update Loop (`tick`)
- `tick(float deltaSeconds)` — called once per frame:
  1. Rejects negative `deltaSeconds`; does nothing if `deltaSeconds == 0` or `state != RUNNING`.
  2. Advances `elapsedSeconds`, updates player power-up timers (`player.updateEffects`), and updates hit cooldown (`collisionResolver.update`).
  3. Updates all enemies (`enemy.update(deltaSeconds, this)`) and runs `collisionResolver.resolve(this)`. Stops early if the player lost all lives.
  4. Adds movement time to the player and moves cell-by-cell while `player.canMoveOneCell()`, checking `canPlayerEnter(next)` and resolving collisions after every step.

### Movement Checks, Score, Damage & Victory
- `canPlayerEnter(GridPosition position)` — `true` if inside the map and not blocked (taking `player.hasKey()` into account for `Exit`).
- `canEnemyEnter(GridPosition position, Enemy movingEnemy)` — `true` if inside the map, not blocked (`tile.blocksMovement(false)` so enemies never enter walls or exits), and no other enemy is on that cell.
- `addScore(int points, String reason)` — adds points and publishes `ScoreChangedEvent`.
- `damagePlayer(Damaging hazard, GridPosition position, String sourceName)` — removes lives (unless shielded), publishes `LifeLostEvent`, and switches to `GAME_OVER` + publishes `GameOverEvent` if lives hit `0`.
- `finishVictory()` — switches to `VICTORY`, awards `500 + timeBonus + lifeBonus`, and publishes `VictoryEvent`.

## What Teammates Need to Know

- **Member 2 (Data/Map/Save):** Create `new GameWorld(map)` after parsing and validating a `.properties` map, and register pickups with `world.addCollectable(...)`.
- **Member 3 (Obstacles & AI):** Call `world.canEnemyEnter(nextPos, this)` inside `Enemy.update(...)` to check if a step is legal.
- **Member 4 (Graphics & Audio) & Member 5 (Screens & HUD):** Subscribe to `world.getEventBus()`, pass input via `world.setMovement(dir, sprint)`, run `world.tick(delta)`, and read state/HUD values via `getState()`, `getScore()`, `getElapsedSeconds()`, `getPlayer()`, `getMap()`, and `getCollectables()`.
