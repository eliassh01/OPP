package src.gameobjects;

import danogl.GameObject;
import danogl.util.Vector2;
import src.brick_strategies.ChangeCameraStrategy;

/**
 * An object of this class is instantiated on collision of ball
 * with a brick with a change camera strategy.
 * It checks ball's collision counter every frame,
 * and once it finds the ball has collided countDownValue times since instantiation,
 * it calls the strategy to reset the camera to normal.
 */
public class BallCollisionCountdownAgent extends GameObject {

    private static final int NUM_BALL_COLLISIONS_TO_TURN_OFF = 4;
    private Ball ball;
    private ChangeCameraStrategy owner;
    private int countDownValue;

    /**
     * Constructor.
     * @param ball Ball object whose collisions are to be counted.
     * @param owner Object asking for countdown notification.
     * @param countDownValue Number of ball collisions. Notify caller object that
     *                      the ball collided countDownValue times since instantiation.
     */
    public BallCollisionCountdownAgent(Ball ball, ChangeCameraStrategy owner, int countDownValue){
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.ball = ball;
        this.owner = owner;
        this.countDownValue = countDownValue;
    }

    /**
     * it checks whether the ball gameObject collided
     * with 4 objects after changing camera.
     * If yes, it returns the camera to normal position.
     * @param deltaTime time between updates.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(ball.getCollisionCount() >= countDownValue + NUM_BALL_COLLISIONS_TO_TURN_OFF + 1) {
            owner.turnOffCameraChange();
        }
    }
}
