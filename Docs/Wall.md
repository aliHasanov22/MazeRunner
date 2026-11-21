# `Wall.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Wall.java`
- **Type:** `public final class Wall extends GameObject`

## What This File Does

`Wall` is a solid maze tile (created by `ObjectFactory` for map type ID `1`) that neither the player nor enemies can walk through.

## Constructor & Method

- `Wall(int x, int y)` — creates a wall at grid cell `(x, y)`.
- `blocksMovement(boolean hasKey) : boolean` — always returns `true`.

## What Teammates Need to Know

- **Member 2 (Map Loader):** Map value `1` in `.properties` files maps to `Wall` (`ObjectFactory.wall_ID`).
- **Member 4 (Renderer):** Check `tile instanceof Wall` when drawing wall sprites across `GridMap`.
