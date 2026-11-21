# `PowerUp.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/PowerUp.java`
- **Type:** `public final class PowerUp extends GameObject implements Collectable`

## What This File Does

`PowerUp` is a collectable pickup that grants the player a temporary effect (`PowerUpType.SPEED_BOOST` or `PowerUpType.SHIELD`) for a specified number of seconds and awards `+200` score points.

## Fields, Constructor & Methods

- `type : PowerUpType` — either `SPEED_BOOST` (`1.5x` movement speed) or `SHIELD` (blocks hazard damage).
- `durationSeconds : float` — how long the effect lasts in seconds (must be `> 0`).
- `PowerUp(int x, int y, PowerUpType type, float durationSeconds)` — creates a power-up at `(x, y)`.
- `getType() : PowerUpType` — returns the power-up's `PowerUpType`.
- `getDurationSeconds() : float` — returns the effect duration in seconds.
- `collect(GameWorld world, Player player) : void`:
  1. If `type == PowerUpType.SPEED_BOOST`, calls `player.activateSpeedBoost(durationSeconds)`.
  2. If `type == PowerUpType.SHIELD`, calls `player.activateShield(durationSeconds)`.
  3. Calls `world.addScore(200, "Power-up collected")`.
  4. Publishes `new GameEvent.PowerUpCollectedEvent(type, durationSeconds)` on `world.getEventBus()`.

## What Teammates Need to Know

- **Member 2 (Map Loader):** Add power-ups with `world.addCollectable(new PowerUp(x, y, PowerUpType.SHIELD, 5.0f))` or `PowerUpType.SPEED_BOOST`.
- **Member 4 (Renderer) & Member 5 (HUD):** Inspect `((PowerUp) item).getType()` when rendering `world.getCollectables()` to draw a shield icon vs. a speed-boot icon, and check `player.getShieldTime()` / `player.getSpeedBoostTime()` to display active buff timers on the HUD.
