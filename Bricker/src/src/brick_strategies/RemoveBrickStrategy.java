package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * Concrete brick strategy implementing CollisionStrategy interface.
 * Removes holding brick on collision.
 */
public class RemoveBrickStrategy implements CollisionStrategy {

    private GameObjectCollection gameObjectCollection;

    /**
     * Constructor.
     * @param gameObjectCollection a container for adding/removing instances of GameObjects.
     */
    public RemoveBrickStrategy(GameObjectCollection gameObjectCollection){
        this.gameObjectCollection = gameObjectCollection;
    }

    /**
     * All collision strategy objects should hold a reference to the global game
     * object collection and be able to return it.
     * @return global game object collection whose reference is held in object.
     */
    @Override
    public GameObjectCollection getGameObjectCollection(){
        return gameObjectCollection;
    }

    /**
     * Removes brick from game object collection on collision.
     * @param thisObj this brick object.
     * @param otherObj other object that collides with brick.
     * @param counter global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        if(gameObjectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS)){
            counter.decrement();
        }
    }
}
