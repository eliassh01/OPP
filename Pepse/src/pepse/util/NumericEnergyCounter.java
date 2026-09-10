package pepse.util;


import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;


/**
 * Class NumericEnergyCounter which extends from GameObject, represents a Numeric Energy Counter object of the
 * game that appears on the screen for the user
 */
public class NumericEnergyCounter extends GameObject {


    /**
     * Construct a new GameObject instance.
     *
     * @param energy               The current energy number
     * @param topLeftCorner        Position of the object, in window coordinates (pixels).
     *                             Note that (0,0) is the top-left corner of the window.
     * @param dimensions           Width and height in window coordinates.
     * @param gameObjectCollection The collection of the games objects
     */
    public NumericEnergyCounter(int energy,
                                Vector2 topLeftCorner,
                                Vector2 dimensions,
                                GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, new TextRenderable(Integer.toString(energy)));
    }
}
