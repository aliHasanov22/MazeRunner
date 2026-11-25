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

    /**
     * Creates a new game session on the given map.
     * Finds the map's {@link Entry} tile and spawns the player there with the default 3 lives.
     *
     * @param map the maze grid to play on
     * @throws IllegalStateException if the map does not contain an entry tile
     */
    public GameWorld(GridMap map) {
        this.map = map;
        Entry entry = map.findEntry();
        this.player = new Player(entry.getX(), entry.getY(), Player.DEFAULT_LIVES);
    }

    /**
     * Returns the maze map used by this game session.
     *
     * @return the current {@link GridMap}
     */
    public GridMap getMap() {
        return map;
    }

    /**
     * Returns the player entity for this game session.
     *
     * @return the active {@link Player}
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the event bus that UI, HUD, and audio modules can subscribe to.
     *
     * @return the {@link EventBus} for this game world
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Returns the current lifecycle state of the game.
     *
     * @return the active {@link GameState}
     */
    public GameState getState() {
        return state;
    }

    /**
     * Returns the player's current score.
     *
     * @return total score points accumulated so far
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the total active play time in seconds, excluding paused time.
     *
     * @return elapsed play time in seconds
     */
    public float getElapsedSeconds() {
        return elapsedSeconds;
    }

    /**
     * Returns an unmodifiable view of collectable items still remaining on the map.
     *
     * @return read-only list of active collectable objects
     */
    public List<GameObject> getCollectables() {
        return Collections.unmodifiableList(collectables);
    }

    /**
     * Adds a collectable item (such as a Key, ExtraLife, or PowerUp) to the map
     * and stores it so it can be restored when restarting the game.
     *
     * @param item the collectable game object to place on the map
     * @throws IllegalArgumentException if {@code item} does not implement {@link Collectable}
     */
    public void addCollectable(GameObject item) {
        if (!(item instanceof Collectable)) {
            throw new IllegalArgumentException("Item must be Collectable");
        }
        collectables.add(item);
        startingCollectables.add(item);
    }

    /**
     * Removes a collectable item from the active map list after it is picked up.
     *
     * @param item the collected item to remove
     */
    public void removeCollectable(GameObject item) {
        collectables.remove(item);
    }

    /**
     * Passes the currently held movement direction and sprint flag to the player.
     * Ignored when the game is not in {@link GameState#RUNNING}.
     *
     * @param direction the held movement direction, or {@code null} to stop moving
     * @param sprint    {@code true} if the sprint key is held
     */
    public void setMovement(Direction direction, boolean sprint) {
        if (state == GameState.RUNNING) {
            player.setMovement(direction, sprint);
        }
    }

    /**
     * Pauses an active game and clears any held player movement so the player
     * does not move unexpectedly on resume.
     */
    public void pause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
            player.setMovement(null, false);
        }
    }

    /**
     * Resumes a paused game back into {@link GameState#RUNNING}.
     */
    public void resume() {
        if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    /**
     * Transitions the game to {@link GameState#MENU} from pause, victory, or game over,
     * and clears held player movement. Ignored if currently running.
     */
    public void returnToMenu() {
        if (state != GameState.RUNNING) {
            state = GameState.MENU;
            player.setMovement(null, false);
        }
    }

    /**
     * Restarts the current map from the beginning: resets player position, lives, keys,
     * power-ups, enemies, collectables, score, elapsed time, and hit cooldown, sets state
     * to {@link GameState#RUNNING}, and publishes a {@link GameEvent.NewGameStartedEvent}.
     */
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

    /**
     * Advances the game simulation by {@code deltaSeconds}: updates timers, enemies,
     * player cell movement, and collision resolution. Does nothing if the game is not
     * {@link GameState#RUNNING} or if {@code deltaSeconds <= 0}.
     *
     * @param deltaSeconds time elapsed since the last frame in seconds
     */
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

    /**
     * Checks whether the player is allowed to move onto the given grid cell.
     * Walls always block, and exits block until the player has a key.
     *
     * @param position target grid cell to test
     * @return {@code true} if the cell is inside the map and not blocked
     */
    public boolean canPlayerEnter(GridPosition position) {
        GameObject tile = map.getTileAt(position.x(), position.y());
        return tile != null && !tile.blocksMovement(player.hasKey());
    }

    /**
     * Checks whether an enemy is allowed to move onto the given grid cell.
     * Enemies cannot enter walls, exits, out-of-bounds cells, or cells occupied by another enemy.
     *
     * @param position    target grid cell to test
     * @param movingEnemy the enemy attempting to move (excluded from overlap check)
     * @return {@code true} if the enemy may step onto {@code position}
     */
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

    /**
     * Adds points to the score and publishes a {@link GameEvent.ScoreChangedEvent}.
     *
     * @param points number of points to add
     * @param reason short description of why the points were awarded
     */
    public void addScore(int points, String reason) {
        score += points;
        eventBus.publish(new GameEvent.ScoreChangedEvent(score, points, reason));
    }

    /**
     * Applies damage from a hazard to the player and publishes a {@link GameEvent.LifeLostEvent}.
     * Ignored if the game is not running or the player has an active shield.
     * Transitions to {@link GameState#GAME_OVER} and publishes {@link GameEvent.GameOverEvent}
     * if the player's remaining lives drop to zero.
     *
     * @param hazard     the damaging obstacle or enemy that hit the player
     * @param position   the grid position where the hit occurred
     * @param sourceName class or display name of the hazard source
     */
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

    /**
     * Ends an active game as a victory: switches state to {@link GameState#VICTORY},
     * stops movement, awards the completion, time, and remaining-life score bonuses,
     * and publishes a {@link GameEvent.VictoryEvent}.
     */
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
