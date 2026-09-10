package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;
import src.gameobjects.BallCollisionCountdownAgent;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Changes camera focus from ground to ball until ball collides NUM_BALL_COLLISIONS_TO_TURN_OFF times.
 */
public class ChangeCameraStrategy extends RemoveBrickStrategyDecorator {

    private WindowController windowController;
    private BrickerGameManager gameManager;
    private Ball mainBall;
    private BallCollisionCountdownAgent agent;

    /**
     * Constructor.
     * @param toBeDecorated CollisionStrategy to be decorated.
     * @param windowController controls visual rendering of the game window
     *                        and object renderables.
     * @param gameManager main class in the game which initialises all objects.
     */
    public ChangeCameraStrategy(CollisionStrategy toBeDecorated, WindowController windowController,
                                BrickerGameManager gameManager) {
        super(toBeDecorated);
        this.windowController = windowController;
        this.gameManager = gameManager;
    }

    /**
     * Change camera position on collision and delegate to held CollisionStrategy.
     * @param thisObj this brick object.
     * @param otherObj other object that collides with brick.
     * @param counter global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        if(gameManager.getCamera() != null){
            return;
        }
        getMainBall();
        gameManager.setCamera(new Camera(mainBall, Vector2.ZERO,
                windowController.getWindowDimensions().mult(1.2f),
                windowController.getWindowDimensions()));
        if(otherObj.getTag().equals("main ball")){
            agent = new BallCollisionCountdownAgent(mainBall,
                    this, mainBall.getCollisionCount());
        }
        else{
            agent = new BallCollisionCountdownAgent(mainBall,
                    this, mainBall.getCollisionCount() - 1);
        }
        getGameObjectCollection().addGameObject(agent);
    }

    /**
     * Return camera to normal ground position.
     */
    public void turnOffCameraChange(){
        gameManager.setCamera(null);
        getGameObjectCollection().removeGameObject(agent);
    }

    /*
     * Method that gets the main ball in the game.
     */
    private void getMainBall() {
        for (GameObject object : getGameObjectCollection()){
            if (object.getTag().equals("main ball")){
                mainBall = (Ball) object;
                break;
            }
        }
    }
}

