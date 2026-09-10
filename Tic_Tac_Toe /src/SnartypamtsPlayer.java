/**
 * Class which represents an automatic player.
 * This player is considered even more clever than "clever" and can win most
 * of the games in tournament when he plays against "clever" and "whatever".
 * It implements the interface Player.
 */
public class SnartypamtsPlayer implements Player {

    /**
     * Method that performs a turn in game where the player is "snartypamts".
     * This player is even more clever than "clever".
     * @param board an object of type Board.
     * @param mark the mark that the player adds.
     */
    public void playTurn(Board board, Mark mark) {
        for(int col = 1; col < Board.SIZE; col++) {
            for(int row = 0; row < Board.SIZE; row++) {
                boolean trueOrFalse = board.putMark(mark, row, col);
                if(trueOrFalse) {
                    return;
                }
            }
        }
        // this loop so that he will return to the first column if needed.
        for(int row = 0; row < Board.SIZE; row++){
            boolean trueOrFalse = board.putMark(mark, row, 0);
            if(trueOrFalse) {
                return;
            }
        }
    }
}
