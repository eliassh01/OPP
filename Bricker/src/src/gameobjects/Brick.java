package src.gameobjects;

import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import danogl.util.Counter;

/**
 * This class is responsible for creating bricks
 * and checking if a ball hits it.
 */
public class Brick extends GameObject {
    private CollisionStrategy collisionStrategy;
    private Counter counter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    width and height in window coordinates.
     * @param renderable    the renderable representing the object. Can be null, in which case
     * @param collisionStrategy an instance of CollisionStrategy.
     * @param counter a global counter.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy, Counter counter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.counter = counter;
    }

    /**
     * Calls collisionStrategy.onCollision when collision happens.
     * @param other other GameObject instance participating in collision.
     * @param collision Collision object.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other, counter);
    }
}
