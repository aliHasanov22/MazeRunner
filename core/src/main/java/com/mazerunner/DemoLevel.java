package com.mazerunner;

import com.mazerunner.core.GameWorld;
import com.mazerunner.entities.Key;
import com.mazerunner.map.GridMap;
import com.mazerunner.map.ObjectFactory;

public final class DemoLevel {

    private static final String[] ROWS = {
            "111111111111111",
            "200000100000001",
            "101110101111101",
            "100010001000101",
            "111010111010101",
            "100000100010003",
            "101111101110111",
            "100040000050001",
            "111111111111111"
    };

    private DemoLevel() {
    }

    public static GameWorld create() {
        int width = ROWS[0].length();
        int height = ROWS.length;
        GridMap map = new GridMap(width, height);
        ObjectFactory factory = new ObjectFactory();
        for (int row = 0; row < height; row++) {
            int y = height - 1 - row;
            for (int x = 0; x < width; x++) {
                int typeId = ROWS[row].charAt(x) - '0';
                map.setTile(factory.create(typeId, x, y));
            }
        }
        GameWorld world = new GameWorld(map);
        world.addCollectable(new Key(13, 7));
        return world;
    }
}
