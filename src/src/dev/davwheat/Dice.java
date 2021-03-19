package dev.davwheat;

import java.util.Random;

public class Dice {
    private int d1 = 0;
    private int d2 = 0;

    /**
     * Gets a random number between 1 and 6.
     *
     * @return int (1-6)
     */
    public static int rollDice() {
        Random random = new Random();
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
        /**
         * We need to roll two dice rather than get
         * a random value between 2-12 due to the
         * different distribution of values.
         */
        int d1 = rollDice(), d2 = rollDice();

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
        if (d1 == 0 || d2 == 0) {
            throw new IllegalStateException("Cannot check if a roll is a double before both dice have been rolled.");
        }

        return d1 == d2;
    }

    /**
     * Get the total value of both dice rolls.
     *
     * @return total roll value
     */
    public int getTotalRoll() {
        if (d1 == 0 || d2 == 0) {
            throw new IllegalStateException("Cannot get the total roll value before both dice have been rolled.");
        }

        return d1 + d2;
    }

    /**
     * Gets the value of one specific dice roll.
     *
     * @param diceNumber Number dice to fetch value from (1 or 2)
     * @return The roll value
     */
    public int getOneRoll(int diceNumber) {
        if (diceNumber != 1 && diceNumber != 2) {
            throw new IllegalArgumentException("Dice number must be 0 or 1.");
        }

        switch (diceNumber) {
            default:
            case 1:
                return d1;

            case 2:
                return d2;
        }
    }
}
