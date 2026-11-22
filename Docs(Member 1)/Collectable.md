# `Collectable.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Collectable.java`
- **Type:** `public interface Collectable`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`Collectable` is the interface implemented by pickup items (`Key`, `ExtraLife`, `PowerUp`). When the player steps onto a collectable's cell, `CollisionResolver` calls `collect(world, player)` and removes the item from the world.

## Method

- `collect(GameWorld world, Player player) : void` — applies the pickup's effect to `player` and `world` (updating keys/lives/power-up timers, adding score, and publishing a `GameEvent`).

## How Teammates Use It

- **Member 2 (Map Loader):** Any item passed to `world.addCollectable(item)` must implement `Collectable` (such as `Key`, `ExtraLife`, or `PowerUp`).
- **Member 4 (Renderer):** Iterate over `world.getCollectables()` to draw uncollected items on the maze.
