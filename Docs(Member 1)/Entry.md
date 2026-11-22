# `Entry.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Entry.java`
- **Type:** `public final class Entry extends GameObject`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`Entry` represents the starting cell where the player spawns when a `GameWorld` starts or resets (map type ID `2` in `ObjectFactory`).

## Constructor

- `public Entry(int x, int y)` — creates the entry tile at `(x, y)`. Inherits `blocksMovement(hasKey) == false` from `GameObject`.

## How Teammates Use It

- **Member 2 (Map Loader & Validator):** Map ID `2` (`ObjectFactory.ENTRY_ID`) creates an `Entry`. Valid maps must contain exactly one `Entry`.
- **Member 4 (Renderer):** Check `tile instanceof Entry` to draw a visually distinct entrance sprite/color.
