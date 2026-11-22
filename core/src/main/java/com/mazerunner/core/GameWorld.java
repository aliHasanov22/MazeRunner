package com.mazerunner.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mazerunner.collision.CollisionResolver;
import com.mazerunner.entities.Collectable;
import com.mazerunner.entities.Damaging;
import com.mazerunner.entities.Enemy;
import com.mazerunner.entities.Entry;
import com.mazerunner.entities.GameObject;
import com.mazerunner.entities.Player;
import com.mazerunner.events.EventBus;
import com.mazerunner.events.GameEvent;
import com.mazerunner.input.Direction;
import com.mazerunner.map.GridMap;
import com.mazerunner.map.GridPosition;

/**
 * Main controller for one game session: runs the game loop, score,
 * damage, victory, game over, pause and reset.
 *
 * @author Ali Hasanov
 */
public final class GameWorld {

    private static final int VICTORY_SCORE = 500;
    private static final int POINTS_PER_LIFE = 100;
    private static final int MAX_TIME_BONUS = 600;
    private static final int POINTS_PER_SECOND = 5;

    private final GridMap map;
    private final Player player;
    private final EventBus eventBus = new EventBus();
    private final CollisionResolver collisionResolver = new CollisionResolver();
    private final List<GameObject> collectables = new ArrayList<>();
    private final List<GameObject> startingCollectables = new ArrayList<>();

    private GameState state = GameState.RUNNING;
    private int score;
    private float elapsedSeconds;

    /** Creates a new game on the given map. The player starts on the Entry tile with 3 lives. */
    public GameWorld(GridMap map) {
        this.map = map;
        Entry entry = map.findEntry();
        this.player = new Player(entry.getX(), entry.getY(), Player.DEFAULT_LIVES);
    }

    /** Returns the map. */
    public GridMap getMap() {
        return map;
    }

    /** Returns the player. */
    public Player getPlayer() {
        return player;
    }

    /** Returns the event bus that UI and audio can subscribe to. */
    public EventBus getEventBus() {
        return eventBus;
    }

    /** Returns the current game state. */
    public GameState getState() {
        return state;
    }

    /** Returns the current score. */
    public int getScore() {
        return score;
    }

    /** Returns the played time in seconds (paused time is not counted). */
    public float getElapsedSeconds() {
        return elapsedSeconds;
    }

    /** Returns a read-only list of items still on the map. */
    public List<GameObject> getCollectables() {
        return Collections.unmodifiableList(collectables);
    }

    /** Adds a Key, ExtraLife or PowerUp to the map. */
    public void addCollectable(GameObject item) {
        if (!(item instanceof Collectable)) {
            throw new IllegalArgumentException("Item must be Collectable");
        }
        collectables.add(item);
        startingCollectables.add(item);
    }

    /** Removes an item after it is picked up. */
    public void removeCollectable(GameObject item) {
        collectables.remove(item);
    }

    /** Passes the held direction (or null) and sprint key to the player while running. */
    public void setMovement(Direction direction, boolean sprint) {
        if (state == GameState.RUNNING) {
            player.setMovement(direction, sprint);
        }
    }

    /** Pauses the game and stops the player. */
    public void pause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
            player.setMovement(null, false);
        }
    }

    /** Continues a paused game. */
    public void resume() {
        if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    /** Goes to the menu from pause, victory or game over. */
    public void returnToMenu() {
        if (state != GameState.RUNNING) {
            state = GameState.MENU;
            player.setMovement(null, false);
        }
    }

    /** Restarts the same map: player, enemies, items, score, timer and cooldown are reset. */
    public void resetForNewGame() {
        Entry entry = map.findEntry();
        player.resetForNewGame(entry.getX(), entry.getY(), Player.DEFAULT_LIVES);
        map.resetEnemies();
        collectables.clear();
        collectables.addAll(startingCollectables);
        score = 0;
        elapsedSeconds = 0;
        collisionResolver.reset();
        state = GameState.RUNNING;
        eventBus.publish(new GameEvent.NewGameStartedEvent());
    }

    /** Updates the game by deltaSeconds. Does nothing unless the game is RUNNING. */
    public void tick(float deltaSeconds) {
        if (state != GameState.RUNNING || deltaSeconds <= 0) {
            return;
        }
        elapsedSeconds += deltaSeconds;
        player.updateEffects(deltaSeconds);
        collisionResolver.update(deltaSeconds);

        for (Enemy enemy : map.getEnemies()) {
            enemy.update(deltaSeconds, this);
        }
        collisionResolver.resolve(this);

        player.addMovementTime(deltaSeconds);
        while (state == GameState.RUNNING && player.canMoveOneCell()) {
            GridPosition next = player.getPosition().move(player.getDirection());
            if (!canPlayerEnter(next)) {
                player.clearMovementTime();
                break;
            }
            player.stepTo(next);
            collisionResolver.resolve(this);
        }
    }

    /** Returns true if the player can walk onto this cell (walls and locked exits block). */
    public boolean canPlayerEnter(GridPosition position) {
        GameObject tile = map.getTileAt(position.x(), position.y());
        return tile != null && !tile.blocksMovement(player.hasKey());
    }

    /** Returns true if an enemy can walk onto this cell (no walls, exits or other enemies). */
    public boolean canEnemyEnter(GridPosition position, Enemy movingEnemy) {
        GameObject tile = map.getTileAt(position.x(), position.y());
        if (tile == null || tile.blocksMovement(false)) {
            return false;
        }
        for (Enemy other : map.getEnemies()) {
            if (other != movingEnemy && other.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    /** Adds points to the score and publishes a ScoreChangedEvent. */
    public void addScore(int points, String reason) {
        score += points;
        eventBus.publish(new GameEvent.ScoreChangedEvent(score, points, reason));
    }

    /** Removes lives from the player (unless shielded). Switches to GAME_OVER at 0 lives. */
    public void damagePlayer(Damaging hazard, GridPosition position, String sourceName) {
        if (state != GameState.RUNNING || player.hasShield()) {
            return;
        }
        int lost = player.loseLives(hazard.getDamageAmount());
        eventBus.publish(new GameEvent.LifeLostEvent(sourceName, position, player.getLives(), lost));
        if (player.getLives() <= 0) {
            state = GameState.GAME_OVER;
            player.setMovement(null, false);
            eventBus.publish(new GameEvent.GameOverEvent(score));
        }
    }

    /** Ends the game as a win and adds the victory, time and life bonuses. */
    public void finishVictory() {
        if (state != GameState.RUNNING) {
            return;
        }
        state = GameState.VICTORY;
        player.setMovement(null, false);
        int secondsLeft = Math.max(0, MAX_TIME_BONUS - (int) elapsedSeconds);
        addScore(VICTORY_SCORE + secondsLeft * POINTS_PER_SECOND + player.getLives() * POINTS_PER_LIFE,
                "Victory bonus");
        eventBus.publish(new GameEvent.VictoryEvent(score, elapsedSeconds, player.getLives()));
    }
}
