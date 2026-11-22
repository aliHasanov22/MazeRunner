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

/**
 * Checks what the player is touching: items, traps, enemies and the exit.
 * A hazard only hurts when the player first steps on it, and after a hit
 * there is a 1 second cooldown, so one collision removes only one life.
 *
 * @author Ali Hasanov
 */
public final class CollisionResolver {

    public static final float HIT_COOLDOWN_SECONDS = 1.0f;

    private float cooldownRemaining;
    private final Set<Long> touchingHazards = new HashSet<>();

    /** Counts down the hit cooldown. */
    public void update(float deltaSeconds) {
        cooldownRemaining = Math.max(0, cooldownRemaining - deltaSeconds);
    }

    /** Handles all collisions on the player's current cell. */
    public void resolve(GameWorld world) {
        if (world.getState() != GameState.RUNNING) {
            return;
        }
        Player player = world.getPlayer();

        // 1. pick up items
        List<GameObject> pickedUp = new ArrayList<>();
        for (GameObject item : world.getCollectables()) {
            if (player.collidesWith(item)) {
                ((Collectable) item).collect(world, player);
                pickedUp.add(item);
            }
        }
        for (GameObject item : pickedUp) {
            world.removeCollectable(item);
        }

        // 2. find hazards the player is standing on
        List<GameObject> hazards = new ArrayList<>();
        GameObject tile = world.getMap().getTileAt(player.getX(), player.getY());
        hazards.add(tile);
        hazards.addAll(world.getMap().getEnemies());

        Set<Long> nowTouching = new HashSet<>();
        GameObject newHit = null;
        for (GameObject hazard : hazards) {
            if (hazard instanceof Damaging && player.collidesWith(hazard)) {
                nowTouching.add(hazard.getId());
                if (newHit == null && !touchingHazards.contains(hazard.getId())) {
                    newHit = hazard;
                }
            }
        }
        touchingHazards.clear();
        touchingHazards.addAll(nowTouching);

        // 3. damage only on a new contact and when the cooldown is over
        if (newHit != null && cooldownRemaining <= 0) {
            world.damagePlayer((Damaging) newHit, newHit.getPosition(), newHit.getClass().getSimpleName());
            cooldownRemaining = HIT_COOLDOWN_SECONDS;
        }

        // 4. win when standing on an exit with a key
        if (tile instanceof Exit && player.hasKey()) {
            world.finishVictory();
        }
    }

    /** Clears the cooldown and contacts for a new game. */
    public void reset() {
        cooldownRemaining = 0;
        touchingHazards.clear();
    }

    /** Returns the remaining cooldown in seconds. */
    public float getCooldownRemaining() {
        return cooldownRemaining;
    }
}
