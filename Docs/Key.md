# `Key.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Key.java`
- **Type:** `public final class Key extends GameObject implements Collectable`

## What This File Does

`Key` is a collectable pickup placed on the map via `world.addCollectable(new Key(x, y))`. Collecting at least one `Key` unlocks all `Exit` tiles on the map.

## Constructor & Method

- `Key(int x, int y)` — creates a key pickup at grid cell `(x, y)`.
- `collect(GameWorld world, Player player) : void` — called automatically by `CollisionResolver` when the player steps on `(x, y)`:
  1. Calls `player.addKey()` (so `player.hasKey()` becomes `true`).
  2. Calls `world.addScore(100, "Key collected")`.
  3. Publishes `new GameEvent.KeyCollectedEvent(getPosition(), player.getKeyCount())` on `world.getEventBus()`.

## What Teammates Need to Know

- **Member 2 (Map Loader):** Add keys to a `GameWorld` using `world.addCollectable(new Key(x, y))`.
- **Member 4 (Graphics & Audio):** Render active keys from `world.getCollectables()` (check `item instanceof Key`), and subscribe to `GameEvent.KeyCollectedEvent` to play the key-collected sound effect.
- **Member 5 (HUD):** Display whether the player has collected the key using `world.getPlayer().hasKey()` (or `getKeyCount()`).
