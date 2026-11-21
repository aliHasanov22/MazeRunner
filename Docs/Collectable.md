# `Collectable.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Collectable.java`
- **Type:** `public interface Collectable`

## What This File Does

`Collectable` is the interface implemented by items that the player can pick up when stepping onto their cell (`Key`, `ExtraLife`, and `PowerUp`).

## Method

- `collect(GameWorld world, Player player) : void` — applies the pickup's effect to `player` and `world` (such as adding a key/life/buff, adding score, and publishing a `GameEvent` on `world.getEventBus()`).

## What Teammates Need to Know

- When the player walks onto a cell containing a `Collectable`, `CollisionResolver` calls `item.collect(world, player)` and then removes the item from `world.getCollectables()`.
- If anyone adds a new `Collectable` class later, also add a branch for it in `GameWorld.copyCollectable(...)` so `resetForNewGame()` can respawn it.
