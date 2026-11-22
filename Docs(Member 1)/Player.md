# `Player.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Player.java`
- **Type:** `public final class Player extends GameObject`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`Player` holds all gameplay state for the player character: position (inherited from `GameObject`), lives, keys, held direction, sprint flag, grid movement progress accumulator, and remaining timers for `SPEED_BOOST` and `SHIELD`.

## Constants

| Constant | Value | Meaning |
|---|---:|---|
| `DEFAULT_LIVES` | `3` | Default starting lives. |
| `MAX_LIVES` | `5` | Maximum lives the player can hold. |
| `NORMAL_SPEED` | `4.0f` | Walking speed in cells per second. |
| `SPRINT_SPEED` | `7.0f` | Sprinting speed (Shift held) in cells per second. |
| `SPEED_BOOST_MULTIPLIER` | `1.5f` | Speed multiplier while `speedBoostTime > 0`. |

## Fields

- `lives : int`, `keyCount : int`
- `direction : Direction`, `sprinting : boolean`, `movementProgress : float`
- `speedBoostTime : float`, `shieldTime : float`

## Constructor

- `public Player(int x, int y, int lives)` — spawns the player at `(x, y)` with `lives`.

## Methods

### Getters
- `getLives() : int` — remaining lives (`0..5`).
- `getKeyCount() : int` — number of keys collected.
- `hasKey() : boolean` — `true` if `keyCount > 0`.
- `hasShield() : boolean` — `true` if `shieldTime > 0`.
- `getShieldTime() : float` — remaining shield duration in seconds.
- `getSpeedBoostTime() : float` — remaining speed boost duration in seconds.
- `getDirection() : Direction` — currently held direction, or `null` if stopped.

### Movement
- `setMovement(Direction direction, boolean sprint) : void` — sets held direction and sprint flag; passing `null` stops the player and resets `movementProgress = 0`.
- `addMovementTime(float deltaSeconds) : void` — adds `deltaSeconds * speed` (`NORMAL_SPEED` or `SPRINT_SPEED`, multiplied by `1.5` when speed boost is active) to `movementProgress`.
- `canMoveOneCell() : boolean` — returns `direction != null && movementProgress >= 1`.
- `stepTo(GridPosition position) : void` — moves to `position` and subtracts `1` from `movementProgress`.
- `clearMovementTime() : void` — sets `movementProgress = 0` when blocked by a wall or locked exit.

### Lives, Keys, Power-Ups & Reset
- `loseLives(int amount) : int` — subtracts `Math.min(amount, lives)` and returns how many lives were removed.
- `addLife() : boolean` — increments `lives` if `lives < MAX_LIVES` (`5`) and returns `true`; returns `false` if already full.
- `addKey() : void` — increments `keyCount`.
- `activateSpeedBoost(float seconds) : void` — sets `speedBoostTime = Math.max(speedBoostTime, seconds)`.
- `activateShield(float seconds) : void` — sets `shieldTime = Math.max(shieldTime, seconds)`.
- `updateEffects(float deltaSeconds) : void` — decrements `speedBoostTime` and `shieldTime` down to `0`.
- `resetForNewGame(int startX, int startY, int startingLives) : void` — resets position, lives, keys (`0`), movement (`null, false`), and both power-up timers (`0`).

## How Teammates Use It

- **Member 2 (Save/Load):** Read `getPosition()`, `getLives()`, `getKeyCount()`, `getShieldTime()`, and `getSpeedBoostTime()`.
- **Member 3 (Enemy AI):** Check `world.getPlayer().getPosition()` and `world.getPlayer().hasShield()` for AI state transitions.
- **Member 4 (Graphics) & Member 5 (HUD):** Render the player at `getPosition()`, and display `getLives()`, `hasKey()`, `getShieldTime()`, and `getSpeedBoostTime()` on the HUD.
