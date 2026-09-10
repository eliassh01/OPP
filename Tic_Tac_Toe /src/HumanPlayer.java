import java.util.Scanner;

/**
 * Class which represents a human (true) player.
 * During the game this player is asked to enter where
 * he wants to add the Mark on the board.
 */
public class HumanPlayer implements Player {

    private Scanner in = new Scanner(System.in);

    /**
     * A method that performs a turn in game where the player is human, and
     * he chooses the coordinates.
     * @param board an object of type Board.
     * @param mark the mark that the player adds.
     */
    public void playTurn(Board board, Mark mark) {
        System.out.println("Player " + mark + ", type coordinates: ");
        boolean trueOrFalse = false;
        while(!trueOrFalse) {
            int num = in.nextInt();
            int col = (num % 10) - 1;
            int row = (num / 10) - 1;
            trueOrFalse = board.putMark(mark, row, col);
            if(!trueOrFalse) {
                System.out.println("Invalid coordinates, type again: ");
            }
        }
    }
}
