package com.mazerunner.map;

import com.mazerunner.entities.Enemy;
import com.mazerunner.entities.Entry;
import com.mazerunner.entities.Exit;
import com.mazerunner.entities.FreePath;
import com.mazerunner.entities.GameObject;
import com.mazerunner.entities.Trap;
import com.mazerunner.entities.Wall;

/**
 * Factory that creates maze objects from the integer type IDs (0 to 5)
 * used in .properties map files.
 *
 * @author Ali Hasanov
 */
public final class ObjectFactory {

    public static final int FREE_PATH_ID = 0;
    public static final int WALL_ID = 1;
    public static final int ENTRY_ID = 2;
    public static final int EXIT_ID = 3;
    public static final int TRAP_ID = 4;
    public static final int ENEMY_ID = 5;

    /** Creates the GameObject for typeId (0..5) at grid coordinate (x, y). */
    public GameObject create(int typeId, int x, int y) {
        switch (typeId) {
            case FREE_PATH_ID:
                return new FreePath(x, y);
            case WALL_ID:
                return new Wall(x, y);
            case ENTRY_ID:
                return new Entry(x, y);
            case EXIT_ID:
                return new Exit(x, y);
            case TRAP_ID:
                return new Trap(x, y);
            case ENEMY_ID:
                return new Enemy(x, y);
            default:
                throw new IllegalArgumentException("Unknown map object type ID: " + typeId);
        }
    }
}
