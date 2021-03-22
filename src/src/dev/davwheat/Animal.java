package dev.davwheat;

import dev.davwheat.enums.AnimalLevel;
import dev.davwheat.enums.BoardSpaceType;
import dev.davwheat.exceptions.AnimalAlreadyOwnedException;
import dev.davwheat.exceptions.AnimalNotOwnedException;
import dev.davwheat.exceptions.AnimalUpgradeNotAllowedException;

import javax.naming.NoPermissionException;

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
     * Contains indexes 0 to 4 (inclusive) for Levels 0 to 4.
     */
    public final double[] stopCosts;

    /**
     * The Player who owns this Animal.
     */
    private Player ownedBy = null;

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
     * @param stopCosts   An array of 5 costs for stopping on the Animal (Level 0 - 4).
     * @param index       Where the Animal is on the GameBoard.
     * @param game        The instance of Game that this Animal belongs to.
     */
    public Animal(String name, double cost, double upgradeCost, double[] stopCosts, int index, Game game) {
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
        return ownedBy;
    }

    /**
     * Get the cost for the provided Player to stop on this Animal's BoardSpace.
     *
     * @param actor The player
     * @return The cost for this player to stop on this animal
     */
    public double getStopCost(Player actor) {
        // It's free to stop on your own property
        if (isOwnedBy(actor)) return 0;

        double cost = this.stopCosts[this.currentLevel.value];

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
    public void payForStop(Player actor) {
        if (this.isOwnedBy(actor)) {
            // Don't charge the owner of this Animal
            return;
        }

        // Charge the person stopping, and apply the
        // opposite action to the Animal owner.
        actor.adjustBankBalance(-getStopCost(actor), this.getOwner());
    }

    /**
     * Attempts to purchase the Animal and make it owned by the Player.
     *
     * @param actor Person buying the Animal
     * @throws AnimalAlreadyOwnedException Animal is already owned by another player.
     */
    public void purchase(Player actor) throws AnimalAlreadyOwnedException {
        if (this.getOwner() != null) {
            throw new AnimalAlreadyOwnedException("Cannot purchase an animal if it is already owned.");
        }

        // Charge the player
        actor.adjustBankBalance(-purchaseCost);

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
    public void upgrade(Player actor) throws AnimalNotOwnedException, NoPermissionException, AnimalUpgradeNotAllowedException {
        if (getOwner() == null) {
            throw new AnimalNotOwnedException("An animal cannot be upgraded if it is not owned. You should always check if an upgrade is possible using `isUpgradable(player)` before attempting an upgrade.");
        } else if (!isOwnedBy(actor)) {
            throw new NoPermissionException("Only the Animal owner has permission to upgrade the animal. You should always check if an upgrade is possible using `isUpgradable(player)` before attempting an upgrade.");
        } else if (currentLevel == AnimalLevel.LEVEL_FOUR) {
            throw new AnimalUpgradeNotAllowedException("Animal is already at the maximum level. You should always check if an upgrade is possible using `isUpgradable(player)` before attempting an upgrade.");
        }

        // If nothing has been thrown at this point, it should be safe to upgrade
        // ... I hope

        // Charge the actor the upgrade cost
        actor.adjustBankBalance(-upgradeCost);
        // Increase the level by 1
        currentLevel = AnimalLevel.fromNumberValue(currentLevel.value + 1);
    }

    /**
     * Determines whether the Animal is upgradable by the specified Player.
     *
     * @param actor Player attempting the upgrade
     * @return Whether the Player can upgrade the Animal
     */
    public boolean isUpgradable(Player actor) {
        return getOwner() != null && isOwnedBy(actor) && currentLevel != AnimalLevel.LEVEL_FOUR;
    }

    /**
     * Tests if the provided Player owns this animal.
     *
     * @param actor The player
     * @return If the player owns the animal
     */
    private boolean isOwnedBy(Player actor) {
        return (getOwner() == actor);
    }
}
