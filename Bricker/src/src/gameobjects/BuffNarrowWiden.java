package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Class which represent a Status which is sometimes produced
 * when a brick is broken.
 */
public class BuffNarrowWiden extends GameObject {

    private GameObjectCollection gameObjectCollection;
    private boolean isWiden;

    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions width and height in window coordinates.
     * @param renderable the renderable representing the object. Can be null, in which case
     * @param gameObjectCollection a container for adding/removing instances of GameObjects.
     * @param isWiden is the objects widens or narrows.
     */
    public BuffNarrowWiden(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                           GameObjectCollection gameObjectCollection, boolean isWiden) {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjectCollection = gameObjectCollection;
        this.isWiden = isWiden;
    }

    /**
     * Method that makes sure that ExpandedContractedPaddle only collides with Paddle.
     * @param other the object that this paddle collides with.
     * @return true if it can collide, false else.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals("main Paddle");
    }

    /**
     * Method that widens or narrows the Paddle when it collides with buffWiden
     * or buffNarrow.
     * @param other other GameObject instance participating in collision.
     * @param collision Collision object.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        float otherWidth = other.getDimensions().x();
        float otherHeight = other.getDimensions().y();
        if(isWiden){
            other.setDimensions(new Vector2(otherWidth + 5, otherHeight));
        }
        else {
            other.setDimensions(new Vector2(otherWidth - 5, otherHeight));
        }
        gameObjectCollection.removeGameObject(this);
    }
}
