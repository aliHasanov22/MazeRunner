package com.game.MazeRunner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mazerunner.core.GameWorld;
import com.mazerunner.entities.Entry;
import com.mazerunner.entities.Exit;
import com.mazerunner.entities.GameObject;
import com.mazerunner.entities.Key;
import com.mazerunner.entities.Trap;
import com.mazerunner.entities.Wall;
import com.mazerunner.input.Direction;
import com.mazerunner.map.GridMap;
import com.mazerunner.map.GridPosition;

/**
 * Temporary demo application that runs the core gameplay module in a window.
 */
public class Main extends ApplicationAdapter {
    private static final float CELL_SIZE = 64.0f;

    private ShapeRenderer shapeRenderer;
    private GameWorld world;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        world = createWorld();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.08f, 0.08f, 0.12f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        readMovement();
        world.tick(Math.min(Gdx.graphics.getDeltaTime(), 0.1f));

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawMap();
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }

    private GameWorld createWorld() {
        GridMap map = new GridMap(7, 5);
        map.setTile(new Entry(0, 2));
        map.setTile(new Exit(6, 2));
        map.setTile(new Wall(2, 1));
        map.setTile(new Wall(2, 2));
        map.setTile(new Wall(2, 3));
        map.setTile(new Trap(4, 2));

        GameWorld gameWorld = new GameWorld(map);
        gameWorld.addCollectable(new Key(1, 2));
        return gameWorld;
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

    private void drawMap() {
        GridMap map = world.getMap();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                float drawX = x * CELL_SIZE;
                float drawY = y * CELL_SIZE;
                Object tile = map.getTileAt(x, y);
                if (tile instanceof Wall) {
                    shapeRenderer.setColor(0.25f, 0.25f, 0.30f, 1.0f);
                } else if (tile instanceof Trap) {
                    shapeRenderer.setColor(0.70f, 0.20f, 0.20f, 1.0f);
                } else if (tile instanceof Exit) {
                    shapeRenderer.setColor(0.20f, 0.70f, 0.30f, 1.0f);
                } else if (tile instanceof Entry) {
                    shapeRenderer.setColor(0.20f, 0.45f, 0.70f, 1.0f);
                } else {
                    shapeRenderer.setColor(0.15f, 0.15f, 0.18f, 1.0f);
                }
                shapeRenderer.rect(drawX + 2.0f, drawY + 2.0f, CELL_SIZE - 4.0f, CELL_SIZE - 4.0f);
            }
        }

        for (GameObject item : world.getCollectables()) {
            GridPosition position = item.getPosition();
            shapeRenderer.setColor(1.0f, 0.85f, 0.20f, 1.0f);
            shapeRenderer.circle(
                    position.x() * CELL_SIZE + CELL_SIZE / 2.0f,
                    position.y() * CELL_SIZE + CELL_SIZE / 2.0f,
                    12.0f);
        }

        GridPosition playerPosition = world.getPlayer().getPosition();
        shapeRenderer.setColor(0.20f, 0.75f, 1.0f, 1.0f);
        shapeRenderer.circle(
                playerPosition.x() * CELL_SIZE + CELL_SIZE / 2.0f,
                playerPosition.y() * CELL_SIZE + CELL_SIZE / 2.0f,
                16.0f);
    }
}
