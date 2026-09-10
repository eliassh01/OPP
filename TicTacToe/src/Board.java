/**
 * Class which represents the board on which the tournament is taking place.
 * It contains methods which checks whether the game has ended or not and
 * who is the winner.
 * It also has methods which add a Mark on the board and return the Mark
 * in a given coordinates.
 */
public class Board {

    /**
     * Board contains SIZE rows and SIZE columns.
     */
    public static final int SIZE = 6;

    /**
     * Length of the 'O' or 'X' sequence needed to win.
     */
    public static final int WIN_STREAK = 4;

    private Mark[][] boardWithMarks;
    private int fullCellsInBoard;
    private boolean gameEnded;
    private Mark winner;

    /**
     * Constructor.
     * Initialise boardWithMarks and fill it with Mark.BLANK.
     * Initialises gameEnded to be false, winner to be BLANK.
     */
    public Board() {
        this.boardWithMarks = new Mark[SIZE][SIZE];
        gameEnded = false;
        winner = Mark.BLANK;
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                this.boardWithMarks[row][col] = Mark.BLANK;
            }
        }
    }

    /**
     * Method that adds a Mark on the board if the coordinates are legal and empty.
     * @param mark Mark to add to board.
     * @param row row number to put the Mark in.
     * @param col column number to put the Mark in.
     * @return if adding was successful true, else false.
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }
        if (boardWithMarks[row][col] == Mark.X || boardWithMarks[row][col] == Mark.O) {
            return false;
        }
        boardWithMarks[row][col] = mark;
        checkForWinner(row, col, mark);
        fullCellsInBoard += 1;
        return true;
    }


    /**
     * Method that returns the Mark in coordinated (row, col) on the board if coordinates are legal.
     * @param row row number to put the Mark in.
     * @param col column number to put the Mark in.
     * @return the Mark int coordinates (row, col) if coordinates are legal, else return BLANK.
     */
    public Mark getMark(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return Mark.BLANK;
        }
        return boardWithMarks[row][col];
    }

    /**
     * Checks if the game ended.
     * @return if the game ended, returns true. else, false.
     */
    public boolean gameEnded(){
        if(fullCellsInBoard >= SIZE * SIZE){
            gameEnded = true;
        }
        return gameEnded;
    }

    /**
     * @return return the winner in the game.
     */
    public Mark getWinner() {
        return winner;
    }

    /*
     * Method that counts the number of marks on the board that are in a streak.
     * The counting begins from (row, col) coordinate and move in the direction
     * according to rowDelta (up, down or stay in place) and colDelta (right, left, or stay in place).
     * it is private.
     */
    private int countMarkInDirection(int row, int col, int rowDelta, int colDelta, Mark mark) {
        int count = 0;
        while(row < SIZE && row >= 0 && col < SIZE && col >= 0 && boardWithMarks[row][col] == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }
        return count;
    }

    /*
     * Method that after a player adds a Mark on it checks whether he won or no.
     * It is called by the method putMark.
     * It calls the method countMarkInDirection.
     * It is private.
     */
    private void checkForWinner(int row, int col, Mark mark) {
        int rowSequenceLength = countMarkInDirection(row, col, 0, 1, mark);
        rowSequenceLength += countMarkInDirection(row, col, 0, -1, mark);
        int colSequenceLength = countMarkInDirection(row, col, 1, 0, mark);
        colSequenceLength += countMarkInDirection(row, col, -1, 0, mark);
        int leftDiagonalSequenceLength = countMarkInDirection(row, col, 1, 1, mark);
        leftDiagonalSequenceLength += countMarkInDirection(row ,col, -1, -1, mark);
        int rightDiagonalSequenceLength = countMarkInDirection(row, col, -1, 1, mark);
        rightDiagonalSequenceLength += countMarkInDirection(row, col, 1, -1, mark);
        if(rowSequenceLength - 1 >= WIN_STREAK || colSequenceLength - 1 >= WIN_STREAK
                || leftDiagonalSequenceLength - 1 >= WIN_STREAK ||
                rightDiagonalSequenceLength - 1 >= WIN_STREAK) {
            winner = mark;
            gameEnded = true;
        }
    }
}