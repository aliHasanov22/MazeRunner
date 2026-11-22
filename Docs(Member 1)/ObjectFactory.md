# `ObjectFactory.java`

- **Package:** `com.mazerunner.map`
- **File:** `core/src/main/java/com/mazerunner/map/ObjectFactory.java`
- **Type:** `public final class ObjectFactory`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`ObjectFactory` creates the right `GameObject` subclass for each numeric type ID (`0` through `5`) defined in the project's `.properties` map file format.

## Constants

| Constant | ID | Created Class |
|---|---:|---|
| `FREE_PATH_ID` | `0` | `FreePath(x, y)` |
| `WALL_ID` | `1` | `Wall(x, y)` |
| `ENTRY_ID` | `2` | `Entry(x, y)` |
| `EXIT_ID` | `3` | `Exit(x, y)` |
| `TRAP_ID` | `4` | `Trap(x, y)` |
| `ENEMY_ID` | `5` | `Enemy(x, y)` |

## Method

- `create(int typeId, int x, int y) : GameObject` — uses a `switch (typeId)` to instantiate the matching `GameObject` at `(x, y)`. Throws `IllegalArgumentException("Unknown map object type ID: " + typeId)` for any ID outside `0–5`.

## How Teammates Use It

- **Member 2 (Map Parser):** For every `x,y = id` entry in a `.properties` file, call `factory.create(id, x, y)` and pass the returned `GameObject` to `map.setTile(...)`.
- **Member 3 (Obstacles):** If your invented obstacle gets its own map ID (e.g., `6`), add a new constant and `case` branch here with Member 1.
