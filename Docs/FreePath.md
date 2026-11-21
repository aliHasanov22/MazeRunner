# `FreePath.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/FreePath.java`
- **Type:** `public final class FreePath extends GameObject`

## What This File Does

`FreePath` is a standard walkable floor cell in the maze (created by `ObjectFactory` for map type ID `0`, and also used by `GridMap` to pre-fill all cells and to sit underneath `Enemy` spawns).

## Constructor

- `FreePath(int x, int y)` — creates a walkable floor tile at `(x, y)`. Inherits `blocksMovement(hasKey) == false` from `GameObject`.

## What Teammates Need to Know

- **Member 2 (Map Loader):** Map ID `0` (`ObjectFactory.FREE_PATH_ID`) creates a `FreePath`. Any cell not listed in a `.properties` file is also already a `FreePath` in `GridMap`.
- **Member 4 (Renderer):** Check `tile instanceof FreePath` to draw the floor tile sprite.
