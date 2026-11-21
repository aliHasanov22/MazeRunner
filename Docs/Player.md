# `Player.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Player.java`
- **Type:** `public final class Player extends GameObject`

## What This File Does

`Player` stores the player's grid position (inherited from `GameObject`), remaining lives, collected keys, movement direction, sprint state, sub-cell movement progress accumulator, and remaining timers for `SPEED_BOOST` and `SHIELD` power-ups.

## Constants & Fields

- `DEFAULT_LIVES = 3` — default starting lives.
- `MAX_LIVES = 5` — maximum lives a player can hold.
- `NORMAL_SPEED = 4.0f` — walking speed in cells per second.
- `SPRINT_SPEED = 7.0f` — sprinting speed (Shift held) in cells per second.
- `SPEED_BOOST_MULTIPLIER = 1.5f` — multiplier applied when a speed boost power-up is active.
- Private state fields: `lives`, `keyCount`, `requestedDirection`, `sprinting`, `movementProgress`, `speedBoostTime`, `shieldTime`.

## Constructors

- `Player(int x, int y)` — creates a player at `(x, y)` with `DEFAULT_LIVES` (`3`).
- `Player(int x, int y, int startingLives)` — creates a player at `(x, y)` with `startingLives` (must be between `1` and `5`).

## Key Methods

### Getters (Used by HUD, AI, and Renderer)
- `getLives()` — remaining lives (`0..5`).
- `getKeyCount()` — number of keys collected.
- `hasKey()` — `true` if `keyCount > 0` (unlocks `Exit`).
- `hasShield()` — `true` while `shieldTime > 0.0f` (blocks hazard damage).
- `getShieldTime()` — remaining shield time in seconds.
- `getSpeedBoostTime()` — remaining speed boost time in seconds.
- `getRequestedDirection()` — current `Direction` held by the player (or `null`).
- `getPosition()`, `getX()`, `getY()` — inherited from `GameObject`.

### Movement & Timers (Called by `GameWorld`)
- `setMovement(Direction direction, boolean sprint)` — sets held direction and sprint flag; passing `null` stops movement and resets `movementProgress` to `0`.
- `addMovementTime(float deltaSeconds)` — adds `deltaSeconds * speed` to `movementProgress` (`4.0` walk, `7.0` sprint, `* 1.5` if speed-boosted).
- `canMoveOneCell()` — `true` when a direction is held and `movementProgress >= 1.0f`.
- `consumeMovementStep()` — subtracts `1.0f` from `movementProgress` after stepping one cell.
- `clearMovementTime()` — clears `movementProgress = 0.0f` when blocked by a wall or locked exit.
- `moveTo(GridPosition position)` — updates the player's `(x, y)` coordinates.
- `updateEffects(float deltaSeconds)` — counts down `speedBoostTime` and `shieldTime`.

### Inventory, Health & Reset
- `loseLives(int amount)` — subtracts up to `amount` lives and returns how many lives were actually lost.
- `addLife()` — adds 1 life if `lives < MAX_LIVES` (`5`) and returns `true`, or returns `false` if already at `5`.
- `addKey()` — increments `keyCount`.
- `activateSpeedBoost(float durationSeconds)` — sets `speedBoostTime = Math.max(speedBoostTime, durationSeconds)`.
- `activateShield(float durationSeconds)` — sets `shieldTime = Math.max(shieldTime, durationSeconds)`.
- `resetTemporaryState()` — clears direction, sprint, movement progress, and power-up timers.
- `resetForNewGame(int startX, int startY, int startingLives)` — moves player back to `(startX, startY)`, restores lives, resets `keyCount = 0`, and clears temporary effects.

## What Teammates Need to Know

- **Member 2 (Save/Load):** Read `player.getPosition()`, `player.getLives()`, `player.getKeyCount()`, `player.getShieldTime()`, and `player.getSpeedBoostTime()` when saving game state.
- **Member 3 (Enemy AI):** Read `world.getPlayer().getPosition()` and `world.getPlayer().hasShield()` to decide when an enemy should switch between Patrol, Attack, Evade, or Retreat.
- **Member 4 (Graphics) & Member 5 (HUD):** Draw the player at `player.getPosition()`, and show `player.getLives()` and `player.hasKey()` on the HUD.
