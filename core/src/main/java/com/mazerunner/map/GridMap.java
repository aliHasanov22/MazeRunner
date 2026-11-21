package com.mazerunner.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.mazerunner.entities.Enemy;
import com.mazerunner.entities.Entry;
import com.mazerunner.entities.Exit;
import com.mazerunner.entities.FreePath;
import com.mazerunner.entities.GameObject;
import com.mazerunner.input.Direction;

/**
 * Stores the 2D grid of maze tiles and the list of moving enemies,
 * and validates basic map rules (entry, border exit, and exit reachability).
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

    /** Returns true if (x, y) is inside the map boundaries. */
    public boolean isInside(int x, int y) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    /** Returns the tile at (x, y), or null if out of bounds. */
    public GameObject getTileAt(int x, int y) {
        if (!isInside(x, y)) {
            return null;
        }
        return cells[x][y];
    }

    /**
     * Places an object on the grid. If the object is an Enemy, it is added to
     * the enemies list and a FreePath tile is placed on its cell.
     */
    public void setTile(GameObject object) {
        if (object == null) {
            throw new IllegalArgumentException("Map object cannot be null");
        }
        int x = object.getX();
        int y = object.getY();
        if (!isInside(x, y)) {
            throw new IllegalArgumentException("Object position is outside the map: " + object.getPosition());
        }

        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy existing = enemies.get(i);
            if (existing.getX() == x && existing.getY() == y) {
                enemies.remove(i);
            }
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

    /** Returns a list containing all terrain tiles followed by all enemies. */
    public List<GameObject> getAllObjects() {
        List<GameObject> objects = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                objects.add(cells[x][y]);
            }
        }
        objects.addAll(enemies);
        return Collections.unmodifiableList(objects);
    }

    /** Finds and returns the single Entry tile on the map. */
    public Entry findEntry() {
        Entry found = null;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (cells[x][y] instanceof Entry) {
                    if (found != null) {
                        throw new IllegalStateException("Map must contain exactly one entry");
                    }
                    found = (Entry) cells[x][y];
                }
            }
        }
        if (found == null) {
            throw new IllegalStateException("Map does not contain an entry");
        }
        return found;
    }

    /** Returns all Exit tiles on the map. */
    public List<Exit> getExits() {
        List<Exit> exits = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (cells[x][y] instanceof Exit) {
                    exits.add((Exit) cells[x][y]);
                }
            }
        }
        return Collections.unmodifiableList(exits);
    }

    /** Finds the exit with the smallest Manhattan distance from the given position. */
    public Exit findNearestExit(GridPosition from) {
        Exit nearest = null;
        int bestDistance = Integer.MAX_VALUE;
        for (Exit exit : getExits()) {
            int distance = Math.abs(exit.getX() - from.x()) + Math.abs(exit.getY() - from.y());
            if (distance < bestDistance) {
                bestDistance = distance;
                nearest = exit;
            }
        }
        return nearest;
    }

    /** Validates that the map has 1 entry, at least 1 border exit, and a valid path to an exit. */
    public void validate() {
        Entry entry = findEntry();
        List<Exit> exits = getExits();
        if (exits.isEmpty()) {
            throw new IllegalStateException("Map must contain at least one exit");
        }
        for (Exit exit : exits) {
            boolean onBorder = exit.getX() == 0 || exit.getY() == 0
                    || exit.getX() == width - 1 || exit.getY() == height - 1;
            if (!onBorder) {
                throw new IllegalStateException("Every exit must be on the outer map border: " + exit.getPosition());
            }
        }
        if (!isExitReachable(entry.getPosition())) {
            throw new IllegalStateException("No exit is reachable from the entry");
        }
    }

    /** Returns true if a cell is inside the map and does not block movement. */
    public boolean isWalkable(int x, int y, boolean hasKey) {
        if (!isInside(x, y)) {
            return false;
        }
        return !cells[x][y].blocksMovement(hasKey);
    }

    /** Resets all enemies back to their starting positions. */
    public void resetEnemies() {
        for (Enemy enemy : enemies) {
            enemy.resetForNewGame();
        }
    }

    private boolean isExitReachable(GridPosition start) {
        boolean[][] visited = new boolean[width][height];
        Queue<GridPosition> queue = new LinkedList<>();
        queue.add(start);
        visited[start.x()][start.y()] = true;

        while (!queue.isEmpty()) {
            GridPosition current = queue.remove();
            if (cells[current.x()][current.y()] instanceof Exit) {
                return true;
            }
            for (Direction direction : Direction.values()) {
                GridPosition next = current.move(direction);
                if (!isInside(next.x(), next.y()) || visited[next.x()][next.y()]) {
                    continue;
                }
                if (!isWalkable(next.x(), next.y(), true)) {
                    continue;
                }
                visited[next.x()][next.y()] = true;
                queue.add(next);
            }
        }
        return false;
    }
}
