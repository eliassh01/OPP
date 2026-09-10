/**
 * Class which represent a tournament played between different type of players.
 * Each tournament is composed of a number of Games.
 * each Game is a tic-tac-toe game.
 * At the end of the tournament it represents the number of games each player won and
 * the number of draws.
 * It contains the main.
 * @author Elias.
 */
public class Tournament {

    private static final int NUM_ROUNDS = 1;
    private static final int RENDERER = 2;
    private static final int PLAYER_1 = 3;
    private static final int PLAYER_2 = 4;
    private static final String ARGUMENTS_ERROR = "Usage: java Tournament [round count] " +
            "[render target: console/none] [player1: human/clever/whatever/snartypamts]" +
            " [player2: human/clever/whatever/snartypamts]";

    public static void main(String[] args) {
        if (args.length != 5){
            System.err.println(ARGUMENTS_ERROR);
            return;
        }
        PlayerFactory playerFactory = new PlayerFactory();
        RendererFactory rendererFactory = new RendererFactory();
        int rounds = Integer.parseInt(args[NUM_ROUNDS]);
        Renderer renderer = rendererFactory.buildRenderer(args[RENDERER]);
        Player[] players = {playerFactory.buildPlayer(args[PLAYER_1]),
                playerFactory.buildPlayer(args[PLAYER_2])};
        if(rounds <= 0 || renderer == null || players[0] == null || players[1] == null) {
            System.err.println(ARGUMENTS_ERROR);
            return;
        }
        Tournament tournament = new Tournament(rounds, renderer, players);
        tournament.playTournament();;
    }

    private int rounds;
    private final Renderer renderer;
    private int[] playersNumWins;
    private Player[] players;

    /**
     * Constructor.
     * @param rounds number of rounds in the tournament.
     * @param renderer an object that renders a given board to the console.
     * @param players the players (objects) that will play in the Tournament.
     */
    public Tournament(int rounds, Renderer renderer, Player[] players) {
        this.rounds = rounds;
        this.renderer = renderer;
        playersNumWins = new int[3];
        this.players = new Player[players.length];
        for(int i = 0; i < players.length; i++) {
            this.players[i] = players[i];
        }
    }

    /**
     * this method operates a Tournament between two players.
     * after each round it switches who is playing with Mark X and Y.
     * for example, if player1 is X and player2 is Y at this round,
     * int the next round player1 will be Y and players2 X;
     */
    public void playTournament(){
        Game game;
        for(int i = 0; i < rounds; i++) {
            game = new Game(players[i % 2], players[(i + 1) % 2], renderer);
            Mark markOfWinner = game.run();
            if(markOfWinner == Mark.X) {
                playersNumWins[i % 2]++;
            }
            else if(markOfWinner == Mark.O) {
                playersNumWins[(i + 1) % 2]++;
            }
            else {
                playersNumWins[2]++;
            }
        }
        System.out.printf("=== player 1: %d | player 2: %d | Draws: %d ===\r",
                    playersNumWins[0], playersNumWins[1], playersNumWins[2]);
    }
}
