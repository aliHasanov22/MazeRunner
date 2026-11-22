# `Enemy.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/Enemy.java`
- **Type:** `public final class Enemy extends GameObject implements Damaging`
- **Author:** Ali Hasanov *(starter patrol class created for `ObjectFactory` ID `5`; Member 3 owns enemy AI)*

## What This File Does

`Enemy` is a moving hazard that removes 1 life when it touches the player. This starter version moves one cell every `0.8` seconds in a simple patrol (`RIGHT -> DOWN -> LEFT -> UP`) so `ObjectFactory`, `GameWorld.tick()`, and `CollisionResolver` work right away.

## Constants & Fields

- `private static final float SECONDS_PER_STEP = 0.8f`
- `private static final Direction[] PATROL = {Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP}`
- `private final int startX`, `private final int startY` — spawn cell saved for `resetForNewGame()`.
- `private float timer`, `private int directionIndex` — patrol timer and current direction index.

## Constructor & Methods

- `public Enemy(int x, int y)` — creates an enemy at `(x, y)` and records `(startX, startY)`.
- `getDamageAmount() : int` — returns `1`.
- `update(float deltaSeconds, GameWorld world) : void` — adds `deltaSeconds` to `timer`; when `timer >= 0.8f`, tries moving one cell in `PATROL[directionIndex]` if `world.canEnemyEnter(next, this)` returns `true`, turning clockwise if blocked.
- `resetForNewGame() : void` — moves the enemy back to `(startX, startY)` and resets `timer = 0` and `directionIndex = 0`.

## How Teammates Use It

- **Member 3 (Obstacles & AI):** Replace `update(float deltaSeconds, GameWorld world)` with the **Patrol / Attack / Evade / Retreat** state machine (and optional A* pathfinding), calling `world.canEnemyEnter(next, this)` before stepping.
- **Member 4 (Renderer):** Draw all enemies returned by `world.getMap().getEnemies()`.
