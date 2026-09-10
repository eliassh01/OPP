/**
 * Class which represents a factory of Players, which constructs
 * different types of Players according to the Player needed to construct.
 */
public class PlayerFactory {
    /**
     * Method that takes a player name and calls the appropriate constructor
     * according to the player type needed.
     * @param playerName the player name needed to construct.
     * @return object of the appropriate type of player, else, if the name is not
     * legal, it returns null.
     */
    public Player buildPlayer(String playerName) {
        switch (playerName) {
            case "human":
                return new HumanPlayer();
            case "whatever":
                return new WhateverPlayer();
            case "clever":
                return new CleverPlayer();
            case "snartypamts":
                return new SnartypamtsPlayer();
            default:
                return null;
        }
    }
}
