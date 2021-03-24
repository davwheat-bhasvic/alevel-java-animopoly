package dev.davwheat;

import java.util.Random;

/**
 * Handles the rolling of dice to decide player movement.
 */
public class Dice {
    private int d1;
    private int d2;

    /**
     * Gets a random number between 1 and 6.
     *
     * @return int (1-6)
     */
    public static int rollDice() {
        final Random random = new Random();
        return random.nextInt(6) + 1;
    }

    /**
     * Rolls two dice and updates the instance of this class accordingly.
     * <p>
     * To get the total roll, call:
     * rollAllDice().getTotalRoll()
     *
     * @return the current dice object
     */
    public Dice rollAllDice() {
        /*
         * We need to roll two dice rather than get
         * a random value between 2-12 due to the
         * different distribution of values.
         */
        final int d1 = Dice.rollDice();
        final int d2 = Dice.rollDice();

        this.d1 = d1;
        this.d2 = d2;

        return this;
    }

    /**
     * Is the roll a double?
     *
     * @return true for double
     */
    public boolean isDouble() {
        if (this.d1 == 0 || this.d2 == 0) {
            throw new IllegalStateException("Cannot check if a roll is a double before both dice have been rolled.");
        }

        return this.d1 == this.d2;
    }

    /**
     * Get the total value of both dice rolls.
     *
     * @return total roll value
     */
    public int getTotalRoll() {
        if (this.d1 == 0 || this.d2 == 0) {
            throw new IllegalStateException("Cannot get the total roll value before both dice have been rolled.");
        }

        return this.d1 + this.d2;
    }

    /**
     * Gets the value of one specific dice roll.
     *
     * @param diceNumber Number dice to fetch value from (1 or 2)
     * @return The roll value
     */
    public int getOneRoll(final int diceNumber) {
        return switch (diceNumber) {
            case 1 -> this.d1;
            case 2 -> this.d2;
            default -> throw new IllegalArgumentException("Dice number must be 1 or 2.");
        };
    }
}
