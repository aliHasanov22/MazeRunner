# MazeRunner Core Gameplay Documentation (`Docs/`)

This folder contains a Markdown guide for every Java file in the core gameplay module (`core/src/main/java/com/mazerunner/` and `core/src/test/java/com/mazerunner/`), so each teammate knows what every class does and which methods to call.

## Files by Package

### 1. `com.mazerunner.core`
- [GameState.md](GameState.md) — `GameState.java` (the 5 game states: `RUNNING`, `PAUSED`, `VICTORY`, `GAME_OVER`, `MENU`)
- [GameWorld.md](GameWorld.md) — `GameWorld.java` (main simulation loop `tick(deltaSeconds)`, score, damage, pause/resume/reset)

### 2. `com.mazerunner.collision`
- [CollisionResolver.md](CollisionResolver.md) — `CollisionResolver.java` (item pickups, contact-entry hazard damage, 1.0s cooldown, exit win check)

### 3. `com.mazerunner.map`
- [GridPosition.md](GridPosition.md) — `GridPosition.java` (immutable `(x, y)` grid cell record + `move(Direction)`)
- [GridMap.md](GridMap.md) — `GridMap.java` (2D grid of tiles, enemy list, BFS exit reachability & map validation)
- [ObjectFactory.md](ObjectFactory.md) — `ObjectFactory.java` (creates maze objects from `.properties` map IDs `0–5`)

### 4. `com.mazerunner.entities`
- [GameObject.md](GameObject.md) — `GameObject.java` (abstract base class for all maze objects with unique `id`, `x`, `y`)
- [Collidable.md](Collidable.md) — `Collidable.java` (same-cell collision interface)
- [Damaging.md](Damaging.md) — `Damaging.java` (interface for hazards that remove lives)
- [Collectable.md](Collectable.md) — `Collectable.java` (interface for items the player can pick up)
- [Player.md](Player.md) — `Player.java` (player lives, keys, walk/sprint movement, shield & speed boost timers)
- [Enemy.md](Enemy.md) — `Enemy.java` (moving enemy with default patrol; ready for Member 3's AI state machine)
- [Wall.md](Wall.md) — `Wall.java` (solid wall tile that always blocks movement)
- [FreePath.md](FreePath.md) — `FreePath.java` (walkable floor tile)
- [Entry.md](Entry.md) — `Entry.java` (player starting tile; exactly 1 per map)
- [Exit.md](Exit.md) — `Exit.java` (outer-border exit tile; blocked until the player has a key)
- [Trap.md](Trap.md) — `Trap.java` (static damaging tile; removes 1 life on entry)
- [Key.md](Key.md) — `Key.java` (collectable key that unlocks the exit and gives +100 points)
- [ExtraLife.md](ExtraLife.md) — `ExtraLife.java` (collectable heart that gives +1 life up to 5, or +300 points if full)
- [PowerUp.md](PowerUp.md) — `PowerUp.java` (collectable temporary `SPEED_BOOST` or `SHIELD`, +200 points)

### 5. `com.mazerunner.events`
- [EventBus.md](EventBus.md) — `EventBus.java` (publish/subscribe bus so gameplay never calls UI or audio directly)
- [GameEvent.md](GameEvent.md) — `GameEvent.java` (base event interface + all 8 event records)

### 6. `com.mazerunner.input`
- [Direction.md](Direction.md) — `Direction.java` (`UP`, `DOWN`, `LEFT`, `RIGHT` and their `(dx, dy)` step offsets)

### 7. `com.mazerunner.items`
- [PowerUpType.md](PowerUpType.md) — `PowerUpType.java` (`SPEED_BOOST`, `SHIELD`)

### 8. `com.mazerunner` (Tests)
- [CoreGameplaySelfTest.md](CoreGameplaySelfTest.md) — `CoreGameplaySelfTest.java` (standalone smoke test runner for all core rules)
