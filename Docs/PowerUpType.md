# `PowerUpType.java`

- **Package:** `com.mazerunner.items`
- **File:** `core/src/main/java/com/mazerunner/items/PowerUpType.java`
- **Type:** `public enum PowerUpType`

## What This File Does

`PowerUpType` lists the temporary power-up effects that a `PowerUp` item can give to the player.

## Enum Values

| Value | Gameplay Effect |
|---|---|
| `SPEED_BOOST` | Multiplies player movement speed (both walking and sprinting) by `1.5x` (`Player.SPEED_BOOST_MULTIPLIER`) while active. |
| `SHIELD` | Makes `player.hasShield()` return `true` while active, preventing any lives from being lost on hazard contact. |

## What Teammates Need to Know

- **Member 2 (Map Loader):** Pass `PowerUpType.SPEED_BOOST` or `PowerUpType.SHIELD` when constructing a `new PowerUp(x, y, type, durationSeconds)`.
- **Member 4 (Graphics) & Member 5 (HUD):** Check `powerUp.getType()` to choose the right sprite for a `PowerUp` on the map, and check `GameEvent.PowerUpCollectedEvent.type()` when a power-up is picked up.
