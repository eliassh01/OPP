package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;

/**
 * Class that represent a leaf of the Trees in the Game.
 */
public class Leaf extends Block {

    private static final int FADEOUT_TIME = 10;
    public static final int MAX_LEAF_LIFE = 300;
    public static final int LEAF_SIZE = 28;
    private Transition<Float> horizontalTransition;
    private Transition<Float> angleTransition;
    private Transition<Vector2> vector2Transition;
    private final Renderable renderable;
    private final Random random;

    /**
     * Constructor.
     * @param topLeftCorner The location of the top-left corner of the created leaf.
     * @param renderable A renderable to render as leaf.
     * @param gameObjects The collection of all participating game objects
     * @param leafLayer The number of the layer to which the created
     *                 leaf should be added.
     * @param random Random Object.
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable,
                GameObjectCollection gameObjects, int leafLayer,
                Random random) {
        super(topLeftCorner, renderable);
        this.renderable = renderable;
        this.random = random;
        physics().setMass(0);
        this.setTopLeftCorner(topLeftCorner);
        this.setTag("leaf");
        gameObjects.addGameObject(this, leafLayer);
        // following line to let the leaf collide with the Terrain.
        gameObjects.layers().shouldLayersCollide(Layer.STATIC_OBJECTS, leafLayer, true);
        vibrationsOfLeaf();
        fallingLeaves(gameObjects, leafLayer, topLeftCorner);
    }

    /**
     * stops The leaf when it collides with terrain.
     * @param other block of the terrain.
     * @param collision object of collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        this.transform().setVelocity(Vector2.ZERO);
        this.removeComponent(horizontalTransition);
        this.removeComponent(angleTransition);
        this.removeComponent(vector2Transition);
    }

    /**
     * Private method that operates two Transitions on leaf which
     * causes it to vibrate, and each leaf is given a random delayTime
     * (time until the Transition starts) with the help of ScheduledTask.
     */
    private void vibrationsOfLeaf() {
        Runnable runnable = () -> {
            angleTransition = new Transition<Float>(this, this.renderer()::setRenderableAngle,
                    80f, 100f,
                    Transition.LINEAR_INTERPOLATOR_FLOAT, 1,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
            vector2Transition = new Transition<Vector2>(this, this::setDimensions,
                    new Vector2(Block.SIZE, Block.SIZE), new Vector2(LEAF_SIZE, LEAF_SIZE),
                    Transition.LINEAR_INTERPOLATOR_VECTOR, 2,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        };
        float[] timeDelays = {0f, 0.2f, 0.4f, 0.6f, 0.8f, 1, 1.2f, 1.4f};
        int randomNum = random.nextInt(timeDelays.length);
        float timeDelay = timeDelays[randomNum];
        new ScheduledTask(this,  timeDelay, false, runnable);
    }

    /**
     * Private method that gives the leaf a random lifeTime, after that
     * time the leaf starts falling and fading out. then a random deathTime
     * is taken and after that time a leaf a new leaf is created in the old position.
     */
    private void fallingLeaves(GameObjectCollection gameObjects,
                               int leafLayer, Vector2 leafDimensions) {
        int leafLife = random.nextInt(MAX_LEAF_LIFE);
        float fallingVelocity = 50;
        Runnable runnable = () -> {
            this.transform().setVelocityY(fallingVelocity);
            int deathTime = random.nextInt(10);
            // The transition that makes the leaf move horizontally when it is falling.
            horizontalTransition = new Transition<Float>(this, this.transform()::setVelocityX,
                    -30f, 30f,
                    Transition.LINEAR_INTERPOLATOR_FLOAT, 2,
                    Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
            // The leafs fade out and then after FADEOUT_TIME it creates
            // ScheduledTask that returns the leaf to its original position after deathTime.
            this.renderer().fadeOut(FADEOUT_TIME, () -> new ScheduledTask(this, deathTime,
                    false, () -> {
                gameObjects.removeGameObject(this);
                new Leaf(leafDimensions, renderable, gameObjects, leafLayer, random);
            }));
        };
        new ScheduledTask(this, leafLife, false, runnable);
    }
}
