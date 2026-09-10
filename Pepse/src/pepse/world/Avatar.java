package pepse.world;


import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.NumericEnergyCounter;


import java.awt.event.KeyEvent;
import java.util.Scanner;

/**
 * Responsible for the creation and management of the  avatar.
 */
public class Avatar extends GameObject{
    private static final int AVATAR_HEIGHT = 60;
    private static final int AVATAR_WIDTH = 40;
    private static final int AVATAR_FLYING_HEIGHT= 75;
    private static final int AVATAR_FLYING_WIDTH = 75;
    private static final int GRAVITY = 500;
    private static final double ENERGY_FACTOR = 0.5;
    private static final double MAX_ENERGY = 100;
    private static final double MAX_Y_VEL = 300;
    private static final int ENERGY_DISPLAY_SIZE = 30;

    private static final int JUMP_VEL= -300;
    private static int movementSpeed;
    private static double energy = MAX_ENERGY;
    private static Vector2 movementDir;
    private static UserInputListener inputListener;
    private static NumericEnergyCounter numericEnergyCounter;
    private static Renderable avatarImage;
    private GameObjectCollection gameObjects;
    private static ImageReader avatarImageReader;
    private static boolean walkAnimation = false;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }


    /**
     * This function creates the avatar, initialize it with 100 energy
     * @param gameObjects The collection of all participating game objects.
     * @param layer The layer of the avatar
     * @param topLeftCorner top Left corner of where the avatar should be placed
     * @param inputListener  An inputListener for reading user input in the current frame
     * @param imageReader Used to read images from disk or from within a jar
     * @return
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader){
        Avatar.inputListener = inputListener;
        avatarImageReader = imageReader;
        avatarImage = imageReader.readImage("assets/standing.png", true);


        Avatar avatar = new Avatar(topLeftCorner, new Vector2(AVATAR_WIDTH,AVATAR_HEIGHT), avatarImage);

        avatar.gameObjects = gameObjects;
        gameObjects.addGameObject(avatar,layer);
        movementSpeed = 300;
        movementDir = new Vector2(0,0);
        avatar.setVelocity(new Vector2(0,0));
        avatar.transform().setAccelerationY(GRAVITY);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        numericEnergyCounter = new NumericEnergyCounter((int)energy,
                Vector2.ZERO,
                new Vector2(ENERGY_DISPLAY_SIZE,ENERGY_DISPLAY_SIZE),
                gameObjects);
        numericEnergyCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.layers().shouldLayersCollide(layer, Layer.STATIC_OBJECTS+10, true);
        gameObjects.addGameObject(numericEnergyCounter, Layer.FOREGROUND);
        return avatar;
    }

    /**
     * This function overrides Dano game lab update function, it moves the avatar on the screen depending on
     * what arrow key the user is pressing,and checks if the shift and space keys are pressed, if space is
     * pressed the avatar jumps if sapce + shift are pressed the avatar flies, as well it updates its energy
     * accordingly
     * @param deltaTime time between every update and update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        //updates the energy counter display
        updateEnergyDisplay();

        updateStandRenderable();

        arrowKeysPressed();

        jumpOrFlyPressed();
    }

    private void updateStandRenderable() {
        if(getVelocity().x()==0){
            this.renderer().setRenderable(new AnimationRenderable(new String[]{"assets/standing.png"},
                                                                    avatarImageReader,
                                                    true,
                                                    0.5));
            this.setDimensions(new Vector2(AVATAR_WIDTH,AVATAR_HEIGHT));
            walkAnimation = false;
        }
    }

    /**
     * displays the new energy of the avatar on the screen.
     */
    private void updateEnergyDisplay() {
        gameObjects.removeGameObject(numericEnergyCounter, Layer.FOREGROUND);
        numericEnergyCounter = new NumericEnergyCounter((int)(energy),
                                numericEnergyCounter.getTopLeftCorner(),
                                numericEnergyCounter.getDimensions(),
                                gameObjects);
        numericEnergyCounter.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(numericEnergyCounter, Layer.FOREGROUND);
    }

    /**
     * checks if jump(space) or fly(shift+space) are pressed and change the renderable and avatar location
     * accordingly
     */
    private void jumpOrFlyPressed() {
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT) &&
                energy>0){
            this.renderer().setRenderable(new AnimationRenderable(new String[]{"assets/flying.png"},
                                            avatarImageReader,true,1));
            this.setDimensions(new Vector2(AVATAR_FLYING_WIDTH,AVATAR_FLYING_HEIGHT));
            this.transform().setAccelerationY(0);
            this.setVelocity(new Vector2(this.getVelocity().x(),JUMP_VEL));
            energy -= ENERGY_FACTOR;
        } else {

            this.transform().setAccelerationY(GRAVITY);
            if(this.getVelocity().y()>MAX_Y_VEL){
                this.setVelocity(new Vector2(this.getVelocity().x(),(float) MAX_Y_VEL));
            }
            if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && this.getVelocity().y() == 0) {
                this.setVelocity(new Vector2(this.getVelocity().x(), JUMP_VEL));
                this.transform().setAccelerationY(GRAVITY);
            } else if (this.getVelocity().y() == 0) {
                if (energy < MAX_ENERGY) {
                    energy += ENERGY_FACTOR;
                } else {
                    energy = MAX_ENERGY;
                }
            }
        }
    }

    /**
     * checks if ->(right arrow) or <-(left arrow) are pressed and change the renderable and avatar location
     * accordingly
     */
    private void arrowKeysPressed() {
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if(!walkAnimation){
                this.renderer().setRenderable(new AnimationRenderable(
                        new String[]{"assets/walking1.png","assets/walking2.png"},
                        avatarImageReader,
                        true,
                        0.5));
                walkAnimation = true;
            }
            this.renderer().setIsFlippedHorizontally(false);
            movementDir = movementDir.add(Vector2.LEFT).normalized();
            setVelocity(movementDir.mult(movementSpeed).add(new Vector2(0,this.getVelocity().y())));
        } else if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if(!walkAnimation){
                this.renderer().setRenderable(new AnimationRenderable(
                        new String[]{"assets/walking1.png","assets/walking2.png"},
                        avatarImageReader,
                        true,
                        0.5));
                walkAnimation = true;
            }
            this.renderer().setIsFlippedHorizontally(true);
            movementDir = movementDir.add(Vector2.RIGHT).normalized();
            setVelocity(movementDir.mult(movementSpeed).add(new Vector2(0,this.getVelocity().y())));

        } else{

            setVelocity(new Vector2(0, this.getVelocity().y()));

        }
    }
}
