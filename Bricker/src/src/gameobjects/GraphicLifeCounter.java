package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Display a graphic object on the game window
 * showing as many widgets as lives left.
 */
public class GraphicLifeCounter extends GameObject {

    private Counter livesCounter;
    private GameObjectCollection gameObjectCollection;
    private int numOfLives;

    /**
     * Constructor.
     * @param widgetTopLeftCorner top left corner of left most life widgets.
     *                           Other widgets will be displayed to its right, aligned in height.
     * @param widgetDimensions  dimensions of widgets to be displayed.
     * @param livesCounter global lives counter of game.
     * @param widgetRenderable  image to use for widgets.
     * @param gameObjectCollection  global game object collection managed by game manager.
     * @param numOfLives global setting of number of lives.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner,
                              Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable,
                              GameObjectCollection gameObjectCollection,
                              int numOfLives) {
        super(widgetTopLeftCorner, widgetDimensions, widgetRenderable);
        this.livesCounter = livesCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.numOfLives = numOfLives;
    }

    /**
     * It checks whether numberOfLives decreased by 1,
     * if yes it removes a Graphic counter.
     * @param deltaTime time between updates.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(livesCounter.value() < numOfLives){
            gameObjectCollection.removeGameObject(this, Layer.BACKGROUND);
        }
    }
}