package src.brick_strategies;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Puck;

import java.util.Random;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Introduces several pucks instead of brick once removed.
 */
public class PuckStrategy extends RemoveBrickStrategyDecorator {

    private static final float PUCK_SPEED = 90;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private Random rand = new Random();

    /**
     * Constructor.
     * @param toBeDecorated CollisionStrategy to be decorated.
     * @param imageReader an ImageReader instance for reading images from files for
     *                     rendering of objects.
     * @param soundReader a SoundReader instance for reading soundClips from files
     *                    for rendering event sounds.
     */
    public PuckStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader, SoundReader soundReader){
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    /**
     * Add pucks to game on collision and delegate to held CollisionStrategy.
     * @param thisObj this brick object.
     * @param otherObj other object that collides with brick.
     * @param counter global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        Renderable puckImage =  imageReader.readImage("assets/mockBall.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        float puckSize = thisObj.getDimensions().x() / 3;
        int i = 0;
        while (i != 3){
            GameObject puck = new Puck(Vector2.ZERO,
                    new Vector2(puckSize, puckSize), puckImage, collisionSound);
            puck.setCenter(thisObj.getCenter());
            setVelocity(puck);
            getGameObjectCollection().addGameObject(puck);
            i++;
        }
    }

    /*
     * set the velocity of the puck.
     * set the direction of the puck randomly when it is produced.
     */
    private void setVelocity(GameObject gameObject) {
        float ballVelX = 1;
        if(rand.nextBoolean()){
            ballVelX *= -1;
        }
        gameObject.setVelocity(new Vector2(ballVelX, PUCK_SPEED));
    }
}
