# `PowerUp.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/PowerUp.java`
- **Type:** `public final class PowerUp extends GameObject implements Collectable`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`PowerUp` is a collectable pickup that grants the player either a `PowerUpType.SPEED_BOOST` or `PowerUpType.SHIELD` for `durationSeconds`, and awards `+200` score points.

## Fields & Constructor

- `private final PowerUpType type` — `SPEED_BOOST` or `SHIELD`.
- `private final float durationSeconds` — active duration in seconds.
- `public PowerUp(int x, int y, PowerUpType type, float durationSeconds)` — creates a power-up at `(x, y)`.

## Methods

- `getType() : PowerUpType` — returns `type`.
- `getDurationSeconds() : float` — returns `durationSeconds`.
- `collect(GameWorld world, Player player) : void`:
  1. If `type == PowerUpType.SPEED_BOOST`, calls `player.activateSpeedBoost(durationSeconds)`.
  2. Otherwise (`SHIELD`), calls `player.activateShield(durationSeconds)`.
  3. Calls `world.addScore(200, "Power-up collected")`.
  4. Publishes `new GameEvent.PowerUpCollectedEvent(type, durationSeconds)` on `world.getEventBus()`.

## How Teammates Use It

- **Member 2 (Map Loader):** Add power-ups via `world.addCollectable(new PowerUp(x, y, PowerUpType.SHIELD, 5.0f))` or `PowerUpType.SPEED_BOOST`.
- **Member 4 (Renderer) & Member 5 (HUD):** Check `((PowerUp) item).getType()` to choose the right sprite, and read `player.getShieldTime()` / `player.getSpeedBoostTime()` to show active timers on the HUD.
