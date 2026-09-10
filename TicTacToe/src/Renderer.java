/**
 * Interface Renderer which has renderBoard method.
 * This method is implemented by the classes which implements Renderer.
 */
public interface Renderer {
    /**
     * This is rendererBoard method which is implemented by all
     * classes which implement this class.
     * @param board object of Board.
     */
    void renderBoard(Board board);
}
