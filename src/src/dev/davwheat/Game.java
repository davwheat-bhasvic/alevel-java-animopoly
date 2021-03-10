package dev.davwheat;

import dev.davwheat.exceptions.InvalidActorException;

import java.util.ArrayList;

/**
 * Manages the game state and runs the game itself.
 */
public class Game {

    /**
     * List of all players in the game.
     */
    private ArrayList<Player> players;

    /**
     * The player who is currently taking their turn.
     */
    private Player activePlayer;

    /**
     * The full deck of cards used within the game.
     */
    public final Deck cardDeck;

    /**
     * The GameBoard associated with the current game.
     */
    public final GameBoard gameBoardInstance;

    /**
     * Creates an instance of the `Game` class.
     */
    public Game() {
        this.initialiseGame();
    }

    /**
     * Gets the list of all players in the Game.
     *
     * @return List of all players
     */
    public Player getPlayers() {
        return this.players;
    }

    /**
     * Gets the player currently taking their turn.
     *
     * @return Player currently taking their turn
     */
    public Player getActivePlayer() {
        return this.activePlayer;
    }

    /**
     * End the active player's turn.
     *
     * @param actor The Player class which is calling this method.
     * @throws InvalidActorException
     */
    public void endTurn(Player actor) throws InvalidActorException {
        if (actor != activePlayer) {
            throw new InvalidActorException("Only the active player can end the turn.");
        }

        // Get the index of the current active player
        int activePlayerIndex = players.indexOf(actor);

        if (activePlayerIndex + 1 >= players.toArray().length) {
            // This is the last player in the list, we need to wrap
            // around to the start of the list

            activePlayer = players.get(0);
        } else {
            activePlayer = players.get(activePlayerIndex + 1);
        }
    }

    /**
     * Sets up the new instance of Game.
     * <p>
     * This is called automatically within the Game constructor so doesn't need to be used in most cases.
     *
     * @hidden
     */
    public void initialiseGame() {
        IOHelper ioHelper = new IOHelper();

        int playerCount;
        this.players = new ArrayList<Player>();

        // Get the number of players playing
        do {
            playerCount = ioHelper.readInteger("How many players are playing?", "Invalid input -- please enter a whole number");
        } while (playerCount < 2 || playerCount > 8);

        // Creates all the players!
        for (int i = 0; i < playerCount; i++) {
            String name = ioHelper.readString("Enter name for Player " + (i + 1), "Invalid name.");
            System.out.println("Hello " + name + "!");
            System.out.println("");

            this.players.add(new Player(name, i));
        }
    }
}
