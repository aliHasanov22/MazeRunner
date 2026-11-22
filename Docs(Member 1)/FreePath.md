# `FreePath.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/FreePath.java`
- **Type:** `public final class FreePath extends GameObject`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`FreePath` represents a standard walkable floor tile in the maze (map type ID `0` in `ObjectFactory`). `GridMap` fills every cell with `FreePath` by default and leaves a `FreePath` underneath each `Enemy` spawn cell.

## Constructor

- `public FreePath(int x, int y)` — creates a walkable floor tile at `(x, y)`. Inherits `blocksMovement(hasKey) == false` from `GameObject`.

## How Teammates Use It

- **Member 2 (Map Loader):** Map ID `0` (`ObjectFactory.FREE_PATH_ID`) creates a `FreePath`, and any coordinate missing from a `.properties` file defaults to `FreePath`.
- **Member 4 (Renderer):** Check `tile instanceof FreePath` to draw the floor sprite.
