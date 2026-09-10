package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * Responsible for the creation and management of trees.
 */
public class Tree {

    private static final Color STEM_COLOUR = new Color(100, 50, 20);
    private static final Color LEAF_COLOUR = new Color(50, 200, 30);
    private static final int PROBABILITY = 20;
    private final GameObjectCollection gameObjects;
    private final Function<Float, Float> groundHeightAt;
    private final int seed;
    private final Random rand;
    private static final HashSet<Integer> stems = new HashSet<>();

    /**
     * Constructor.
     * @param gameObjects The collection of all participating game objects.
     * @param groundHeightAt Function which takes a float and returns a float.
     * @param seed The seed.
     */
    public Tree(GameObjectCollection gameObjects,
                Function<Float, Float> groundHeightAt,
                int seed) {
        this.gameObjects = gameObjects;
        this.groundHeightAt = groundHeightAt;
        this.seed = seed;
        this.rand = new Random (seed);

    }

    /**
     * This method creates trees in a given range of x-values.
     * @param minX The lower bound of the given range
     *             (will be rounded to a multiple of Block.SIZE).
     * @param maxX  The upper bound of the given range
     *              (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        int min = (int) (Math.floor((float) minX / Block.SIZE) * Block.SIZE);
        int max = (int) (Math.floor((float) maxX / Block.SIZE) * Block.SIZE);
        for (int posX = min; posX <= max; posX += Block.SIZE) {
            if(stems.contains(posX))
                continue;
            Random random = new Random(Objects.hash((int) (posX), seed));
            int randomNum = random.nextInt(PROBABILITY);
            if (randomNum > 0){
                continue;
            }
            int posY = (int) Math.floor(groundHeightAt.apply((float) posX)/Block.SIZE)
                    * Block.SIZE;
            createStemAndLeaf(posX, posY);
            stems.add(posX);
        }
    }

    /**
     * Private method that takes an (x, y) coordinates and creates
     * a Tree with leaves around it at these coordinates.
     */
    private void createStemAndLeaf(int stemXPosition, int stemYPosition) {
        // number of blocks the stem is made from
        int numOfBlocksOfStem = 5 + rand.nextInt(12);
        for (int i = 0; i < numOfBlocksOfStem; i++) {
            GameObject stem = new Block(new Vector2(stemXPosition,
                    stemYPosition - ((i + 1) * Block.SIZE)),
                    new RectangleRenderable(ColorSupplier.approximateColor(STEM_COLOUR)));
            gameObjects.addGameObject(stem, Layer.STATIC_OBJECTS+10);
            stem.setTag("stem");
        }
        // creating leafs on the stem we put.
        int topLeftOfLeafsX = (stemXPosition - (numOfBlocksOfStem/4 * Block.SIZE));
        int topLeftOfLeafsY = (stemYPosition - ((5*numOfBlocksOfStem/4) * Block.SIZE));
        for (int row = 0; row < (numOfBlocksOfStem/2); row++) {
            for (int col = 0; col < (numOfBlocksOfStem/2) ; col++) {
                int randNum = rand.nextInt(10);
                if (randNum >= 2){
                    Vector2 leafDimensions = new Vector2(topLeftOfLeafsX + (row * Block.SIZE),
                            topLeftOfLeafsY + (col * Block.SIZE));
                    new Leaf(leafDimensions,
                            new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOUR)),
                            gameObjects, Layer.STATIC_OBJECTS + 20, rand);
                }
            }
        }
    }
}
