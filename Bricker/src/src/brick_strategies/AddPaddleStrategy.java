package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.MockPaddle;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Introduces extra paddle to game window which remains until colliding
 * NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE with other game objects.
 */
public class AddPaddleStrategy extends RemoveBrickStrategyDecorator {

    private static final int NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE = 3;
    private static final int MOCK_PADDLE_WIDTH = 100;
    private static final int MOCK_PADDLE_HEIGHT = 15;
    private static final int MIN_DISTANCE_FROM_EDGE = 25;
    private ImageReader imageReader;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;

    /**
     * Constructor.
     * @param toBeDecorated collisionStrategy to be decorated.
     * @param imageReader an ImageReader instance for reading images from files for
     *                      rendering of objects.
     * @param inputListener an InputListener instance for reading user input.
     * @param windowDimensions pixel dimensions for game window height x width.
     */
    public AddPaddleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader,
                             UserInputListener inputListener, Vector2 windowDimensions) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Adds additional paddle to game and delegates to held object.
     * @param thisObj this brick object.
     * @param otherObj other object that collides with brick.
     * @param counter global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if(!MockPaddle.isInstantiated) {
            Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
            GameObject mockPaddle = new MockPaddle(Vector2.ZERO,
                    new Vector2(MOCK_PADDLE_WIDTH, MOCK_PADDLE_HEIGHT), paddleImage,
                    inputListener, windowDimensions, getGameObjectCollection(),
                    MIN_DISTANCE_FROM_EDGE, NUM_COLLISIONS_FOR_MOCK_PADDLE_DISAPPEARANCE);
            mockPaddle.setCenter(new Vector2( windowDimensions.x()/2, windowDimensions.y()/2));
            getGameObjectCollection().addGameObject(mockPaddle);
            MockPaddle.isInstantiated = true;
        }
    }
}
