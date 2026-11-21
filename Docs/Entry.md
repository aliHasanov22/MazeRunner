# `Entry.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Entry.java`
- **Type:** `public final class Entry extends GameObject`

## What This File Does

`Entry` marks the starting cell where the player spawns (created by `ObjectFactory` for map type ID `2`). Every valid map must have **exactly one** `Entry` tile (`GridMap.findEntry()` and `GridMap.validate()` enforce this).

## Constructor

- `Entry(int x, int y)` — creates the entry cell at `(x, y)`. Inherits `blocksMovement(hasKey) == false` from `GameObject`, so the player can freely walk off and back onto it.

## What Teammates Need to Know

- **Member 2 (Map Loader & Validator):** Map ID `2` (`ObjectFactory.enterence_ID`) creates an `Entry`. Maps with 0 or more than 1 `Entry` are rejected by `GridMap.validate()`.
- **Member 4 (Renderer):** Draw `Entry` with a distinct sprite/color so the player can visually distinguish the entrance from the exit and normal floor tiles.
