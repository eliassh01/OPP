package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.Color;

/**
 * Represents the halo of sun.
 */
public class SunHalo {

    private static final int HALO_SIZE = 310;

    /**
     *  Creates the sun's Halo
     * @param gameObjects The collection of all participating game objects.
     * @param layer The layer of the sun's halo
     * @param sun the game's sun
     * @param color the color of the halo
     * @return A sun's halo
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color) {
        GameObject sunHalo = new GameObject(Vector2.ZERO,
                new Vector2(HALO_SIZE, HALO_SIZE),
                new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        sunHalo.setTag("sunHalo");
        gameObjects.addGameObject(sunHalo, layer);
        return sunHalo;
    }
}
