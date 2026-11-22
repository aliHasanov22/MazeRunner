# `ExtraLife.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/ExtraLife.java`
- **Type:** `public final class ExtraLife extends GameObject implements Collectable`
- **Author:** Ali Hasanov (Member 1)

## What This File Does

`ExtraLife` is a collectable pickup that adds `+1` life (up to `Player.MAX_LIVES = 5`) and awards bonus score. If the player is already at full lives, it converts into a larger score bonus instead.

## Constructor & Method

- `public ExtraLife(int x, int y)` — creates an extra-life pickup at `(x, y)`.
- `collect(GameWorld world, Player player) : void`:
  1. Calls `boolean added = player.addLife()`.
  2. If `added` is `true`, calls `world.addScore(150, "Extra life collected")`.
  3. If `added` is `false` (already at 5 lives), calls `world.addScore(300, "Extra life converted to score")`.
  4. Publishes `new GameEvent.ExtraLifeCollectedEvent(player.getLives(), added)` on `world.getEventBus()`.

## How Teammates Use It

- **Member 2 (Map Loader):** Add extra-life pickups with `world.addCollectable(new ExtraLife(x, y))`.
- **Member 4 (Renderer) & Member 5 (HUD):** Render items where `item instanceof ExtraLife` in `world.getCollectables()`, and display updated lives from `player.getLives()`.
