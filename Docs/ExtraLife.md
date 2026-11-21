# `ExtraLife.java`

- **Package:** `com.mazerunner.entities`
- **File:** `core/src/main/java/com/mazerunner/entities/ExtraLife.java`
- **Type:** `public final class ExtraLife extends GameObject implements Collectable`

## What This File Does

`ExtraLife` is a collectable pickup placed via `world.addCollectable(new ExtraLife(x, y))`. When the player walks over it, it gives `+1` life (up to `Player.MAX_LIVES = 5`) and bonus points.

## Constructor & Method

- `ExtraLife(int x, int y)` — creates an extra-life item at `(x, y)`.
- `collect(GameWorld world, Player player) : void`:
  1. Calls `boolean added = player.addLife()`.
  2. If `added` is `true` (lives were `< 5`), awards `+150` score (`"Extra life collected"`).
  3. If `added` is `false` (player was already at `5` lives), awards `+300` score (`"Extra life converted to score"`).
  4. Publishes `new GameEvent.ExtraLifeCollectedEvent(player.getLives(), added)` on `world.getEventBus()`.

## What Teammates Need to Know

- **Member 2 (Map Loader):** Add extra-life pickups to a level with `world.addCollectable(new ExtraLife(x, y))`.
- **Member 4 (Renderer) & Member 5 (HUD):** Draw items where `item instanceof ExtraLife` in `world.getCollectables()`, and update the HUD heart/life display from `player.getLives()` or `ExtraLifeCollectedEvent`.
