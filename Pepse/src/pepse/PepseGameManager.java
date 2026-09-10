package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;
import java.awt.*;

/**
 * The main class of the simulator.
 */
public class PepseGameManager extends GameManager {

    private static final int SEED = 3827357;
    private static final int CYCLE_LENGTH = 30;
    private static Terrain terrain;
    private static Tree tree;
    private static float windowDimX;

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     * @param imageReader Contains a single method: readImage, which reads an
     *                    image from disk. See its documentation for help.
     * @param soundReader Contains a single method: readSound,
     *                   which reads a wav file from disk.
     * @param inputListener Contains a single method: isKeyPressed,
     *                     which returns whether a given key is currently pressed
     *                     by the user or not. See its documentation.
     * @param windowController  Contains an array of helpful, self-explanatory
     *                         methods concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        // sky
        Sky.create(gameObjects(),
                windowController.getWindowDimensions(),
                Layer.BACKGROUND);

        // terrain
        windowDimX = windowController.getWindowDimensions().x();

        int x_dim = (int) Math.floor(windowDimX);
        int left_range = (-x_dim/2) - ((- x_dim/2) % Block.SIZE);
        int right_range = (x_dim/2) - ((x_dim/2)%Block.SIZE)+2*Block.SIZE;

        terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS,
                windowController.getWindowDimensions(), SEED);
         terrain.createInRange(left_range , right_range);

        // night
        GameObject night = Night.create(gameObjects(), Layer.FOREGROUND,
                windowController.getWindowDimensions(), CYCLE_LENGTH);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // Sun
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND + 1,
                windowController.getWindowDimensions(), CYCLE_LENGTH);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        // SunHalo
        SunHalo.create(gameObjects(), Layer.BACKGROUND + 10,
                sun, new Color(255, 255, 0, 20));

        //Create trees
        tree = new Tree(gameObjects(),terrain::groundHeightAt, SEED);
        tree.createInRange(left_range , right_range);

        // Creates Avatar
        Avatar avatar = Avatar.create(gameObjects(),Layer.DEFAULT,
                        new Vector2(windowController.getWindowDimensions().x()/2,
                                (terrain.groundHeightAt(windowController.getWindowDimensions().x()/2))
                                        - 2*Block.SIZE),
                        inputListener,
                        imageReader);

        // Creates new Camera
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    /**
     * Updates the game every delta time,overrides the original method and calls createInRange in terrian and
     * tree instances to update the game for an open world
     * @param deltaTime time between every update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        int topLeftX = (int) getCamera().getObjectFollowed().getTopLeftCorner().x();
        int x_dim = (int) Math.floor(windowDimX);
        int left_range = (topLeftX -x_dim/2) - ((topLeftX - x_dim/2) % Block.SIZE);
        int right_range = (topLeftX + x_dim/2) -
                ((topLeftX + x_dim/2)%Block.SIZE)+2*Block.SIZE;
        terrain.createInRange(left_range , right_range) ;
        tree.createInRange(left_range,right_range);
    }
}
