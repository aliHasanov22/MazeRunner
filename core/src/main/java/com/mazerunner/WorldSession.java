package com.mazerunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mazerunner.core.GameState;
import com.mazerunner.core.GameWorld;
import com.mazerunner.entities.Enemy;
import com.mazerunner.entities.Entry;
import com.mazerunner.entities.Exit;
import com.mazerunner.entities.GameObject;
import com.mazerunner.entities.Key;
import com.mazerunner.entities.Player;
import com.mazerunner.entities.Trap;
import com.mazerunner.entities.Wall;
import com.mazerunner.input.Direction;
import com.mazerunner.map.GridMap;
import com.mazerunner.map.GridPosition;

public class WorldSession implements GameSession {

    private static final Color FLOOR = new Color(0.15f, 0.15f, 0.18f, 1f);
    private static final Color WALL = new Color(0.3f, 0.3f, 0.36f, 1f);
    private static final Color TRAP = new Color(0.7f, 0.2f, 0.2f, 1f);
    private static final Color EXIT = new Color(0.2f, 0.7f, 0.3f, 1f);
    private static final Color ENTRY = new Color(0.2f, 0.45f, 0.7f, 1f);
    private static final Color KEY = new Color(1f, 0.85f, 0.2f, 1f);
    private static final Color ITEM = new Color(0.9f, 0.4f, 0.8f, 1f);
    private static final Color ENEMY = new Color(1f, 0.4f, 0.2f, 1f);
    private static final Color PLAYER = new Color(0.2f, 0.75f, 1f, 1f);

    private final GameWorld world;
    private final OrthographicCamera camera = new OrthographicCamera();
    private final ExtendViewport viewport;
    private final ShapeRenderer shapes = new ShapeRenderer();

    public WorldSession(GameWorld world) {
        this.world = world;
        GridMap map = world.getMap();
        viewport = new ExtendViewport(map.getWidth() + 2, map.getHeight() + 2, camera);
        camera.position.set(map.getWidth() / 2f, map.getHeight() / 2f, 0);
    }

    @Override
    public void tick(float delta) {
        readMovement();
        world.tick(delta);
    }

    private void readMovement() {
        boolean sprint = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
        Direction direction = null;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            direction = Direction.UP;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            direction = Direction.DOWN;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction = Direction.LEFT;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            direction = Direction.RIGHT;
        }
        world.setMovement(direction, sprint);
    }

    @Override
    public void render() {
        viewport.apply();
        camera.update();
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        drawTiles();
        drawItems();
        for (Enemy enemy : world.getMap().getEnemies()) {
            drawCircle(enemy.getPosition(), 0.35f, ENEMY);
        }
        drawCircle(world.getPlayer().getPosition(), 0.4f, PLAYER);
        shapes.end();
    }

    private void drawTiles() {
        GridMap map = world.getMap();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                GameObject tile = map.getTileAt(x, y);
                shapes.setColor(colorOf(tile));
                shapes.rect(x + 0.04f, y + 0.04f, 0.92f, 0.92f);
            }
        }
    }

    private Color colorOf(GameObject tile) {
        if (tile instanceof Wall) {
            return WALL;
        } else if (tile instanceof Trap) {
            return TRAP;
        } else if (tile instanceof Exit) {
            return EXIT;
        } else if (tile instanceof Entry) {
            return ENTRY;
        }
        return FLOOR;
    }

    private void drawItems() {
        for (GameObject item : world.getCollectables()) {
            drawCircle(item.getPosition(), 0.25f, item instanceof Key ? KEY : ITEM);
        }
    }

    private void drawCircle(GridPosition position, float radius, Color color) {
        shapes.setColor(color);
        shapes.circle(position.x() + 0.5f, position.y() + 0.5f, radius, 20);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        world.pause();
    }

    @Override
    public void resume() {
        world.resume();
    }

    @Override
    public boolean isWon() {
        return world.getState() == GameState.VICTORY;
    }

    @Override
    public boolean isLost() {
        return world.getState() == GameState.GAME_OVER;
    }

    @Override
    public int getLives() {
        return world.getPlayer().getLives();
    }

    @Override
    public int getMaxLives() {
        return Math.max(Player.DEFAULT_LIVES, getLives());
    }

    @Override
    public boolean hasKey() {
        return world.getPlayer().hasKey();
    }

    @Override
    public int getScore() {
        return world.getScore();
    }

    @Override
    public float getPlayTime() {
        return world.getElapsedSeconds();
    }

    @Override
    public GridPosition getPlayerPosition() {
        return world.getPlayer().getPosition();
    }

    @Override
    public GridPosition getNearestExit() {
        Exit exit = world.getMap().findNearestExit(getPlayerPosition());
        return exit == null ? null : exit.getPosition();
    }

    @Override
    public void dispose() {
        shapes.dispose();
    }
}
