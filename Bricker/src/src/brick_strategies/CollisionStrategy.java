package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

/**
 * General type for brick strategies, part of decorator pattern implementation.
 * All brick strategies implement this interface.
 */
public interface CollisionStrategy {

    /**
     * Delegate to held Collision strategy object.
     * @param thisObj this brick object.
     * @param otherObj other object that collides with brick.
     * @param counter global brick counter.
     */
    void onCollision(GameObject thisObj, GameObject otherObj, Counter counter);

    /**
     * All collision strategy objects should hold a reference to the global game
     * object collection and be able to return it.
     * @return global game object collection whose reference is held in object.
     */
    GameObjectCollection getGameObjectCollection();
}
