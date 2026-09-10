package src.gameobjects;

import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * One of the types of objects that can be set loose when a brick is hit.
 */
public class Puck extends Ball {

    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner  position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions width and height in window coordinates.
     * @param renderable the renderable representing the object. Can be null, in which case.
     * @param collisionSound the sound we hear when collision happens.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions,
                Renderable renderable, Sound collisionSound){
        super(topLeftCorner, dimensions, renderable, collisionSound);
    }
}
