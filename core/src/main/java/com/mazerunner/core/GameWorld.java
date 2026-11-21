package com.mazerunner.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mazerunner.collision.CollisionResolver;
import com.mazerunner.entities.Collectable;
import com.mazerunner.entities.Damaging;
import com.mazerunner.entities.Enemy;
import com.mazerunner.entities.Entry;
import com.mazerunner.entities.ExtraLife;
import com.mazerunner.entities.GameObject;
import com.mazerunner.entities.Key;
import com.mazerunner.entities.Player;
import com.mazerunner.entities.PowerUp;
import com.mazerunner.events.EventBus;
import com.mazerunner.events.GameEvent;
import com.mazerunner.input.Direction;
import com.mazerunner.map.GridMap;
import com.mazerunner.map.GridPosition;

/**
 * Main controller for a Maze Runner game session.
 * Manages the map, player, enemies, collisions, score, timer, and game states.
 *
 * @author Ali Hasanov
 */
public final class GameWorld {

    private static final int VICTORY_BASE_SCORE = 500;
    private static final int POINTS_PER_REMAINING_LIFE = 100;
    private static final int MAX_TIME_BONUS = 600;
    private static final int POINTS_PER_REMAINING_SECOND = 5;

    private final GridMap map;
    private final Player player;
    private final int startingLives;
    private final EventBus eventBus;
    private final CollisionResolver collisionResolver;
    private final List<GameObject> collectables = new ArrayList<>();
    private final List<GameObject> collectableTemplates = new ArrayList<>();

    private GameState state = GameState.RUNNING;
    private int score = 0;
    private float elapsedSeconds = 0.0f;

    /** Creates a new game world with the default 3 lives and a new EventBus. */
    public GameWorld(GridMap map) {
        this(map, Player.DEFAULT_LIVES, new EventBus());
    }

    /** Creates a new game world with a custom number of lives and EventBus. */
    public GameWorld(GridMap map, int startingLives, EventBus eventBus) {
        if (map == null) {
            throw new IllegalArgumentException("Map cannot be null");
        }
        if (eventBus == null) {
            throw new IllegalArgumentException("Event bus cannot be null");
        }
        map.validate();
        Entry entry = map.findEntry();

        this.map = map;
        this.startingLives = startingLives;
        this.player = new Player(entry.getX(), entry.getY(), startingLives);
        this.eventBus = eventBus;
        this.collisionResolver = new CollisionResolver();
    }

    /** Returns the current maze map. */
    public GridMap getMap() {
        return map;
    }

    /** Returns the player character. */
    public Player getPlayer() {
        return player;
    }

    /** Returns the event bus used to notify UI and audio listeners. */
    public EventBus getEventBus() {
        return eventBus;
    }

    /** Returns the collision resolver used by this world. */
    public CollisionResolver getCollisionResolver() {
        return collisionResolver;
    }

    /** Returns the current game state. */
    public GameState getState() {
        return state;
    }

    /** Returns the player's current score. */
    public int getScore() {
        return score;
    }

    /** Returns how many seconds the game has been running (excluding paused time). */
    public float getElapsedSeconds() {
        return elapsedSeconds;
    }

    /** Returns a read-only list of active collectables still on the map. */
    public List<GameObject> getCollectables() {
        return Collections.unmodifiableList(collectables);
    }

    /** Adds a collectable item to the map and saves a copy so it can be restored on reset. */
    public void addCollectable(GameObject item) {
        if (item == null || !(item instanceof Collectable)) {
            throw new IllegalArgumentException("Item must be a Collectable object");
        }
        if (!map.isInside(item.getX(), item.getY())) {
            throw new IllegalArgumentException("Collectable is outside map bounds: " + item.getPosition());
        }
        collectables.add(item);
        collectableTemplates.add(copyCollectable(item));
    }

    /** Removes a collectable from the map after it is picked up. */
    public void removeCollectable(GameObject item) {
        collectables.remove(item);
    }

    /** Updates the player's held movement direction and sprint flag while the game is running. */
    public void setMovement(Direction direction, boolean sprint) {
        if (state == GameState.RUNNING) {
            player.setMovement(direction, sprint);
        }
    }

    /** Pauses the game and clears any held movement input. */
    public void pause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
            player.setMovement(null, false);
        }
    }

    /** Resumes the game from the paused state. */
    public void resume() {
        if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    /** Switches the state to MENU from PAUSED, VICTORY, or GAME_OVER. */
    public void returnToMenu() {
        if (state == GameState.VICTORY || state == GameState.GAME_OVER || state == GameState.PAUSED) {
            state = GameState.MENU;
            player.setMovement(null, false);
        }
    }

    /** Resets the player, enemies, collectables, score, and timer for a fresh game on the same map. */
    public void resetForNewGame() {
        Entry entry = map.findEntry();
        player.resetForNewGame(entry.getX(), entry.getY(), startingLives);
        map.resetEnemies();

        collectables.clear();
        for (GameObject template : collectableTemplates) {
            collectables.add(copyCollectable(template));
        }

        score = 0;
        elapsedSeconds = 0.0f;
        collisionResolver.reset();
        state = GameState.RUNNING;
        eventBus.publish(new GameEvent.NewGameStartedEvent());
    }

    /** Advances the game simulation by deltaSeconds while the game state is RUNNING. */
    public void tick(float deltaSeconds) {
        if (deltaSeconds < 0.0f) {
            throw new IllegalArgumentException("Delta time cannot be negative");
        }
        if (deltaSeconds == 0.0f || state != GameState.RUNNING) {
            return;
        }

        elapsedSeconds += deltaSeconds;
        player.updateEffects(deltaSeconds);
        collisionResolver.update(deltaSeconds);

        for (Enemy enemy : map.getEnemies()) {
            enemy.update(deltaSeconds, this);
        }
        collisionResolver.resolve(this);
        if (state != GameState.RUNNING) {
            return;
        }

        player.addMovementTime(deltaSeconds);
        while (player.canMoveOneCell() && state == GameState.RUNNING) {
            Direction direction = player.getRequestedDirection();
            GridPosition next = player.getPosition().move(direction);
            if (!canPlayerEnter(next)) {
                player.clearMovementTime();
                break;
            }

            player.moveTo(next);
            player.consumeMovementStep();
            collisionResolver.resolve(this);
        }
    }

    /** Returns true if the player is allowed to move onto the given grid cell. */
    public boolean canPlayerEnter(GridPosition position) {
        if (position == null || !map.isInside(position.x(), position.y())) {
            return false;
        }
        GameObject tile = map.getTileAt(position.x(), position.y());
        return !tile.blocksMovement(player.hasKey());
    }

    /** Returns true if an enemy is allowed to move onto the given grid cell. */
    public boolean canEnemyEnter(GridPosition position, Enemy movingEnemy) {
        if (position == null || !map.isInside(position.x(), position.y())) {
            return false;
        }
        GameObject tile = map.getTileAt(position.x(), position.y());
        if (tile.blocksMovement(false)) {
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
        if (points < 0) {
            throw new IllegalArgumentException("Score points cannot be negative");
        }
        if (reason == null || reason.isBlank()) {
            reason = "Gameplay points";
        }
        score += points;
        eventBus.publish(new GameEvent.ScoreChangedEvent(score, points, reason));
    }

    /** Applies hazard damage to the player and switches to GAME_OVER if no lives remain. */
    public void damagePlayer(Damaging hazard, GridPosition position, String sourceName) {
        if (state != GameState.RUNNING || player.hasShield()) {
            return;
        }
        int livesLost = player.loseLives(hazard.getDamageAmount());
        if (livesLost == 0) {
            return;
        }
        eventBus.publish(new GameEvent.LifeLostEvent(sourceName, position, player.getLives(), livesLost));
        if (player.getLives() <= 0) {
            state = GameState.GAME_OVER;
            player.setMovement(null, false);
            eventBus.publish(new GameEvent.GameOverEvent(score));
        }
    }

    /** Ends the level as a victory and awards the completion, time, and remaining-life bonuses. */
    public void finishVictory() {
        if (state != GameState.RUNNING) {
            return;
        }
        state = GameState.VICTORY;
        player.setMovement(null, false);

        int remainingTime = Math.max(0, MAX_TIME_BONUS - (int) Math.ceil(elapsedSeconds));
        int timeBonus = remainingTime * POINTS_PER_REMAINING_SECOND;
        int lifeBonus = player.getLives() * POINTS_PER_REMAINING_LIFE;
        addScore(VICTORY_BASE_SCORE + timeBonus + lifeBonus, "Victory bonus");
        eventBus.publish(new GameEvent.VictoryEvent(score, elapsedSeconds, player.getLives()));
    }

    private GameObject copyCollectable(GameObject item) {
        if (item instanceof Key) {
            return new Key(item.getX(), item.getY());
        } else if (item instanceof ExtraLife) {
            return new ExtraLife(item.getX(), item.getY());
        } else if (item instanceof PowerUp) {
            PowerUp powerUp = (PowerUp) item;
            return new PowerUp(powerUp.getX(), powerUp.getY(), powerUp.getType(), powerUp.getDurationSeconds());
        }
        throw new IllegalArgumentException("Unsupported collectable type: " + item.getClass().getName());
    }
}
