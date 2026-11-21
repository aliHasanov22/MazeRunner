# `Enemy.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Enemy.java`
- **Type:** `public final class Enemy extends GameObject implements Damaging`

## What This File Does

`Enemy` is a moving hazard that removes 1 life when it collides with the player. Right now it runs a simple, deterministic 4-direction patrol (`RIGHT -> DOWN -> LEFT -> UP`, moving one cell every `0.8` seconds) so the core game loop and collision tests work out of the box.

## Constants & Fields

- `SECONDS_PER_STEP = 0.8f` — time in seconds between enemy steps.
- `DIRECTIONS = { RIGHT, DOWN, LEFT, UP }` — direction cycle for the default patrol.
- `startX : int`, `startY : int` — initial spawn coordinates saved so `resetForNewGame()` can put the enemy back where it started.
- `movementTimer : float` — accumulated time toward the next step.
- `directionIndex : int` — current index in `DIRECTIONS`.

## Constructor & Methods

- `Enemy(int x, int y)` — creates an enemy at `(x, y)` and saves `(startX, startY)`.
- `getDamageAmount() : int` — returns `1` (one contact hit removes 1 life).
- `update(float deltaSeconds, GameWorld world) : void` — called once per tick by `GameWorld.tick(deltaSeconds)`. Accumulates time and moves one cell at a time if `world.canEnemyEnter(next, this)` allows entering the target cell.
- `resetForNewGame() : void` — restores position to `(startX, startY)` and resets `movementTimer` and `directionIndex`.

## What Teammates Need to Know

- **Member 3 (Obstacles & AI — Primary Owner for AI Upgrade):**
  - Replace or expand `update(float deltaSeconds, GameWorld world)` with your **Patrol / Attack / Evade / Retreat** state machine (and optional A* pathfinding).
  - Always check `world.canEnemyEnter(nextPosition, this)` before moving to a cell so enemies never walk through walls, exits, out of bounds, or on top of another enemy.
  - Keep `resetForNewGame()` resetting your state machine back to `PATROL` at `(startX, startY)`.
- **Member 4 (Renderer):** Get all enemies from `world.getMap().getEnemies()` and draw each enemy at `enemy.getPosition()`.
