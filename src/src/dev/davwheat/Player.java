package dev.davwheat;

import dev.davwheat.enums.BoardSpaceType;
import dev.davwheat.enums.Color;
import dev.davwheat.exceptions.AnimalAlreadyOwnedException;
import dev.davwheat.exceptions.AnimalNotOwnedException;
import dev.davwheat.exceptions.AnimalUpgradeNotAllowedException;
import dev.davwheat.exceptions.InsufficientBalanceException;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    public double adjustBankBalance(final double change, final Player reciprocalActionPlayer) throws InsufficientBalanceException {
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
    public double adjustBankBalance(final double change) throws InsufficientBalanceException {
        if (change < 0 && this.currentBankBalance < -change) {
            throw new InsufficientBalanceException("Not enough money for this balance change.");
        }

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

        System.out.printf("%s%s", Color.RESET, Color.WHITE_BOLD_BRIGHT);
        System.out.printf("\n\nPlayer %d (%s) is now playing.\n", this.playerId + 1, this.playerName);
        System.out.printf("%s", Color.RESET);
        System.out.printf("%s has %s£%.2f%s available.\n", this.playerName, Color.GREEN_BOLD_BRIGHT, this.currentBankBalance, Color.RESET);

        gameBoard.printCurrentBoard();

        System.out.println("Press ENTER to roll the dice.");
        ioHelper.pressEnterToContinue();

        System.out.println("... ROLLING ...");
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
                try {
                    this.adjustBankBalance(1000);
                } catch (InsufficientBalanceException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("You just passed Start! Collect £500");
                try {
                    this.adjustBankBalance(500);
                } catch (InsufficientBalanceException e) {
                    e.printStackTrace();
                }
            }
        }

        if (currentSpace.type == BoardSpaceType.ANIMAL) {
            System.out.printf("You landed on \"%s\".\n", currentSpace.displayName);

            final Animal animalSpace = (Animal) currentSpace;
            animalSpace.printCard();
            System.out.println();

            final Player owner = animalSpace.getOwner();

            if (owner == null) {
                // Animal is not owned

                // Should buy the Animal?
                final boolean shouldBuy = String.valueOf(ioHelper.readChar(String.format("%s is not owned and costs £%.2f. Would you like to buy it? (Y/N)", animalSpace.displayName, animalSpace.purchaseCost), "Please choose either 'Y' (yes) or 'N' (no).", IOHelper.YesNoCharValidator)).equalsIgnoreCase("y");

                if (shouldBuy) {
                    try {
                        animalSpace.purchase(this);
                        System.out.printf("%sYou now own %s! New balance: £%.2f%s\n", Color.BLUE_BOLD, animalSpace.displayName, this.currentBankBalance, Color.RESET);
                    } catch (final AnimalAlreadyOwnedException e) {
                        e.printStackTrace();
                    } catch (final InsufficientBalanceException e) {
                        System.out.println("You can't afford to pay for this Animal.");
                    }
                }
            } else if (animalSpace.isOwnedBy(this)) {
                // This is their own property!
                System.out.printf("%sYou own this animal, so stopping here is free.%s\n", Color.BLUE_BOLD, Color.RESET);
            } else {
                final double stopCost = animalSpace.getStopCost(this);

                System.out.printf("%s is owned by %s, so you need to %spay them £%.2f%s.\n", animalSpace.displayName, owner.playerName, Color.RED_BOLD_BRIGHT, stopCost, Color.RESET);
                System.out.println("Press ENTER to continue.");
                ioHelper.pressEnterToContinue();

                try {
                    animalSpace.payForStop(this);
                } catch (InsufficientBalanceException e) {
                    System.out.println("You can't afford to pay for this stop. You're out!");
                    this.currentBankBalance = -1;
                    return;
                }
                System.out.printf("Your new balance is %s£%.2f%s.\n", Color.GREEN_BOLD_BRIGHT, this.currentBankBalance, Color.RESET);
            }
        } else if (currentSpace.type == BoardSpaceType.MISS_NEXT_TURN) {
            System.out.println("You landed on \"Miss next turn\".");
        }

        if (dice.isDouble()) {
            final char chance = ioHelper.readChar("You rolled a double! Would you like to pick up a Chance card? (Y/N)", "Please choose either Y for yes or N for no.", IOHelper.YesNoCharValidator);

            if (String.valueOf(chance).equalsIgnoreCase("y")) {
                // Pick up chance card
                final Card card = this.gameInstance.cardDeck.takeCard();
                card.printCard();
                System.out.println("\nPress ENTER to continue.");
                ioHelper.pressEnterToContinue();

                // Take the action
                try {
                    card.takeAction(this);
                } catch (InsufficientBalanceException e) {
                    System.out.println("You can't afford to pay what this card requires. You're out!");
                    this.currentBankBalance = -1;
                    return;
                }

                System.out.printf("Your new balance is %s£%.2f%s.\n", Color.GREEN_BOLD_BRIGHT, this.currentBankBalance, Color.RESET);
            }
        }

        int option = 0;

        while (option != 2) {
            System.out.printf("\n%sPlayer turn menu%s\n", Color.BLUE_BOLD_BRIGHT, Color.RESET);
            System.out.println("\n1. Upgrade Animal");
            System.out.println("2. End turn");
            option = ioHelper.readInteger("Choose an option: ", "Please choose either 1 or 2.", i -> i >= 1 && i <= 2);

            switch (option) {
                case 1 -> this.upgradeAnimal(ioHelper);
            }
        }

        // This should never error... hopefully...
        try {
            this.gameInstance.endTurn(this);
        } catch (final Exception ignored) {
        }
    }

    private void upgradeAnimal(final IOHelper ioHelper) {
        while (true) {
            System.out.printf("\n%sUpgrade an animal%s\n", Color.BLUE_BOLD_BRIGHT, Color.RESET);
            final List<Animal> myAnimals = this.gameInstance.gameBoardInstance.getOwnedAnimals(this);

            final AtomicInteger i = new AtomicInteger(1);
            myAnimals.forEach(a -> {
                final int num = i.getAndIncrement();
                System.out.printf("%d. %s (Current: L%d)\n", num, a.displayName, a.getCurrentLevel().value);
            });

            final int backNum = i.getAndIncrement();
            System.out.printf("%d. Back\n", backNum);

            final int option = ioHelper.readInteger(
                    "Choose an option: ",
                    String.format("Please choose a value between 1 and %d.", i.get()),
                    x -> x >= 1 && x <= i.get()
            );

            // Back
            if (option == backNum) {
                return;
            }

            final Animal animal = myAnimals.get(option - 1);

            if (!animal.isUpgradable(this)) {
                System.out.printf("\"%s\" is not upgradable.", animal.displayName);
                continue;
            }

            System.out.printf("Upgrading \"%s\" will cost £%.2f\n", animal.displayName, animal.upgradeCost);
            final boolean upgrade = String.valueOf(ioHelper.readChar("Would you like to upgrade? (Y/N)", "Please choose Y for yes, or N for no.", IOHelper.YesNoCharValidator))
                    .equalsIgnoreCase("y");

            if (upgrade) {
                try {
                    animal.upgrade(this);
                    System.out.printf("\"%s\" has been upgraded to level %d!\n", animal.displayName, animal.getCurrentLevel().value);
                    System.out.printf("Your new balance is %s£%.2f%s.\n", Color.GREEN_BOLD_BRIGHT, this.currentBankBalance, Color.RESET);
                } catch (AnimalNotOwnedException | NoPermissionException | AnimalUpgradeNotAllowedException e) {
                    e.printStackTrace();
                } catch (InsufficientBalanceException e) {
                    System.out.printf("%sYou don't have enough money to upgrade this.%s\n", Color.RED_BOLD_BRIGHT, Color.RESET);
                    System.out.printf("Your balance is %s£%.2f%s.\n", Color.GREEN_BOLD_BRIGHT, this.currentBankBalance, Color.RESET);
                }
            }
        }
    }
}
