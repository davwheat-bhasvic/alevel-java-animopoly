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
    public GameBoard gameBoardInstance;

    /**
     * Creates an instance of the `Game` class.
     */
    public Game() {
        this.initialiseGame();
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
    public void endTurn(final Player actor) throws NoPermissionException {
        if (actor != this.activePlayer) {
            throw new NoPermissionException("Only the active player can end the turn.");
        }

        // Get the index of the current active player
        final int activePlayerIndex = this.players.indexOf(actor);

        boolean hasPlayerLost = true;

        while (hasPlayerLost) {
            if (activePlayerIndex + 1 >= this.players.toArray().length) {
                // This is the last player in the list, we need to wrap
                // around to the start of the list
                this.activePlayer = this.players.get(0);
            } else {
                this.activePlayer = this.players.get(activePlayerIndex + 1);
            }
            hasPlayerLost = this.activePlayer.hasLost();
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
        final IOHelper ioHelper = new IOHelper();

        this.gameBoardInstance = new GameBoard(this);
        this.createPlayers(ioHelper);

        this.activePlayer = this.players.get(0);
        System.out.printf("Player 1 (%s) will start the game.\n\n", this.activePlayer.playerName);

        while (!this.onlyOnePlayerLeft()) {
            this.activePlayer.startTurn();
        }
    }

    private boolean onlyOnePlayerLeft() {
        final int playersLeft = (int) this.players.stream().filter(player -> !player.hasLost()).count();

        return playersLeft <= 1;
    }

    /**
     * Internal method to propagate the player list.
     *
     * @param ioHelper IOHelper object
     * @hidden
     */
    private void createPlayers(final IOHelper ioHelper) {
        final int playerCount;
        this.players = new ArrayList<>();

        /*
         * We use this to add custom validation for player names to prevent multiple players choosing
         * the same name.
         *
         * This is passed to the IOHelper `readString` method.
         */
        final Function<String, Boolean> isValidPlayerName = (String playerName) -> {
            final AtomicBoolean matchesExistingPlayer = new AtomicBoolean(false);
            this.players.forEach((player) -> {
                if (playerName.equalsIgnoreCase(player.playerName)) matchesExistingPlayer.set(true);
            });
            return !matchesExistingPlayer.get();
        };

        /*
         * We use this to add custom validation for player chars to prevent multiple players choosing
         * the same character to represent themselves with.
         *
         * This is passed to the IOHelper `readChar` method.
         */
        final Function<Character, Boolean> isValidPlayerChar = (Character playerChar) -> {
            final AtomicBoolean matchesExistingPlayer = new AtomicBoolean(false);
            this.players.forEach((player) -> {
                if (playerChar.toString().equalsIgnoreCase(String.valueOf(player.playerVisualIdentifier)))
                    matchesExistingPlayer.set(true);
            });
            return !matchesExistingPlayer.get();
        };

        // Get the number of players playing (must be between 2 and 8)
        playerCount = ioHelper.readInteger("How many players are playing?", "Invalid input -- please enter a whole number between 2 and 8 (inclusive)", (Integer count) -> !(count < 2 || count > 8));

        // Creates all the players!
        for (int i = 0; i < playerCount; i++) {
            final String name = ioHelper.readString("Enter name for Player " + (i + 1), "Please enter a valid name that hasn't been chosen by another player.", isValidPlayerName);
            System.out.printf("Hello %s!", name);
            System.out.println();

            final char pieceIdentifier = ioHelper.readChar("Choose a character to represent yourself.", "Please enter a character that is A-Z, 0-9, or one of !£%?*, and hasn't been chosen by another player.", isValidPlayerChar);

            this.players.add(new Player(name, i, this, pieceIdentifier));
        }
    }
}
