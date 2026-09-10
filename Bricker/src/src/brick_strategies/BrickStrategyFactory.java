package src.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import src.BrickerGameManager;
import java.util.Random;

/**
 * Factory class for creating Collision strategies.
 */
public class BrickStrategyFactory {
    private Random random = new Random();
    private GameObjectCollection gameObjectCollection;
    private BrickerGameManager gameManager;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private Vector2 windowDimensions;

    /**
     * Constructor.
     *
     * @param gameObjectCollection a container for adding/removing instances of GameObjects.
     * @param gameManager object of the main class in game.
     * @param imageReader an ImageReader instance for reading images from files for
     *                     rendering of objects.
     * @param soundReader a SoundReader instance for reading soundClips from files
     *                       for rendering event sounds.
     * @param inputListener an InputListener instance for reading user input.
     * @param windowController an InputListener instance for reading user input.
     * @param windowDimensions pixel dimensions for game window height x width.
     */
    public BrickStrategyFactory(GameObjectCollection gameObjectCollection,
                                BrickerGameManager gameManager,
                                ImageReader imageReader, SoundReader soundReader,
                                UserInputListener inputListener, WindowController windowController,
                                Vector2 windowDimensions) {
        this.gameObjectCollection = gameObjectCollection;
        this.gameManager = gameManager;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Method randomly selects between 5 strategies and returns one CollisionStrategy object
     * which is a RemoveBrickStrategy decorated by one of the decorator strategies,
     * or decorated by two randomly selected strategies, or decorated by one of the decorator
     * strategies and a pair of additional two decorator strategies.
     *
     * @return CollisionStrategy object.
     */
    public CollisionStrategy getStrategy() {
        int randNum = random.nextInt(6);
        RemoveBrickStrategy removeBrickStrategy =
                new RemoveBrickStrategy(gameObjectCollection);
        if(randNum == 0 || randNum == 1 || randNum == 2 || randNum == 3){
            return getOneStrategy(removeBrickStrategy);
        }
        else if(randNum == 4){
            //this case is if we need double Strategy.
            return getTowStrategy(removeBrickStrategy);
        }
        else {
            return removeBrickStrategy;
        }
    }

    /*
     * Method that returns one of the four strategies randomly
     * (puck, addPaddle, changeCamera, narrowWiden).
     * It is called by getStrategy.
     */
    private CollisionStrategy getOneStrategy(CollisionStrategy strategy) {
        int ranNum = random.nextInt(4);
        switch (ranNum) {
            case 0:
                return new PuckStrategy(strategy, imageReader, soundReader);
            case 1:
                return new AddPaddleStrategy(strategy, imageReader,
                        inputListener, windowDimensions);
            case 2:
                return new ChangeCameraStrategy(strategy,
                        windowController, gameManager);
            default:
                return new NarrowWidenPaddleStrategy(strategy,
                        imageReader, gameObjectCollection);
        }
    }

    /*
     * Method that returns randomly one Strategy which takes as parameter
     * CollisionStrategy that is not RemoveBrickStrategy.
     * It is called by getStrategy.
     */
    private CollisionStrategy getTowStrategy(CollisionStrategy removeBrickStrategy){
        CollisionStrategy firstStrategy = getOneStrategy(removeBrickStrategy);
        int ranNum = random.nextInt(5);
        switch (ranNum) {
            case 0:
                return new PuckStrategy(firstStrategy, imageReader, soundReader);
            case 1:
                return new AddPaddleStrategy(firstStrategy, imageReader,
                        inputListener, windowDimensions);
            case 2:
                return new ChangeCameraStrategy(firstStrategy,
                        windowController, gameManager);
            case 3:
                return new NarrowWidenPaddleStrategy(firstStrategy,
                        imageReader, gameObjectCollection);
            default:
                //this case is if we want 3 strategies.
                return getThreeStrategy(firstStrategy);
        }
    }

    /*
     * Returns three Strategies.
     */
    private CollisionStrategy getThreeStrategy(CollisionStrategy firstStrategy) {
        CollisionStrategy secondStrategy = getOneStrategy(firstStrategy);
        return getOneStrategy(secondStrategy);
    }
}

