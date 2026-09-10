package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import danogl.gui.ImageReader;
import src.gameobjects.BuffNarrowWiden;

import java.util.Random;

/**
 * Concrete class extending abstract RemoveBrickStrategyDecorator.
 * Introduces Status (buffNarrow or buffWiden) which when it hits the
 * paddle it widens or narrow it.
 */
public class NarrowWidenPaddleStrategy extends RemoveBrickStrategyDecorator {

    private static final int BUFF_SPEED = 85;
    private ImageReader imageReader;
    private GameObjectCollection gameObjectCollection;
    private Random random = new Random();
    private boolean isWiden = false;

    /**
     * Constructor.
     * @param toBeDecorated Collision strategy object to be decorated.
     * @param imageReader   an ImageReader instance for reading images from files for
     *                      rendering of objects.
     */
    public NarrowWidenPaddleStrategy(CollisionStrategy toBeDecorated, ImageReader imageReader,
                                     GameObjectCollection gameObjectCollection) {
        super(toBeDecorated);
        this.imageReader = imageReader;
        this.gameObjectCollection = gameObjectCollection;
    }

    /**
     * Adds
     * @param thisObj this brick object.
     * @param otherObj other object that collides with brick.
     * @param counter global brick counter.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter counter) {
        super.onCollision(thisObj, otherObj, counter);
        Renderable buffWiden = imageReader.readImage("assets/buffWiden.png", true);
        Renderable buffNarrow = imageReader.readImage("assets/buffNarrow.png", true);
        GameObject buff;
        int randomNum = random.nextInt(2);
        if(randomNum == 0){
            isWiden = true;
            buff = new BuffNarrowWiden(Vector2.ZERO, thisObj.getDimensions(),
                    buffWiden, gameObjectCollection, isWiden);
        }
        else {
            buff = new BuffNarrowWiden(Vector2.ZERO, thisObj.getDimensions(),
                    buffNarrow, gameObjectCollection, isWiden);
            isWiden = false;
        }
        buff.setCenter(thisObj.getCenter());
        buff.setVelocity(new Vector2(1, BUFF_SPEED));
        gameObjectCollection.addGameObject(buff);
    }
}
