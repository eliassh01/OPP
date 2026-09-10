/**
 * Class which represent a tic-tac-toe game played during a tournament.
 * Each game is composed of a number of turns.
 * At the end of each game one player wins or the game end as a draw.
 */
public class Game {

    private Player playerX;
    private Player playerO;
    private Renderer renderer;

    /**
     * Constructor.
     * Initialises all the fields in the class
     * @param playerX an object of the player that plays with the mark X.
     * @param playerO and object of the player that plays with the mark O.
     * @param renderer and object of renderer which is needed to print the
     *                board if the player is human.
     */
    public Game(Player playerX, Player playerO, Renderer renderer){
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
    }

    /**
     * method that runs the game and at the end it returns the winner.
     * @return the winner in the game, if the game ends draw, it returns Mark.BLANK.
     */
    public Mark run(){
        Board board = new Board();
        int counter = 0;
        while(!board.gameEnded()){
            if(counter % 2 == 0){
                playerX.playTurn(board, Mark.X);
            }
            else{
                playerO.playTurn(board, Mark.O);
            }
            counter++;
            renderer.renderBoard(board);
        }
        return board.getWinner();
    }
}
