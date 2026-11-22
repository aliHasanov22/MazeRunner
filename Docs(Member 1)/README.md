# Member 1 Documentation (`Docs(Member 1)`)

This folder documents every Java file in the core gameplay module (`core/src/main/java/com/mazerunner/` and `core/src/test/java/com/mazerunner/`), written by **Member 1 (Ali Hasanov)** so teammates can quickly see what every class does, which methods to call, and how each part connects to their module.

---

## All Documented Java Files (25 Java Files Across 7 Packages + Tests)

### `com.mazerunner.core`
- [GameState.md](GameState.md) — `GameState.java` (the 5 game states: `RUNNING`, `PAUSED`, `VICTORY`, `GAME_OVER`, `MENU`)
- [GameWorld.md](GameWorld.md) — `GameWorld.java` (main game loop `tick`, movement, score, damage, victory, game over, pause, reset)

### `com.mazerunner.collision`
- [CollisionResolver.md](CollisionResolver.md) — `CollisionResolver.java` (item pickups, contact-entry hazard damage, 1.0s cooldown, exit win check)

### `com.mazerunner.map`
- [GridMap.md](GridMap.md) — `GridMap.java` (2D grid of tiles, enemy list, entry lookup, nearest-exit lookup for HUD)
- [GridPosition.md](GridPosition.md) — `GridPosition.java` (grid coordinate `(x, y)` record and `move(Direction)`)
- [ObjectFactory.md](ObjectFactory.md) — `ObjectFactory.java` (creates maze objects from `.properties` map type IDs `0–5`)

### `com.mazerunner.entities`
- [GameObject.md](GameObject.md) — `GameObject.java` (abstract base class for all maze objects with unique `id`, `x`, `y`)
- [Collidable.md](Collidable.md) — `Collidable.java` (interface for same-cell collision checks)
- [Damaging.md](Damaging.md) — `Damaging.java` (interface for hazards that remove lives)
- [Collectable.md](Collectable.md) — `Collectable.java` (interface for items the player can pick up)
- [Player.md](Player.md) — `Player.java` (lives, keys, walk/sprint movement, shield and speed-boost timers)
- [Wall.md](Wall.md) — `Wall.java` (solid wall cell that always blocks movement)
- [FreePath.md](FreePath.md) — `FreePath.java` (normal walkable floor cell)
- [Entry.md](Entry.md) — `Entry.java` (starting cell where the player spawns)
- [Exit.md](Exit.md) — `Exit.java` (border exit cell; blocked until the player collects a key)
- [Key.md](Key.md) — `Key.java` (collectable key that unlocks exits and awards +100 score)
- [ExtraLife.md](ExtraLife.md) — `ExtraLife.java` (collectable heart that gives +1 life up to 5, or +300 score if full)
- [PowerUp.md](PowerUp.md) — `PowerUp.java` (collectable temporary `SPEED_BOOST` or `SHIELD`, awards +200 score)
- [Trap.md](Trap.md) — `Trap.java` *(starter class created for `ObjectFactory` ID `4`; handed off to Member 3)*
- [Enemy.md](Enemy.md) — `Enemy.java` *(starter patrol class created for `ObjectFactory` ID `5`; handed off to Member 3 for AI)*

### `com.mazerunner.events`
- [EventBus.md](EventBus.md) — `EventBus.java` (publish/subscribe event bus disconnecting gameplay from UI/audio)
- [GameEvent.md](GameEvent.md) — `GameEvent.java` (base event interface + all 8 event records)

### `com.mazerunner.input`
- [Direction.md](Direction.md) — `Direction.java` (`UP`, `DOWN`, `LEFT`, `RIGHT` and their `(dx, dy)` step offsets)

### `com.mazerunner.items`
- [PowerUpType.md](PowerUpType.md) — `PowerUpType.java` (`SPEED_BOOST`, `SHIELD`)

### `com.mazerunner` (Tests)
- [CoreGameplaySelfTest.md](CoreGameplaySelfTest.md) — `CoreGameplaySelfTest.java` (headless smoke test runner via `./gradlew coreGameplaySmokeTest`)
