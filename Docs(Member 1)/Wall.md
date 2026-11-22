# `Wall.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Wall.java`
- **Type:** `public final class Wall extends GameObject`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`Wall` represents a solid, impassable maze tile (map type ID `1` in `ObjectFactory`). Neither the player nor enemies can move into a `Wall` cell.

## Constructor & Method

- `public Wall(int x, int y)` — creates a wall at `(x, y)`.
- `blocksMovement(boolean hasKey) : boolean` — always returns `true`.

## How Teammates Use It

- **Member 2 (Map Loader):** Map ID `1` (`ObjectFactory.WALL_ID`) creates a `Wall`.
- **Member 4 (Renderer):** Check `tile instanceof Wall` when drawing maze tiles.
