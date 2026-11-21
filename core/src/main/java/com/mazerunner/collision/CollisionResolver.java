package com.mazerunner.collision;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mazerunner.core.GameState;
import com.mazerunner.core.GameWorld;
import com.mazerunner.entities.Collectable;
import com.mazerunner.entities.Damaging;
import com.mazerunner.entities.Enemy;
import com.mazerunner.entities.Exit;
import com.mazerunner.entities.GameObject;
import com.mazerunner.entities.Player;
import com.mazerunner.map.GridPosition;

/**
 * Handles player collisions with collectable items, hazards, and the exit.
 * Also tracks the hit cooldown and active hazard contacts so standing on a trap
 * only removes one life on entry instead of draining lives every frame.
 *
 * @author Ali Hasanov
 */
public final class CollisionResolver {

    public static final float DEFAULT_HIT_COOLDOWN_SECONDS = 1.0f;

    private final float hitCooldownSeconds;
    private float cooldownRemaining;
    private final Set<Long> activeDamageContacts = new HashSet<>();

    /** Creates a collision resolver with the default 1-second hit cooldown. */
    public CollisionResolver() {
        this(DEFAULT_HIT_COOLDOWN_SECONDS);
    }

    /** Creates a collision resolver with a custom hit cooldown in seconds. */
    public CollisionResolver(float hitCooldownSeconds) {
        if (hitCooldownSeconds < 0.0f) {
            throw new IllegalArgumentException("Hit cooldown cannot be negative");
        }
        this.hitCooldownSeconds = hitCooldownSeconds;
    }

    /** Counts down the remaining hit cooldown timer each frame. */
    public void update(float deltaSeconds) {
        if (deltaSeconds < 0.0f) {
            throw new IllegalArgumentException("Delta time cannot be negative");
        }
        cooldownRemaining -= deltaSeconds;
        if (cooldownRemaining < 0.0f) {
            cooldownRemaining = 0.0f;
        }
    }

    /**
     * Checks all collisions at the player's current cell (items, traps, enemies, and exit).
     * A hazard only damages the player when first entered.
     */
    public void resolve(GameWorld world) {
        if (world == null) {
            throw new IllegalArgumentException("World cannot be null");
        }
        if (world.getState() != GameState.RUNNING) {
            return;
        }

        Player player = world.getPlayer();
        GridPosition playerPosition = player.getPosition();

        collectItemsAtPlayer(world, player);

        Set<Long> currentContacts = new HashSet<>();
        List<Damaging> newContacts = new ArrayList<>();

        GameObject tile = world.getMap().getTileAt(playerPosition.x(), playerPosition.y());
        checkDamageContact(tile, player, currentContacts, newContacts);

        for (Enemy enemy : world.getMap().getEnemies()) {
            checkDamageContact(enemy, player, currentContacts, newContacts);
        }

        if (!player.hasShield() && cooldownRemaining <= 0.0f && !newContacts.isEmpty()) {
            Damaging hazard = newContacts.get(0);
            GameObject source = (GameObject) hazard;
            world.damagePlayer(hazard, source.getPosition(), source.getClass().getSimpleName());
            cooldownRemaining = hitCooldownSeconds;
        }

        activeDamageContacts.clear();
        activeDamageContacts.addAll(currentContacts);

        if (world.getState() == GameState.RUNNING && tile instanceof Exit && player.hasKey()) {
            world.finishVictory();
        }
    }

    /** Resets the hit cooldown and clears active hazard contacts for a new game. */
    public void reset() {
        cooldownRemaining = 0.0f;
        activeDamageContacts.clear();
    }

    /** Returns how much cooldown time is left in seconds. */
    public float getCooldownRemaining() {
        return cooldownRemaining;
    }

    private void checkDamageContact(GameObject object, Player player,
                                    Set<Long> currentContacts, List<Damaging> newContacts) {
        if (object instanceof Damaging && player.collidesWith(object)) {
            currentContacts.add(object.getId());
            if (!activeDamageContacts.contains(object.getId())) {
                newContacts.add((Damaging) object);
            }
        }
    }

    private void collectItemsAtPlayer(GameWorld world, Player player) {
        List<GameObject> collected = new ArrayList<>();
        for (GameObject item : world.getCollectables()) {
            if (item instanceof Collectable && player.collidesWith(item)) {
                ((Collectable) item).collect(world, player);
                collected.add(item);
            }
        }
        for (GameObject item : collected) {
            world.removeCollectable(item);
        }
    }
}
