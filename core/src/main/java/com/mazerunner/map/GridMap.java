package com.mazerunner.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mazerunner.entities.Enemy;
import com.mazerunner.entities.Entry;
import com.mazerunner.entities.Exit;
import com.mazerunner.entities.FreePath;
import com.mazerunner.entities.GameObject;

/**
 * Stores the 2D grid of maze tiles and the list of moving enemies.
 * File parsing and map validation are done by the map loader.
 *
 * @author Ali Hasanov
 */
public final class GridMap {

    private final int width;
    private final int height;
    private final GameObject[][] cells;
    private final List<Enemy> enemies = new ArrayList<>();

    /** Creates a map of size width x height filled with FreePath cells. */
    public GridMap(int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Map width and height must be positive");
        }
        this.width = width;
        this.height = height;
        this.cells = new GameObject[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new FreePath(x, y);
            }
        }
    }

    /** Returns the map width in cells. */
    public int getWidth() {
        return width;
    }

    /** Returns the map height in cells. */
    public int getHeight() {
        return height;
    }

    /** Returns true if (x, y) is inside the map. */
    public boolean isInside(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    /** Returns the tile at (x, y), or null if outside the map. */
    public GameObject getTileAt(int x, int y) {
        if (!isInside(x, y)) {
            return null;
        }
        return cells[x][y];
    }

    /** Places an object on the grid. Enemies go into the enemy list and leave a FreePath underneath. */
    public void setTile(GameObject object) {
        int x = object.getX();
        int y = object.getY();
        if (!isInside(x, y)) {
            throw new IllegalArgumentException("Object is outside the map: " + object.getPosition());
        }
        if (object instanceof Enemy) {
            enemies.add((Enemy) object);
            cells[x][y] = new FreePath(x, y);
        } else {
            cells[x][y] = object;
        }
    }

    /** Returns a read-only list of enemies on the map. */
    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    /** Finds the Entry tile where the player starts. */
    public Entry findEntry() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (cells[x][y] instanceof Entry) {
                    return (Entry) cells[x][y];
                }
            }
        }
        throw new IllegalStateException("Map does not contain an entry");
    }

    /** Finds the exit closest to the given position (used by the HUD arrow). */
    public Exit findNearestExit(GridPosition from) {
        Exit nearest = null;
        int bestDistance = Integer.MAX_VALUE;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (cells[x][y] instanceof Exit) {
                    int distance = Math.abs(x - from.x()) + Math.abs(y - from.y());
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        nearest = (Exit) cells[x][y];
                    }
                }
            }
        }
        return nearest;
    }

    /** Puts all enemies back on their starting cells. */
    public void resetEnemies() {
        for (Enemy enemy : enemies) {
            enemy.resetForNewGame();
        }
    }
}
