# `Direction.java`

- **Package:** `com.mazerunner.input`
- **File:** `core/src/main/java/com/mazerunner/input/Direction.java`
- **Type:** `public enum Direction`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`Direction` defines the 4 movement directions on the maze grid and their `(dx, dy)` step offsets.

## Enum Values

| Value | `getDx()` | `getDy()` | Grid Step |
|---|---:|---:|---|
| `UP` | `0` | `+1` | `(x, y + 1)` |
| `DOWN` | `0` | `-1` | `(x, y - 1)` |
| `LEFT` | `-1` | `0` | `(x - 1, y)` |
| `RIGHT` | `+1` | `0` | `(x + 1, y)` |

## Fields & Methods

- `private final int dx`, `private final int dy`
- `getDx() : int` — returns horizontal step (`-1`, `0`, or `+1`).
- `getDy() : int` — returns vertical step (`-1`, `0`, or `+1`).

## How Teammates Use It

- **Member 2 (Key Bindings Config) & Member 5 (Input):** Map pressed keys to a `Direction` (or `null` when released) and call `world.setMovement(direction, sprintHeld)`.
- **Member 3 (Enemy AI):** Loop over `Direction.values()` and call `pos.move(dir)` to inspect neighbouring cells.
