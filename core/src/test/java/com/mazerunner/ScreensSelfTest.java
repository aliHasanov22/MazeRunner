package com.mazerunner;

import com.mazerunner.core.GameState;
import com.mazerunner.core.GameWorld;
import com.mazerunner.hud.HudMath;
import com.mazerunner.map.GridPosition;
import com.mazerunner.screens.MapNames;

public final class ScreensSelfTest {

    public static void main(String[] args) {
        check(HudMath.formatTime(0).equals("00:00"), "0 seconds");
        check(HudMath.formatTime(9.9f).equals("00:09"), "seconds are cut, not rounded");
        check(HudMath.formatTime(75.4f).equals("01:15"), "minutes and seconds");
        check(HudMath.formatTime(3661).equals("61:01"), "more than one hour");
        check(HudMath.formatTime(-5).equals("00:00"), "never negative");

        GridPosition origin = new GridPosition(3, 3);
        check(near(HudMath.angleBetween(origin, new GridPosition(8, 3)), 0), "right is 0");
        check(near(HudMath.angleBetween(origin, new GridPosition(3, 9)), 90), "up is 90");
        check(near(HudMath.angleBetween(origin, new GridPosition(3, 0)), -90), "down is -90");
        check(near(Math.abs(HudMath.angleBetween(origin, new GridPosition(0, 3))), 180), "left is 180");
        check(near(HudMath.angleBetween(origin, new GridPosition(5, 5)), 45), "diagonal is 45");

        check(MapNames.readable("level-1").equals("Level 1"), "level-1");
        check(MapNames.readable("big_maze").equals("Big Maze"), "big_maze");
        check(MapNames.readable("level--2").equals("Level 2"), "double dash");

        GameWorld world = DemoLevel.create();
        check(world.getState() == GameState.RUNNING, "demo level starts running");
        check(world.getPlayer().getPosition().equals(new GridPosition(0, 7)), "player starts on the entry");
        check(world.getMap().findNearestExit(world.getPlayer().getPosition()) != null, "demo level has an exit");
        check(world.getCollectables().size() == 1, "demo level has the key");

        System.out.println("Screens self-test passed");
    }

    private static boolean near(float actual, float expected) {
        return Math.abs(actual - expected) < 0.01f;
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
