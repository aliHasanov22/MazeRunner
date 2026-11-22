package com.mazerunner;

import com.mazerunner.core.GameState;
import com.mazerunner.core.GameWorld;
import com.mazerunner.entities.Enemy;
import com.mazerunner.entities.Entry;
import com.mazerunner.entities.Exit;
import com.mazerunner.entities.ExtraLife;
import com.mazerunner.entities.FreePath;
import com.mazerunner.entities.Key;
import com.mazerunner.entities.PowerUp;
import com.mazerunner.entities.Trap;
import com.mazerunner.entities.Wall;
import com.mazerunner.events.EventBus;
import com.mazerunner.events.GameEvent;
import com.mazerunner.input.Direction;
import com.mazerunner.items.PowerUpType;
import com.mazerunner.map.GridMap;
import com.mazerunner.map.GridPosition;
import com.mazerunner.map.ObjectFactory;

import java.util.function.Consumer;

/**
 * Checks the core gameplay rules without opening a window.
 * Run with: ./gradlew coreGameplaySmokeTest
 *
 * @author Ali Hasanov
 */
public final class CoreGameplaySelfTest {

    private static int eventCalls = 0;

    /** Runs all checks. Throws AssertionError if one fails. */
    public static void main(String[] args) {
        // factory IDs 0-5
        ObjectFactory factory = new ObjectFactory();
        check(factory.create(0, 0, 0) instanceof FreePath, "ID 0 = FreePath");
        check(factory.create(1, 0, 0) instanceof Wall, "ID 1 = Wall");
        check(factory.create(2, 0, 0) instanceof Entry, "ID 2 = Entry");
        check(factory.create(3, 0, 0) instanceof Exit, "ID 3 = Exit");
        check(factory.create(4, 0, 0) instanceof Trap, "ID 4 = Trap");
        check(factory.create(5, 0, 0) instanceof Enemy, "ID 5 = Enemy");

        // wall blocks the player
        GridMap map = basicMap();
        map.setTile(new Wall(1, 1));
        GameWorld world = new GameWorld(map);
        world.setMovement(Direction.RIGHT, false);
        world.tick(0.5f);
        check(world.getPlayer().getPosition().equals(new GridPosition(0, 1)), "Wall must block player");

        // locked exit works like a wall
        world = new GameWorld(basicMap());
        world.setMovement(Direction.RIGHT, false);
        world.tick(1.0f);
        check(world.getPlayer().getX() == 2 && world.getState() == GameState.RUNNING, "Locked exit must block");

        // key opens exit -> victory
        world = new GameWorld(basicMap());
        world.addCollectable(new Key(1, 1));
        world.setMovement(Direction.RIGHT, false);
        world.tick(1.0f);
        check(world.getPlayer().hasKey(), "Key should be collected");
        check(world.getState() == GameState.VICTORY, "Reaching open exit should win");
        check(world.getScore() >= 600, "Key and victory give score");

        // trap: one hit per contact, standing on it does not drain lives
        map = basicMap();
        map.setTile(new Trap(1, 1));
        world = new GameWorld(map);
        world.setMovement(Direction.RIGHT, false);
        world.tick(0.25f);
        check(world.getPlayer().getLives() == 2, "Trap removes 1 life");
        world.setMovement(null, false);
        world.tick(2.0f);
        check(world.getPlayer().getLives() == 2, "Standing on trap must not remove more lives");
        stepOffAndBack(world);
        check(world.getPlayer().getLives() == 1, "Re-entering trap is a new hit");
        world.tick(1.1f);
        stepOffAndBack(world);
        check(world.getPlayer().getLives() == 0, "Third hit uses last life");
        check(world.getState() == GameState.GAME_OVER, "0 lives = game over");

        // extra life and shield
        world = new GameWorld(basicMap());
        world.addCollectable(new ExtraLife(0, 1));
        world.addCollectable(new PowerUp(1, 1, PowerUpType.SHIELD, 2.0f));
        world.tick(0.01f);
        check(world.getPlayer().getLives() == 4, "Extra life adds 1 life");
        world.setMovement(Direction.RIGHT, false);
        world.tick(0.25f);
        check(world.getPlayer().hasShield(), "Shield should be active");

        // reset
        world.addScore(50, "test");
        world.resetForNewGame();
        check(world.getState() == GameState.RUNNING, "Reset -> RUNNING");
        check(world.getPlayer().getPosition().equals(new GridPosition(0, 1)), "Reset -> back to entry");
        check(world.getPlayer().getLives() == 3 && world.getScore() == 0, "Reset lives and score");
        check(!world.getPlayer().hasShield(), "Reset clears shield");
        check(world.getCollectables().size() == 2, "Reset puts items back");

        // pause freezes time
        world.pause();
        world.tick(1.0f);
        check(world.getElapsedSeconds() == 0, "Paused game must not advance time");

        // event bus subscribe / unsubscribe
        EventBus bus = new EventBus();
        Consumer<GameEvent.ScoreChangedEvent> listener = event -> eventCalls++;
        bus.subscribe(GameEvent.ScoreChangedEvent.class, listener);
        bus.publish(new GameEvent.ScoreChangedEvent(10, 10, "test"));
        bus.unsubscribe(GameEvent.ScoreChangedEvent.class, listener);
        bus.publish(new GameEvent.ScoreChangedEvent(20, 10, "test"));
        check(eventCalls == 1, "Unsubscribed listener must not get events");

        System.out.println("All core gameplay checks passed.");
    }

    // Moves left off the trap at (1,1) and back onto it.
    private static void stepOffAndBack(GameWorld world) {
        world.setMovement(Direction.LEFT, false);
        world.tick(0.25f);
        world.setMovement(Direction.RIGHT, false);
        world.tick(0.25f);
    }

    // 4x3 map: entry at (0,1), exit at (3,1).
    private static GridMap basicMap() {
        GridMap map = new GridMap(4, 3);
        map.setTile(new Entry(0, 1));
        map.setTile(new Exit(3, 1));
        return map;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
