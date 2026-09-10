import java.util.Random;

/**
 * Class which represents an automatic player.
 * This player plays randomly and he is considered weak.
 * It implements the interface Player.
 */
public class WhateverPlayer implements Player {

    private Random rand = new Random();

    /**
     * Method that performs a turn in game where the player is "whatever".
     * "whatever" puts marks randomly, and he is considered weak in game.
     * @param board an object of type Board.
     * @param mark the mark that the player adds.
     */
    public void playTurn(Board board, Mark mark) {
        boolean trueOrFalse = false;
        while(!trueOrFalse) {
            int randomX = rand.nextInt(Board.SIZE);
            int randomY = rand.nextInt(Board.SIZE);
            trueOrFalse = board.putMark(mark, randomX, randomY);
        }
    }
}
