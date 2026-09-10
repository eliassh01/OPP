package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * Represents the sun - moves across the sky in an elliptical path.
 */
public class Sun {

    private static final int SUN_SIZE = 200;
    private static final float ELLIPSE_DIA_A = 500;
    private static final float ELLIPSE_DIA_B = 700;
    private static GameObject sun;


    /**
     * This function creates a yellow circle that moves
     * in the sky in an elliptical path (in camera coordinates).
     * @param gameObjects The collection of all participating game objects.
     * @param layer  The number of the layer to which the created sun should be added.
     * @param windowDimensions The number of the layer to which the created sun
     *                         should be added.
     * @param cycleLength The amount of seconds it should take the created
     *                    game object to complete a full cycle.
     * @return A new game object representing the sun.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength) {
        sun = new GameObject(new Vector2(120,140),
                new Vector2(SUN_SIZE ,SUN_SIZE),
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");
        gameObjects.addGameObject(sun, layer);
        new Transition<Float>(
                sun,
                (angle) -> sun.setCenter(calcSunPosition(windowDimensions, angle)),(float)(3*Math.PI/2),
                (float)((3*Math.PI/2) + 2*Math.PI),
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);

        return sun;
    }

    /**
     * Private method that calcualtes the sun next position in the frame
     * @param windowDimensions window dimensions of the game
     * @param angleInSky the current suns angle in the sky/window
     * @return new vector which is the new location if the sun
     */
    private static Vector2 calcSunPosition(Vector2 windowDimensions,
                                           float angleInSky) {
        float centreX = windowDimensions.x() * 0.5f;
        float centreY = windowDimensions.y() * 0.5f;
        float positionX = (float) (centreX + (ELLIPSE_DIA_A*Math.cos(angleInSky)));
        float positionY = (float) (centreY + (ELLIPSE_DIA_B*Math.sin(angleInSky)));
        return new Vector2(positionX, positionY);
    }

}
