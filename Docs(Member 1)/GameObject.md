# `GameObject.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/GameObject.java`
- **Type:** `public abstract class GameObject implements Collidable`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`GameObject` is the shared abstract base class for every object in the maze (`Player`, `Enemy`, `Wall`, `FreePath`, `Entry`, `Exit`, `Trap`, `Key`, `ExtraLife`, `PowerUp`). It owns each object's unique ID, grid position `(x, y)`, default movement-blocking rule, default update hook, and same-cell collision check.

## Fields

- `private static long nextId = 1` — shared counter that assigns every new `GameObject` a unique ID.
- `private final long id` — unique ID used by `CollisionResolver` to track active hazard contacts.
- `private int x`, `private int y` — grid cell coordinates.

## Constructor

- `protected GameObject(int x, int y)` — assigns `id = nextId++` and stores `(x, y)`.

## Methods

- `getId() : long` — returns the object's unique ID.
- `getX() : int` / `getY() : int` — returns the horizontal / vertical grid coordinate.
- `getPosition() : GridPosition` — returns `new GridPosition(x, y)`.
- `protected setPosition(int newX, int newY) : void` — moves the object to another cell (used by `Player` and `Enemy`).
- `blocksMovement(boolean hasKey) : boolean` — returns `false` by default; overridden by `Wall` (always `true`) and `Exit` (`!hasKey`).
- `update(float deltaSeconds, GameWorld world) : void` — called once per tick; empty by default for static objects, overridden by `Enemy`.
- `collidesWith(GameObject other) : boolean` — returns `other != null && x == other.x && y == other.y`.

## How Teammates Use It

- **Member 3 (Obstacles & AI):** Extend `GameObject` for your invented obstacle (and implement `Damaging`). Override `update(...)` and call `setPosition(...)` if it moves, or override `blocksMovement(...)` if it blocks movement.
- **Member 4 (Renderer):** Every tile, enemy, pickup, and player is a `GameObject`, so you can always use `getX()`, `getY()`, or `getPosition()` when rendering.
