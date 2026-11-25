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

    /**
     * Creates a map of size {@code width x height} filled with {@link FreePath} cells.
     *
     * @param width  map width in cells (must be positive)
     * @param height map height in cells (must be positive)
     * @throws IllegalArgumentException if {@code width < 1} or {@code height < 1}
     */
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

    /**
     * Returns the map width in grid cells.
     *
     * @return number of columns in the grid
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the map height in grid cells.
     *
     * @return number of rows in the grid
     */
    public int getHeight() {
        return height;
    }

    /**
     * Checks whether the coordinates {@code (x, y)} lie inside the map bounds.
     *
     * @param x horizontal grid coordinate
     * @param y vertical grid coordinate
     * @return {@code true} if {@code 0 <= x < width} and {@code 0 <= y < height}
     */
    public boolean isInside(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    /**
     * Returns the static terrain tile at {@code (x, y)}, or {@code null} if out of bounds.
     *
     * @param x horizontal grid coordinate
     * @param y vertical grid coordinate
     * @return terrain {@link GameObject} at {@code (x, y)}, or {@code null}
     */
    public GameObject getTileAt(int x, int y) {
        if (!isInside(x, y)) {
            return null;
        }
        return cells[x][y];
    }

    /**
     * Places an object onto the map at its coordinates.
     * If the object is an {@link Enemy}, it is added to the moving enemy list and
     * a walkable {@link FreePath} tile is placed underneath it.
     *
     * @param object the maze tile or enemy to place
     * @throws IllegalArgumentException if {@code object} lies outside the map bounds
     */
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

    /**
     * Returns an unmodifiable view of all moving enemies on the map.
     *
     * @return read-only list of enemies
     */
    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    /**
     * Scans the grid and returns the {@link Entry} tile where the player spawns.
     *
     * @return the map's {@link Entry} tile
     * @throws IllegalStateException if no {@link Entry} tile exists on the map
     */
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

    /**
     * Finds the {@link Exit} tile with the smallest Manhattan distance from {@code from}.
     *
     * @param from reference grid position (typically the player's position)
     * @return nearest {@link Exit} tile, or {@code null} if the map has no exits
     */
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

    /**
     * Resets every enemy on the map back to its original spawn cell.
     */
    public void resetEnemies() {
        for (Enemy enemy : enemies) {
            enemy.resetForNewGame();
        }
    }
}
