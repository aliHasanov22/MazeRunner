# `GameObject.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/GameObject.java`
- **Type:** `public abstract class GameObject implements Collidable`

## What This File Does

`GameObject` is the shared abstract base class for every object placed in the maze (`Player`, `Enemy`, `Wall`, `FreePath`, `Entry`, `Exit`, `Trap`, `Key`, `ExtraLife`, `PowerUp`). It stores a unique auto-incrementing `id` and grid coordinates `(x, y)` so all entities share position and collision logic without duplicating code.

## Fields & Constructor

- `nextId : long` — static counter starting at `1` that gives every object a unique ID.
- `id : long` — unique ID used by `CollisionResolver` to track active hazard contacts.
- `x : int`, `y : int` — current horizontal and vertical grid coordinates.
- `# GameObject(int x, int y)` — protected constructor called by all subclasses via `super(x, y)`.

## Methods

- `getId() : long` — returns the object's unique ID.
- `getX() : int` / `getY() : int` — returns the horizontal / vertical grid coordinate.
- `getPosition() : GridPosition` — returns a `new GridPosition(x, y)`.
- `# setPosition(int newX, int newY) : void` — protected helper used by moving subclasses (`Player`, `Enemy`) to change grid cells.
- `blocksMovement(boolean hasKey) : boolean` — returns `false` by default; overridden by `Wall` (always `true`) and `Exit` (`!hasKey`).
- `update(float deltaSeconds, GameWorld world) : void` — per-tick update hook; empty by default for static objects, overridden by `Enemy`.
- `collidesWith(GameObject other) : boolean` — returns `true` when `other != null` and both objects have the same `x` and `y`.

## What Teammates Need to Know

- **Member 3 (Obstacles & AI):** Your invented obstacle should extend `GameObject` (and implement `Damaging`). If it moves, override `update(float deltaSeconds, GameWorld world)` and call `setPosition(newX, newY)`.
- **Member 4 (Graphics):** Every tile, enemy, collectable, and player is a `GameObject`, so you can always call `obj.getX()`, `obj.getY()`, or `obj.getPosition()` to know where to draw its sprite.
