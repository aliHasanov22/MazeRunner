# `Key.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Key.java`
- **Type:** `public final class Key extends GameObject implements Collectable`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`Key` is the collectable item required to unlock `Exit` tiles. It is placed in a `GameWorld` via `world.addCollectable(new Key(x, y))`.

## Constructor & Method

- `public Key(int x, int y)` — creates a key pickup at `(x, y)`.
- `collect(GameWorld world, Player player) : void`:
  1. Calls `player.addKey()` (so `player.hasKey()` becomes `true`).
  2. Calls `world.addScore(100, "Key collected")`.
  3. Publishes `new GameEvent.KeyCollectedEvent(getPosition(), player.getKeyCount())` on `world.getEventBus()`.

## How Teammates Use It

- **Member 2 (Map Loader):** Add key pickups to levels using `world.addCollectable(new Key(x, y))`.
- **Member 4 (Graphics & Audio):** Draw uncollected keys from `world.getCollectables()`, and subscribe to `GameEvent.KeyCollectedEvent` to play the key-pickup sound effect.
- **Member 5 (HUD):** Show key status on the HUD using `world.getPlayer().hasKey()`.
