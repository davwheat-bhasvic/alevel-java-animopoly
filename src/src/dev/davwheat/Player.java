package dev.davwheat;

/**
 * A Player who is engaged in the current game.
 */
public class Player {
    /**
     * The player's index in the Game's list of all players.
     * <p>
     * Starts at 0.
     */
    public final int playerId;

    /**
     * The player's name.
     */
    public final String playerName;

    /**
     * Visual identifier for the player. Acts as a way to
     * identify the player on the game board.
     */
    public final char playerVisualIdentifier;

    /**
     * Current position on the board as an index.
     * <p>
     * Valid range is 0 to 25 inclusive (as there are only 26 spaces).
     */
    private int currentSpaceIndex;

    /**
     * The player's current bank balance.
     */
    private double currentBankBalance;

    /**
     * Instance of Game that this Player is part of.
     */
    private final Game gameInstance;

    /**
     * Create a new instance of Player.
     *
     * @param name                   The Player's name
     * @param playerId               The Player's ID (0-index in game's list of players)
     * @param game                   The current game instance.
     * @param playerVisualIdentifier The visual identifier to use to show the player on the game board.
     */
    public Player(String name, int playerId, Game game, char playerVisualIdentifier) {
        this.currentSpaceIndex = 0;
        // TODO: Check starting balance.
        this.currentBankBalance = 500;
        this.gameInstance = game;
        this.playerId = playerId;
        this.playerName = name;
        this.playerVisualIdentifier = playerVisualIdentifier;
    }

    /**
     * Moves a player by a specified number of spaces.
     *
     * @param spaces Number of spaces to move.
     * @return The new board space the player is at.
     */
    public BoardSpace movePlayer(int spaces) {
        currentSpaceIndex += spaces;

        // Ensure the index isn't above 25 or below 0 -- wrap around instead
        if (currentSpaceIndex > 25 || currentSpaceIndex < 0) {
            currentSpaceIndex = Math.abs(currentSpaceIndex % 25);
        }

        return getBoardSpaceAtPlayerPosition();
    }

    /**
     * Get the BoardSpace that the user is currently on.
     *
     * @return The board space that the player is located on.
     */
    public BoardSpace getBoardSpaceAtPlayerPosition() {
        return gameInstance.gameBoardInstance.getBoardSpaceAtPosition(currentSpaceIndex);
    }

    /**
     * Gets the Player's bank balance.
     *
     * @return bank balance
     */
    public double getBankBalance() {
        return this.currentBankBalance;
    }

    /**
     * Adjusts the Player's bank balance by a fixed amount.
     * <p>
     * You can also pass another Player here to adjust their balance by the opposite amount!
     * <p>
     * For example... "adjustBankBalance(5, player2);" will add £5 to this player, removes £5 from player2.
     *
     * @param change                 Amount to change the balance by (+/-)
     * @param reciprocalActionPlayer An player to perform the opposite change on.
     * @return New player balance.
     */
    public double adjustBankBalance(double change, Player reciprocalActionPlayer) {
        if (reciprocalActionPlayer == null) {
            throw new NullPointerException("reciprocalActionPlayer must be a valid instance of Player, and not null.");
        }

        reciprocalActionPlayer.adjustBankBalance(-change);
        return this.adjustBankBalance(change);
    }


    /**
     * Adjusts the Player's bank balance by a fixed amount.
     * <p>
     * You can also pass another Player here to adjust their balance by the opposite amount!
     * <p>
     * For example... "adjustBankBalance(5, player2);" will add £5 to this player, removes £5 from player2.
     *
     * @param change Amount to change the balance by (+/-)
     * @return New player balance.
     */
    public double adjustBankBalance(double change) {
        this.currentBankBalance += change;
        return this.currentBankBalance;
    }

    /**
     * Gets if a player has gone bankrupt (negative money).
     * <p>
     * £0 is not bankrupt, as they could earn money from someone landing on their animal.
     *
     * @return Player has lost
     */
    public boolean hasLost() {
        return currentBankBalance < 0;
    }

    public void startTurn() {
        // TODO: Implement Player startTurn logic.
    }
}
