package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class which represents Paddle which is sometimes produced when ball hits a brick.
 */
public class MockPaddle extends Paddle {

    private GameObjectCollection gameObjectCollection;
    private int numCollisionsToDisappear;
    private int numberOfCollisions;

    /**
     * An indicator that is used to know whether
     * there is mockPaddle in game or not.
     */
    public static boolean isInstantiated = false;

    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions width and height in window coordinates.
     * @param renderable the renderable representing the object. Can be null, in which case.
     * @param inputListener an InputListener instance for reading user input.
     * @param windowDimensions dimensions of screen.
     * @param gameObjectCollection a container for adding/removing instances of GameObjects.
     * @param minDistanceFromEdge border for paddle movement.
     * @param numCollisionsToDisappear number of collisions for the paddle to disappear.
     */
    public MockPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                      Renderable renderable, UserInputListener inputListener,
                      Vector2 windowDimensions, GameObjectCollection gameObjectCollection,
                      int minDistanceFromEdge, int numCollisionsToDisappear) {
        super(topLeftCorner, dimensions, renderable, inputListener,
                windowDimensions, minDistanceFromEdge);
        this.gameObjectCollection = gameObjectCollection;
        this.numCollisionsToDisappear = numCollisionsToDisappear;
        numberOfCollisions = 0;
    }

    /**
     * Called when an object collides with mockPaddle, if the number of
     * times the mockPaddle collided with an object > numCollisionsToDisappear,
     * it removes the mockPaddle from game.
     * @param other other GameObject instance participating in collision.
     * @param collision Collision object.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision){
        super.onCollisionEnter(other, collision);
        numberOfCollisions++;
        if(numberOfCollisions >= numCollisionsToDisappear){
            gameObjectCollection.removeGameObject(this);
            isInstantiated = false;
        }
    }
}
