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

/**
 * Simple test runner for verifying core gameplay rules without opening a game window.
 *
 * @author Ali Hasanov
 */
public final class CoreGameplaySelfTest {

    private static int eventCalls = 0;

    private CoreGameplaySelfTest() {
    }

    /** Runs all core gameplay checks and throws an AssertionError if any check fails. */
    public static void main(String[] args) {
        testFactoryIds();
        testWallBlocksPlayer();
        testExitNeedsKeyAndKeyWins();
        testTrapContactDoesNotDrainLivesEveryFrame();
        testGameOverAfterThreeLogicalHits();
        testPowerUpsAndExtraLife();
        testResetRestoresNewGameState();
        testEventBusSubscription();
        System.out.println("All core gameplay checks passed.");
    }

    private static void testFactoryIds() {
        ObjectFactory factory = new ObjectFactory();
        check(factory.create(0, 0, 0) instanceof FreePath, "ID 0 should create FreePath");
        check(factory.create(1, 0, 0) instanceof Wall, "ID 1 should create Wall");
        check(factory.create(2, 0, 0) instanceof Entry, "ID 2 should create Entry");
        check(factory.create(3, 0, 0) instanceof Exit, "ID 3 should create Exit");
        check(factory.create(4, 0, 0) instanceof Trap, "ID 4 should create Trap");
        check(factory.create(5, 0, 0) instanceof Enemy, "ID 5 should create Enemy");

        boolean rejected = false;
        try {
            factory.create(6, 0, 0);
        } catch (IllegalArgumentException e) {
            rejected = true;
        }
        check(rejected, "Unknown IDs must be rejected");
    }

    private static void testWallBlocksPlayer() {
        GridMap map = basicMap();
        map.setTile(new Wall(1, 1));
        GameWorld world = new GameWorld(map);
        world.setMovement(Direction.RIGHT, false);
        world.tick(0.5f);
        check(world.getPlayer().getPosition().equals(new GridPosition(0, 1)),
                "Player must not pass through a wall");
    }

    private static void testExitNeedsKeyAndKeyWins() {
        GameWorld lockedWorld = new GameWorld(basicMap());
        lockedWorld.setMovement(Direction.RIGHT, false);
        lockedWorld.tick(1.0f);
        check(!lockedWorld.getPlayer().getPosition().equals(new GridPosition(3, 1)),
                "Locked exit must behave like a wall");
        check(lockedWorld.getState() == GameState.RUNNING, "Locked exit must not cause victory");

        GameWorld world = new GameWorld(basicMap());
        world.addCollectable(new Key(1, 1));
        world.setMovement(Direction.RIGHT, false);
        world.tick(0.25f);
        check(world.getPlayer().hasKey(), "Player should collect the key on contact");
        world.tick(0.25f);
        world.tick(0.25f);
        check(world.getState() == GameState.VICTORY, "Player should win after reaching an unlocked exit");
        check(world.getScore() >= 600, "Key and victory should award score");
    }

    private static void testTrapContactDoesNotDrainLivesEveryFrame() {
        GridMap map = basicMap();
        map.setTile(new Trap(1, 1));
        GameWorld world = new GameWorld(map);

        world.setMovement(Direction.RIGHT, false);
        world.tick(0.25f);
        check(world.getPlayer().getLives() == 2, "One trap contact should remove one life");

        world.setMovement(null, false);
        world.tick(2.0f);
        check(world.getPlayer().getLives() == 2,
                "Standing on the same trap must not keep removing lives after the cooldown");

        world.setMovement(Direction.LEFT, false);
        world.tick(0.25f);
        world.setMovement(Direction.RIGHT, false);
        world.tick(0.25f);
        check(world.getPlayer().getLives() == 1,
                "Leaving and re-entering after cooldown should count as a new hit");
    }

    private static void testGameOverAfterThreeLogicalHits() {
        GridMap map = basicMap();
        map.setTile(new Trap(1, 1));
        GameWorld world = new GameWorld(map);

        for (int hit = 0; hit < 3; hit++) {
            world.setMovement(Direction.RIGHT, false);
            world.tick(0.25f);
            if (hit < 2) {
                check(world.getState() == GameState.RUNNING, "Game should continue while lives remain");
                world.setMovement(null, false);
                world.tick(1.1f);
                world.setMovement(Direction.LEFT, false);
                world.tick(0.25f);
            }
        }
        check(world.getPlayer().getLives() == 0, "All three lives should be used");
        check(world.getState() == GameState.GAME_OVER, "Zero lives should trigger game over");
    }

    private static void testPowerUpsAndExtraLife() {
        GameWorld world = new GameWorld(basicMap());
        world.addCollectable(new ExtraLife(0, 1));
        world.addCollectable(new PowerUp(1, 1, PowerUpType.SHIELD, 2.0f));

        world.tick(0.01f);
        check(world.getPlayer().getLives() == 4, "Extra life should increase lives by one");
        world.setMovement(Direction.RIGHT, false);
        world.tick(0.25f);
        check(world.getPlayer().hasShield(), "Shield power-up should activate on contact");
    }

    private static void testResetRestoresNewGameState() {
        GameWorld world = new GameWorld(basicMap());
        world.addCollectable(new Key(1, 1));
        world.setMovement(Direction.RIGHT, false);
        world.tick(0.25f);
        check(world.getPlayer().hasKey(), "Key should be collected before reset");
        world.addScore(50, "test");

        world.resetForNewGame();
        check(world.getState() == GameState.RUNNING, "Reset should return to running state");
        check(world.getPlayer().getPosition().equals(new GridPosition(0, 1)), "Reset should return to entry");
        check(world.getPlayer().getLives() == 3, "Reset should restore three lives");
        check(!world.getPlayer().hasKey(), "Reset should clear key inventory");
        check(world.getScore() == 0, "Reset should clear score");
        check(world.getCollectables().size() == 1, "Reset should restore collectables");
    }

    private static void testEventBusSubscription() {
        EventBus bus = new EventBus();
        eventCalls = 0;
        EventBus.Subscription subscription = bus.subscribe(
                GameEvent.ScoreChangedEvent.class, event -> eventCalls++);
        bus.publish(new GameEvent.ScoreChangedEvent(10, 10, "test"));
        subscription.close();
        bus.publish(new GameEvent.ScoreChangedEvent(20, 10, "test"));
        check(eventCalls == 1, "Unsubscribed listeners should not receive events");
    }

    private static GridMap basicMap() {
        GridMap map = new GridMap(4, 3);
        map.setTile(new Entry(0, 1));
        map.setTile(new Exit(3, 1));
        map.validate();
        return map;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
