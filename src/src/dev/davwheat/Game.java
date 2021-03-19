package dev.davwheat;

import javax.naming.NoPermissionException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

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
        this.gameBoardInstance = new GameBoard(this);
        this.cardDeck = Deck.createBaseDeck();
    }

    /**
     * Gets the list of all players in the Game.
     *
     * @return List of all players
     */
    public ArrayList<Player> getPlayers() {
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
     * @throws NoPermissionException Actor is not the active player
     */
    public void endTurn(Player actor) throws NoPermissionException {
        if (actor != activePlayer) {
            throw new NoPermissionException("Only the active player can end the turn.");
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
        this.players = new ArrayList<>();

        /**
         * We use this to add custom validation for player names to prevent multiple players choosing
         * the same name.
         *
         * This is passed to the IOHelper `readString` method.
         */
        final Function<String, Boolean> isValidPlayerName = (String playerName) -> {
            AtomicBoolean matchesExistingPlayer = new AtomicBoolean(false);
            this.players.forEach((player) -> {
                if (playerName.equalsIgnoreCase(player.playerName)) matchesExistingPlayer.set(true);
            });
            return !matchesExistingPlayer.get();
        };

        /**
         * We use this to add custom validation for player chars to prevent multiple players choosing
         * the same character to represent themselves with.
         *
         * This is passed to the IOHelper `readChar` method.
         */
        final Function<Character, Boolean> isValidPlayerChar = (Character playerChar) -> {
            AtomicBoolean matchesExistingPlayer = new AtomicBoolean(false);
            this.players.forEach((player) -> {
                if (playerChar.toString().equalsIgnoreCase(player.playerName.toString()))
                    matchesExistingPlayer.set(true);
            });
            return !matchesExistingPlayer.get();
        };

        // Get the number of players playing
        do {
            playerCount = ioHelper.readInteger("How many players are playing?", "Invalid input -- please enter a whole number");
        } while (playerCount < 2 || playerCount > 8);

        // Creates all the players!
        for (int i = 0; i < playerCount; i++) {
            String name = ioHelper.readString("Enter name for Player " + (i + 1), "Please enter a valid name that hasn't been chosen before.", isValidPlayerName);
            System.out.printf("Hello %s!", name);
            System.out.println("");

            char pieceIdentifier = ioHelper.readChar("Choose a character to represent yourself.", "Please enter a character that is A-Z, 0-9, or !£%", isValidPlayerChar);

            this.players.add(new Player(name, i, this, pieceIdentifier));
        }
    }
}
