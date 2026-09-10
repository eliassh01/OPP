package pepse.world;


import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

/**
 * Responsible for the creation and management of terrain.
 */
public class Terrain {

    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final int seed;
    private final float groundHeightAtX0;
    private static final int SEQUENTIAL_BLOCKS = 10;
    private static final int TERRAIN_DEPTH = 20;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static int firstBlockLocation = -1;
    private static int lastBlockLocation = -1;
    private static final HashMap<Integer, HashSet<GameObject>> blocksList = new HashMap<>();

    /**
     * Constructors.
     * @param gameObjects The collection of all participating game objects.
     * @param groundLayer The number of the layer to which the created
     *                   ground objects should be added.
     * @param windowDimensions The dimensions of the windows.
     * @param seed A seed for a random number generator.
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer,
                   Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.seed = seed;
        groundHeightAtX0 = ( (float) 5/6) * windowDimensions.y();
    }

    /**
     * This method return the ground height at a given location.
     * @param x location.
     * @return The ground height at the given location.
     */
    public float groundHeightAt(float x) {
        float loSlope=0;
        float  hiSlope=0;
        float loPos=0;
        float hiPos=0;
        float scale = 10f*Block.SIZE;
        float[] slopeAt = new float[2]; // interpolate between every SEQUENTIAL_BLOCKS blocks
                                                        // for each SEQUENTIAL_BLOCKS blocks, use same seed
        int int_x = (int) Math.floor(x);
        int start_of_x_block = int_x - (int_x % Block.SIZE);
        if(int_x < 0){
            start_of_x_block -= Block.SIZE;
        }
        int start_of_sequential_blocks = start_of_x_block -
                                        (start_of_x_block % (SEQUENTIAL_BLOCKS* Block.SIZE));
        if (start_of_x_block < 0){
            start_of_sequential_blocks -= SEQUENTIAL_BLOCKS*Block.SIZE;
        }
        float dist = (float) (start_of_x_block -
                      start_of_sequential_blocks) / (SEQUENTIAL_BLOCKS* Block.SIZE);
        Random rand = new Random(Objects.hash((int) (start_of_sequential_blocks), seed));

        loSlope = rand.nextFloat() * 2 - 1;
        hiSlope = rand.nextFloat() * 2 - 1;
        loPos = loSlope * dist;
        hiPos = hiSlope * (1-dist);
        float u = dist * dist * ((float)3.0 - (float)2.0 * dist);  // cubic curve
        float dy = scale*(float)((loPos*(1-u)) + (hiPos*u));
        return groundHeightAtX0 + dy;  // interpolate

    }

    /**
     * This method creates terrain in a given range of x-values.
     * @param minX The lower bound of the given range
     *             (will be rounded to a multiple of Block.SIZE).
     * @param maxX  The upper bound of the given range
     *              (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        int min = minX;
        int max = maxX;
        if (minX % Block.SIZE != 0) {
            min = minX - minX % Block.SIZE - Block.SIZE;
        }
        if (maxX % Block.SIZE != 0) {
            max = maxX - maxX % Block.SIZE - Block.SIZE;
        }
        if(firstBlockLocation == -1){
            firstBlockLocation = min;
        }
        if(lastBlockLocation == -1){
            lastBlockLocation = max;
        }
        // add blocks in range if not exist
        addTerrainBlocks(min, max);
        // remove blocks out of range
        removeTerrainBLocks(min, max);
    }

    /**
     * Removes terrain blocks wich are out of the frame
     * @param min the new min for createInRange
     * @param max the new max for createInRange
     */
    private void removeTerrainBLocks(int min, int max) {
        int out_of_range_left = (min -((int)windowDimensions.x()-((int)windowDimensions.x()
                % Block.SIZE))-Block.SIZE);
        if(blocksList.containsKey(out_of_range_left)) {
            HashSet<GameObject> before_blocks = blocksList.get(out_of_range_left);
            for (GameObject block_loc : before_blocks) {
                gameObjects.removeGameObject(block_loc, Layer.STATIC_OBJECTS);
            }
            blocksList.remove(out_of_range_left);
        }

        int out_of_range_right = (max +((int)windowDimensions.x()-(int)windowDimensions.x()
                % Block.SIZE) + Block.SIZE);
        if(blocksList.containsKey(out_of_range_right)) {
            HashSet<GameObject> after_blocks = blocksList.get(out_of_range_right);
            for (GameObject block_loc : after_blocks) {
                gameObjects.removeGameObject(block_loc, Layer.STATIC_OBJECTS);
            }
            blocksList.remove(out_of_range_right);
        }
    }

    /**
     * Adds terrain blocks which are in new x's of the frame
     * @param min the new min for createInRange
     * @param max the new max for createInRange
     */
    private void addTerrainBlocks(int min, int max) {
        int counter = 0;

        for (int row = min; row < max; row += Block.SIZE) {
            if(blocksList.containsKey(row)) { //skip column because already rendered
                continue;
            }
            int upperBlockCoordinate = (int) (Math.floor(groundHeightAt(row ) / Block.SIZE) * Block.SIZE);
            int col = upperBlockCoordinate;
            HashSet<GameObject> block_in_row = new HashSet<>();
            for (int depth = TERRAIN_DEPTH ; depth > 0 ; depth -= 1) {
                GameObject ground = new Block(new Vector2(row,col),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                if(counter<2){
                    gameObjects.addGameObject(ground, groundLayer);
                }else{
                    gameObjects.addGameObject(ground, groundLayer+5);
                }
                block_in_row.add(ground);
                ground.setTag("ground");
                col += Block.SIZE;
                counter++;
            }
            counter=0;
            blocksList.put(row, block_in_row);
        }
    }

}
