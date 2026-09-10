package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Display a graphic object on the game window
 * showing a numeric count of lives left.
 */
public class NumericLifeCounter extends GameObject {

    /**
     * Constructor.
     * @param livesCounter global lives counter of game.
     * @param topLeftCorner top left corner of renderable.
     * @param dimensions dimensions of renderable.
     * @param gameObjectCollection global game object collection.
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, new TextRenderable(Integer.toString(livesCounter.value())));
    }

    /**
     * Code in this function is run this frame.
     * For internal use by game engine.
     * @param deltaTime time between updates.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        }
}
