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

    /** Map type ID ({@code 0}) for a walkable {@link FreePath} cell. */
    public static final int FREE_PATH_ID = 0;

    /** Map type ID ({@code 1}) for a solid {@link Wall} cell. */
    public static final int WALL_ID = 1;

    /** Map type ID ({@code 2}) for the player {@link Entry} cell. */
    public static final int ENTRY_ID = 2;

    /** Map type ID ({@code 3}) for a border {@link Exit} cell. */
    public static final int EXIT_ID = 3;

    /** Map type ID ({@code 4}) for a static {@link Trap} cell. */
    public static final int TRAP_ID = 4;

    /** Map type ID ({@code 5}) for a moving {@link Enemy} entity. */
    public static final int ENEMY_ID = 5;

    /**
     * Instantiates the {@link GameObject} corresponding to {@code typeId} at {@code (x, y)}.
     *
     * @param typeId numeric map object ID ({@code 0} through {@code 5})
     * @param x      horizontal grid coordinate
     * @param y      vertical grid coordinate
     * @return newly created {@link GameObject} for the given ID
     * @throws IllegalArgumentException if {@code typeId} is not between {@code 0} and {@code 5}
     */
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
