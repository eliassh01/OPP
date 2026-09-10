package src;

import src.brick_strategies.BrickStrategyFactory;
import src.brick_strategies.CollisionStrategy;
import danogl.util.Counter;
import src.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.util.Random;

/**
 * this class is responsible for game initialization, holding references for game objects
 * and calling update methods for every update iteration.
 * @author elias.
 */
public class BrickerGameManager extends GameManager {

    private static final int BALL_RADIUS = 20;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 15;
    private static final float BALL_SPEED = 100;
    private static final float BRICK_HEIGHT = 15;
    private static final int STARTING_LIFE = 4;
    private static final int NUM_OF_BRICKS = 40;
    private static final int NUMERIC_COUNTER_SIZE = 15;
    private static final int GRAPHIC_COUNTER_SIZE = 15;
    private static final int MIN_DISTANCE_FROM_EDGE = 25;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private Counter livesCounter;
    private Counter bricksCounter;
    private GameObject numericLifeCounter;

    /**
     * Width of the boarder used. It is constant.
     */
    public static final int BORDER_WIDTH = 20;

    /**
     * Constructor.
     * @param windowTitle the title of the game.
     * @param windowDimensions pixel dimensions for game window height x width.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super(windowTitle, windowDimensions);
        livesCounter = new Counter(STARTING_LIFE);
        bricksCounter = new Counter(NUM_OF_BRICKS);
    }

    /**
     * Calling this function should initialize the game window.
     * It should initialize objects in the game window - ball, paddle, walls, life counters, bricks.
     * This version of the game has 5 rows, 8 columns of bricks.
     * @param imageReader an ImageReader instance for reading images from files for
     *                   rendering of objects.
     * @param soundReader a SoundReader instance for reading soundClips from files
     *                    for rendering event sounds.
     * @param inputListener an InputListener instance for reading user input.
     * @param windowController an InputListener instance for reading user input.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        //initialization
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();

        //creating ball
        createBall(imageReader, soundReader);

        //creating paddle
        createPaddle(imageReader, inputListener, windowDimensions);

        //creating borders
        createBorders(windowDimensions);

        //creating background
        createBackground(imageReader, windowController);

        //creating bricks
        BrickStrategyFactory brickStrategyFactory = new BrickStrategyFactory(gameObjects(),
                this, imageReader, soundReader,
                inputListener, windowController, windowDimensions);


        createBricks(imageReader, windowDimensions, bricksCounter, brickStrategyFactory);

        //creating NumericalLifeCounter
        createNumericCounter();

        //creating GraphicLifeCounter
        createGraphicCounter(imageReader);
    }

    /**
     * Code in this function is run every frame update.
     * For internal use by game engine.
     * You do not need to call this method yourself.
     * It checks whether the game has ended or no. it calls
     * checkForGameEnd method.
     * @param deltaTime time between updates.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }

    /**
     * method that returns the ball to the middle of the screen.
     * @param ball Ball object.
     */
    public void repositionBall(GameObject ball){
        ball.setCenter(windowDimensions.mult(0.5f));
    }

    /*
     * Sets the ball velocity. It also sets its direction randomly.
     */
    private void setBall() {
        repositionBall(ball);
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()){
            ballVelX *= -1;
        }
        if(rand.nextBoolean()){
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /*
    * this function checks whether the game has ended or the ball has dropped.
    * if it ended, it asks the user whether he wants to play again.
    * if the ball drops, it returns the ball to the centre.
    * it is called by update method.
     */
    private void checkForGameEnd() {
        float ballHeight = ball.getCenter().y();
        String prompt = "";
        if(livesCounter.value() <= 0){
            prompt = "You lose! Play again?";
        }
        if(bricksCounter.value() <= 0){
            prompt = "You won! Play again?";
        }
        if(ballHeight > windowDimensions.y()) {
            livesCounter.decrement();
            gameObjects().removeGameObject(numericLifeCounter, Layer.BACKGROUND);
            createNumericCounter();
            setBall();
        }
        // we call removingObjectsOutsideScreen method which removes any puck outside the screen.
        removingObjectsOutsideScreen();
        if(!prompt.equals("")){
            if(windowController.openYesNoDialog(prompt)) {
                livesCounter = new Counter(STARTING_LIFE);
                bricksCounter = new Counter(NUM_OF_BRICKS);
                windowController.resetGame();
            }
            else {
                windowController.closeWindow();
            }
        }
    }

    /*
     * this method go over all objects and remove those who are outside the screen
     * from game object collection.
     */
    private void removingObjectsOutsideScreen() {
        for(GameObject gameObject : gameObjects()){
            float objectPositionX = gameObject.getTopLeftCorner().x();
            float objectPositionY = gameObject.getTopLeftCorner().y();
            if(objectPositionX < 0 || objectPositionX > windowDimensions.x() ||
                    objectPositionY < 0 || objectPositionY > windowDimensions.y()){
                gameObjects().removeGameObject(gameObject);
            }
        }
    }

    /*
     * This method creates STARTING_LIFE graphic counters and adds them to game.
     * Distance between each one is 20.
     * It is called by initializer.
     */
    private void createGraphicCounter(ImageReader imageReader) {
        Renderable liveImage = imageReader.readImage("assets/heart.png", true);
        int space = 0;
        for(int row = 0; row < STARTING_LIFE; row++){
            GameObject gameObject = new GraphicLifeCounter(new Vector2(space + 65, 460),
                            new Vector2(GRAPHIC_COUNTER_SIZE, GRAPHIC_COUNTER_SIZE),
                            livesCounter, liveImage, gameObjects(), row + 1);
            gameObjects().addGameObject(gameObject, Layer.BACKGROUND);
            space += 20;
        }
    }

    /*
     * This method creates a numericLifeCounter adds to games.
     * It is called by initializer.
     */
    private void createNumericCounter() {
        numericLifeCounter = new NumericLifeCounter(livesCounter,
                new Vector2(windowDimensions.x()-665, windowDimensions.y()-40),
                new Vector2(NUMERIC_COUNTER_SIZE, NUMERIC_COUNTER_SIZE),
                gameObjects());
        gameObjects().addGameObject(numericLifeCounter, Layer.BACKGROUND);
    }

    /*
     * This method creates 5 row and 8 columns of bricks.
     * Between each one and one there is 2 pixels.
     * It is called by initializer.
     */
    private void createBricks(ImageReader imageReader, Vector2 windowDimensions, Counter counter,
                              BrickStrategyFactory brickStrategyFactory) {
        for(int row = 0; row < 5; row++){
            for(int col = 0; col < 8; col++){
                Renderable brickImage = imageReader.readImage("assets/brick.png", true);
                CollisionStrategy collisionStrategy = brickStrategyFactory.getStrategy();
                float brickLength = (windowDimensions.x()-64) / 8;
                GameObject brick = new Brick(Vector2.ZERO,
                        new Vector2(brickLength, BRICK_HEIGHT),
                        brickImage, collisionStrategy, counter);
                brick.setTopLeftCorner(new Vector2(MIN_DISTANCE_FROM_EDGE + (col*brickLength) + (2*col),
                        MIN_DISTANCE_FROM_EDGE + 5 + (row*BRICK_HEIGHT) + (2*row)));
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    /*
     * method that creates the background.
     * It is called by initializer.
     */
    private void createBackground(ImageReader imageReader, WindowController windowController) {
        GameObject background = new GameObject(
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /*
     * creates upper,right and left boarders to the game.
     * It is called by initializer.
     */
    private void createBorders(Vector2 windowDimensions) {
        GameObject leftBorder = new GameObject(Vector2.ZERO,
                new Vector2(BORDER_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(leftBorder);
        GameObject rightBorder = new GameObject(new Vector2(windowDimensions.x()-BORDER_WIDTH, 0),
                new Vector2(BORDER_WIDTH, windowDimensions.y()), null);
        gameObjects().addGameObject(rightBorder);
        GameObject upperBorder = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), BORDER_WIDTH), null);
        gameObjects().addGameObject(upperBorder);
    }

    /*
     * creates paddle in the middle down of the screen, with a length and width.
     * It is called by initializer.
     */
    private void createPaddle(ImageReader imageReader, UserInputListener inputListener,
                              Vector2 windowDimensions) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        GameObject paddle = new Paddle(Vector2.ZERO, new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage, inputListener, windowDimensions, MIN_DISTANCE_FROM_EDGE);
        paddle.setCenter(new Vector2( windowDimensions.x()/2, windowDimensions.y()-20));
        paddle.setTag("main Paddle");
        gameObjects().addGameObject(paddle);
    }

    /*
     * creates the main ball of the game with a constant size.
     * it is initialized in the middle of the screen and given the
     * tag "main ball". It is called by initializer.
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage =  imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS),
                ballImage, collisionSound);
        ball.setTag("main ball");
        repositionBall(ball);
        setBall();
        gameObjects().addGameObject(ball);
    }

    /**
     * Entry point for game.
     */
    public static void main(String[] args) {
        new BrickerGameManager("Bricker",
                new Vector2(700, 500)).run();
    }
}
