/**
 * Class which represents an automatic player.
 * This player is considered clever and can win most
 * of the games in tournament when he plays against "whatever".
 * It implements the interface Player.
 */
public class CleverPlayer implements Player {

    /**
     * Method that performs a turn in game where the player is "clever".
     * "clever" is clever and adds the Marks on the board in a certain
     * direction so that he can reach the WIN_STREAK fast.
     * @param board an object of type Board.
     * @param mark the mark that the player adds.
     */
    public void playTurn(Board board, Mark mark) {
        for(int row = 0; row < Board.SIZE; row++) {
            for(int col = 0; col < Board.SIZE; col++) {
                boolean trueOrFalse = board.putMark(mark, row, col);
                if(trueOrFalse) {
                    return;
                }
            }
        }
    }
}
