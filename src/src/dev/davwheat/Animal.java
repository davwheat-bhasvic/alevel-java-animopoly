package dev.davwheat;

import dev.davwheat.enums.AnimalLevel;
import dev.davwheat.enums.BoardSpaceType;
import dev.davwheat.enums.Color;
import dev.davwheat.exceptions.AnimalAlreadyOwnedException;
import dev.davwheat.exceptions.AnimalNotOwnedException;
import dev.davwheat.exceptions.AnimalUpgradeNotAllowedException;

import javax.naming.NoPermissionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class that represents an Animal, which is a space on the GameBoard.
 */
public class Animal extends BoardSpace {
    /**
     * The cost of the Animal to buy.
     */
    public final double purchaseCost;

    /**
     * The cost to upgrade the Animal to the next level.
     */
    public final double upgradeCost;

    /**
     * The cost to stop on this Animal for each level.
     * <p>
     * Contains indexes 0 to 3 (inclusive) for Levels 0 to 3.
     */
    public final double[] stopCosts;

    /**
     * The Player who owns this Animal.
     */
    private Player ownedBy;

    /**
     * The current level for this Animal.
     */
    private AnimalLevel currentLevel = AnimalLevel.LEVEL_ZERO;

    /**
     * Create a new instance of the Animal class.
     *
     * @param name        Name of the Animal shown to Players.
     * @param cost        The cost to buy the Animal.
     * @param upgradeCost The cost to upgrade the Animal.
     * @param stopCosts   An array of 4 costs for stopping on the Animal (Level 0 - 3).
     * @param index       Where the Animal is on the GameBoard.
     * @param game        The instance of Game that this Animal belongs to.
     */
    public Animal(final String name, final double cost, final double upgradeCost, final double[] stopCosts, final int index, final Game game) {
        // Call the parent class constructor
        super(name, index, BoardSpaceType.ANIMAL, true, game);

        this.purchaseCost = cost;
        this.upgradeCost = upgradeCost;
        this.stopCosts = stopCosts;
    }

    /**
     * Gets the owner of this animal
     *
     * @return Animal's owner
     */
    public Player getOwner() {
        return this.ownedBy;
    }

    /**
     * Get the cost for the provided Player to stop on this Animal's BoardSpace.
     *
     * @param actor The player
     * @return The cost for this player to stop on this animal
     */
    public double getStopCost(final Player actor) {
        // It's free to stop on your own property
        if (this.isOwnedBy(actor)) return 0;

        final double cost = this.stopCosts[this.currentLevel.value];

        if (cost == 0) {
            throw new IllegalStateException("stopCost at currentLevel is 0");
        }

        return cost;
    }

    /**
     * Charge to provided Player for stopping on this Animal.
     *
     * @param actor Player
     */
    public void payForStop(final Player actor) {
        if (this.isOwnedBy(actor)) {
            // Don't charge the owner of this Animal
            return;
        }

        // Charge the person stopping, and apply the
        // opposite action to the Animal owner.
        actor.adjustBankBalance(-this.getStopCost(actor), this.getOwner());
    }

    /**
     * Attempts to purchase the Animal and make it owned by the Player.
     * <p>
     * Also handles the charging of the purchase cost.
     *
     * @param actor Person buying the Animal
     * @throws AnimalAlreadyOwnedException Animal is already owned by another player.
     */
    public void purchase(final Player actor) throws AnimalAlreadyOwnedException {
        if (this.getOwner() != null) {
            throw new AnimalAlreadyOwnedException("Cannot purchase an animal if it is already owned.");
        }

        // Charge the player
        actor.adjustBankBalance(-this.purchaseCost);

        // Set the owner
        this.ownedBy = actor;
    }

    /**
     * Attempts to upgrade an Animal to the next level.
     * <p>
     * You should always try to check if the Animal <i>can</i> be upgraded first
     * using `Animal.isUpgradable(player)`.
     *
     * @param actor The Player attempting to upgrade the Animal.
     * @throws AnimalNotOwnedException          Thrown when the Animal isn't owned.
     * @throws NoPermissionException            Thrown when a Player other than the Animal owner attempts to upgrade the Animal.
     * @throws AnimalUpgradeNotAllowedException Thrown when an Animal is already at the maximum level.
     */
    public void upgrade(final Player actor) throws AnimalNotOwnedException, NoPermissionException, AnimalUpgradeNotAllowedException {
        if (this.getOwner() == null) {
            throw new AnimalNotOwnedException("An animal cannot be upgraded if it is not owned. You should always check if an upgrade is possible using `isUpgradable(player)` before attempting an upgrade.");
        } else if (!this.isOwnedBy(actor)) {
            throw new NoPermissionException("Only the Animal owner has permission to upgrade the animal. You should always check if an upgrade is possible using `isUpgradable(player)` before attempting an upgrade.");
        } else if (this.currentLevel == AnimalLevel.LEVEL_THREE) {
            throw new AnimalUpgradeNotAllowedException("Animal is already at the maximum level. You should always check if an upgrade is possible using `isUpgradable(player)` before attempting an upgrade.");
        }

        // If nothing has been thrown at this point, it should be safe to upgrade
        // ... I hope

        // Charge the actor the upgrade cost
        actor.adjustBankBalance(-this.upgradeCost);
        // Increase the level by 1
        this.currentLevel = AnimalLevel.fromNumberValue(this.currentLevel.value + 1);
    }

    /**
     * Determines whether the Animal is upgradable by the specified Player.
     *
     * @param actor Player attempting the upgrade
     * @return Whether the Player can upgrade the Animal
     */
    public boolean isUpgradable(final Player actor) {
        return this.getOwner() != null && this.isOwnedBy(actor) && this.currentLevel != AnimalLevel.LEVEL_THREE;
    }

    /**
     * Tests if the provided Player owns this animal.
     *
     * @param actor The player
     * @return If the player owns the animal
     */
    private boolean isOwnedBy(final Player actor) {
        return (this.getOwner() == actor);
    }

    /**
     * Prints a visual representation of the card to stdout.
     */
    public void printCard() {
        final int cardInnerWidth = 24;
        final String cardInnerFrame = "━".repeat(cardInnerWidth);

        final ArrayList<String> displayNameLines = StringTools.centreText(StringTools.splitStringAtWhitespace(this.displayName, cardInnerWidth), cardInnerWidth);

        // Frame characters from here:
        // https://en.wikipedia.org/wiki/Box-drawing_character

        System.out.printf("┏%s┓\n", cardInnerFrame);
        displayNameLines.forEach(s -> System.out.printf("┃%s┃\n", s));
        System.out.printf("┣%s┫\n", cardInnerFrame);
        System.out.printf("┃%s%s%s┃\n", Color.WHITE_BOLD_BRIGHT, StringTools.centreText("Stop costs", cardInnerWidth), Color.RESET);

        // Prints stop costs
        final AtomicInteger level = new AtomicInteger();
        Arrays.stream(this.stopCosts).forEach(cost -> {
            // Whether this line of text represents the current level of the Animal.
            final boolean isThisCurrentLevel = this.getOwner() != null && this.currentLevel.value == level.get();

            System.out.printf("┃%s%s%s┃\n", isThisCurrentLevel ? Color.WHITE_BOLD_BRIGHT : "", StringTools.centreText(String.format("Level %d - £%.2f", level.get(), cost), cardInnerWidth), Color.RESET);
            level.getAndIncrement();
        });

        System.out.printf("┣%s┫\n", cardInnerFrame);

        if (this.getOwner() == null) {
            System.out.printf("┃%s┃\n", StringTools.centreText(String.format("Purchase for £%.2f", this.purchaseCost), cardInnerWidth));
        } else {
            System.out.printf("┃%s┃\n", StringTools.centreText(String.format("Owned by %s", this.getOwner().playerName), cardInnerWidth));
        }

        System.out.printf("┗%s┛\n", cardInnerFrame);
    }
}
