/**
 * interface Player which has playTurn method.
 * this method is implemented by the classes which implements Player.
 */
public interface Player {
    /**
     * This is playTurn method which is implemented by all
     * classes which implement this class.
     * @param board object of Board.
     * @param mark mark that player adds.
     */
    void playTurn(Board board, Mark mark);
}
