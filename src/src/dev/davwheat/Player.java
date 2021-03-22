package dev.davwheat;

import dev.davwheat.enums.BoardSpaceType;
import dev.davwheat.exceptions.AnimalAlreadyOwnedException;

import java.util.Scanner;

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
        final IOHelper ioHelper = new IOHelper();

        // If they're missing this turn, then just stop here.
        if (this.willMissNextTurn) {
            this.willMissNextTurn = false;
            System.out.printf("\n\nPlayer %d (%s) is missing their turn.\n", this.playerId + 1, this.playerName);

            // This should never error... hopefully...
            try {
                this.gameInstance.endTurn(this);
            } catch (final Exception ignored) {
            }
        }

        final GameBoard gameBoard = this.gameInstance.gameBoardInstance;

        System.out.printf("\n\nPlayer %d (%s) is now playing.\n", this.playerId + 1, this.playerName);
        System.out.printf("%s has £%.2f available.\n", this.playerName, this.currentBankBalance);

        gameBoard.printCurrentBoard();

        System.out.println("Press ENTER to roll the dice.");
        // Wait for an ENTER keypress
        new Scanner(System.in).nextLine();
        System.out.println("\n... ROLLING ...");

        final Dice dice = new Dice().rollAllDice();

        final int rollTotal = dice.getTotalRoll();
        final int rollOne = dice.getOneRoll(1);
        final int rollTwo = dice.getOneRoll(2);

        System.out.printf("You rolled %d and %d for a total of %d.\n\n", rollOne, rollTwo, rollTotal);

        final int beforePos = this.currentSpaceIndex;
        final BoardSpace currentSpace = this.movePlayer(rollTotal);
        final int afterPos = this.currentSpaceIndex;

        final boolean justPassedGo = beforePos > afterPos;

        if (justPassedGo) {
            if (currentSpace.type == BoardSpaceType.START) {
                System.out.println("You landed on Start! Collect £1000");
                this.adjustBankBalance(1000);
            } else {
                System.out.println("You just passed Start! Collect £500");
                this.adjustBankBalance(500);
            }
        }

        if (currentSpace.type == BoardSpaceType.ANIMAL) {
            System.out.printf("You landed on \"%s\".\n", currentSpace.displayName);

            final Animal animalSpace = (Animal) currentSpace;
            animalSpace.printCard();

            final Player owner = animalSpace.getOwner();

            if (owner == null) {
                // Animal is not owned

                // Should buy the Animal?
                final boolean shouldBuy = String.valueOf(ioHelper.readChar(String.format("%s is not owned and costs £%.2f. Would you like to buy it? (Y/N)", animalSpace.displayName, animalSpace.purchaseCost), "Please choose either 'Y' (yes) or 'N' (no).", IOHelper.YesNoCharValidator)).equalsIgnoreCase("y");

                if (shouldBuy) {
                    try {
                        animalSpace.purchase(this);
                        System.out.printf("You now own %s! New balance: £%.2f", animalSpace.displayName, this.currentBankBalance);
                    } catch (final AnimalAlreadyOwnedException e) {
                        e.printStackTrace();
                    }
                }
            } else if (owner.equals(this)) {
                // This is their own property!
                System.out.println("You own this animal, so stopping here is free.");
            } else {
                final double stopCost = animalSpace.getStopCost(this);

                System.out.printf("%s is owned by %s, so you need to pay £%.2f.", animalSpace.displayName, owner.playerName, stopCost);
            }
        } else if (currentSpace.type == BoardSpaceType.MISS_NEXT_TURN) {
            System.out.println("You landed on \"Miss next turn\".");
        }

        // This should never error... hopefully...
        try {
            this.gameInstance.endTurn(this);
        } catch (final Exception ignored) {
        }
    }
}
