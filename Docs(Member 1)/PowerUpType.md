# `PowerUpType.java`

- **Package:** `com.mazerunner.items`
- **File:** `core/src/main/java/com/mazerunner/items/PowerUpType.java`
- **Type:** `public enum PowerUpType`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`PowerUpType` defines the two temporary power-up effects supported by `PowerUp` and `Player`.

## Enum Values

| Value | Effect on `Player` |
|---|---|
| `SPEED_BOOST` | Multiplies player movement speed (walk and sprint) by `1.5x` (`Player.SPEED_BOOST_MULTIPLIER`). |
| `SHIELD` | Makes `player.hasShield()` return `true`, blocking all hazard damage until the timer runs out. |

## How Teammates Use It

- **Member 2 (Map Loader):** Pass `PowerUpType.SPEED_BOOST` or `PowerUpType.SHIELD` when creating `new PowerUp(...)`.
- **Member 4 (Renderer) & Member 5 (HUD):** Check `powerUp.getType()` or `PowerUpCollectedEvent.type()` to draw the right icon.
