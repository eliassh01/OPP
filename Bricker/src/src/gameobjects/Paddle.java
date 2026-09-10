package src.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * One of the main game objects. Repels the ball against the bricks.
 */
public class Paddle extends GameObject {
    private static final float PADDLE_SPEED = 115;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;
    private int minDistanceFromEdge;

    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner position of the object, in window coordinates (pixels).
     *                     Note that (0,0) is the top-left corner of the window.
     * @param dimensions width and height in window coordinates.
     * @param inputListener an InputListener instance for reading user input.
     * @param renderable the renderable representing the object. Can be null, in which case.
     * @param windowDimensions dimensions of screen.
     * @param minDistanceFromEdge border for paddle movement.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistanceFromEdge) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistanceFromEdge = minDistanceFromEdge;
    }

    /**
     * Code in this function is run this frame.
     * Makes sure that the paddle doesn't exceed the minimal distance.
     * @param deltaTime time between updates.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(PADDLE_SPEED));
        if(getTopLeftCorner().x() < minDistanceFromEdge) {
            transform().setTopLeftCornerX(minDistanceFromEdge);
        }
        float disFromEdgeToRightScreen  = windowDimensions.x() -
                minDistanceFromEdge - getDimensions().x();
        if(getTopLeftCorner().x() > disFromEdgeToRightScreen){
            transform().setTopLeftCornerX(disFromEdgeToRightScreen);
        }
    }
}
