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
     * Whether this player should miss their next turn.
     * <p>
     * To set this to true, call `makeMissNextTurn()`.
     */
    private boolean willMissNextTurn;

    /**
     * Create a new instance of Player.
     *
     * @param name                   The Player's name
     * @param playerId               The Player's ID (0-index in game's list of players)
     * @param game                   The current game instance.
     * @param playerVisualIdentifier The visual identifier to use to show the player on the game board.
     */
    public Player(final String name, final int playerId, final Game game, final char playerVisualIdentifier) {
        this.currentSpaceIndex = 0;
        this.currentBankBalance = 2000;
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
    public BoardSpace movePlayer(final int spaces) {
        this.currentSpaceIndex += spaces;

        // Ensure the index isn't above 25 or below 0 -- wrap around instead
        if (this.currentSpaceIndex > 25 || this.currentSpaceIndex < 0) {
            this.currentSpaceIndex = Math.abs(this.currentSpaceIndex % 25);
        }

        return this.getBoardSpaceAtPlayerPosition();
    }

    /**
     * Get the BoardSpace that the user is currently on.
     *
     * @return The board space that the player is located on.
     */
    public BoardSpace getBoardSpaceAtPlayerPosition() {
        return this.gameInstance.gameBoardInstance.getBoardSpaceAtPosition(this.currentSpaceIndex);
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
    public double adjustBankBalance(final double change, final Player reciprocalActionPlayer) {
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
    public double adjustBankBalance(final double change) {
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
        return this.currentBankBalance < 0;
    }

    /**
     * Makes this player miss their next turn.
     */
    public void makeMissNextTurn() {
        this.willMissNextTurn = true;
    }

    public void startTurn() {
        // TODO: Implement Player startTurn logic.

        // If they're missing this turn, then just stop here.
        if (this.willMissNextTurn) {
            this.willMissNextTurn = false;
            System.out.printf("Player %d (%s) is missing their turn.\n", this.playerId + 1, this.playerName);

            // This should never error... hopefully...
            try {
                this.gameInstance.endTurn(this);
            } catch (final Exception ignored) {
            }
        }

        System.out.printf("Player %d (%s) is now playing.\n", this.playerId + 1, this.playerName);
        System.out.printf("%s has £%.2f available.\n", this.playerName, this.currentBankBalance);

        this.gameInstance.gameBoardInstance.printCurrentBoard();


        // This should never error... hopefully...
        try {
            this.gameInstance.endTurn(this);
        } catch (final Exception ignored) {
        }
    }
}
