package dev.davwheat.tests;

import dev.davwheat.Dice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {
    static final int ITERATIONS = 25000;

    @Test
    @DisplayName("Ensure rollDice() is between 1 and 6.")
    void rollDice() {
        for (int i = 0; i < DiceTest.ITERATIONS; i++) {
            final int val = Dice.rollDice();
            assertTrue(val >= 1 && val <= 6);
        }
    }

    @Test
    @DisplayName("Ensure rollAllDice() is between 1 and 12.")
    void rollAllDice() {
        for (int i = 0; i < DiceTest.ITERATIONS; i++) {
            final Dice d = new Dice();
            final int val = d.rollAllDice().getTotalRoll();
            assertTrue(val >= 2 && val <= 12);
        }
    }

    @Test
    @DisplayName("Ensure getOneRoll() returns two values that sum to total.")
    void getOneRoll() {
        for (int i = 0; i < DiceTest.ITERATIONS; i++) {
            final Dice d = new Dice();
            final int val = d.rollAllDice().getTotalRoll();
            assertEquals(val, d.getOneRoll(1) + d.getOneRoll(2));
        }
    }

    @Test
    @DisplayName("Ensure getOneRoll() throws for invalid dice number.")
    void getOneRollInvalid() {
        final Dice d = new Dice();
        d.rollAllDice().getTotalRoll();

        assertThrows(IllegalArgumentException.class, () -> d.getOneRoll(-1));
        assertThrows(IllegalArgumentException.class, () -> d.getOneRoll(0));
        assertThrows(IllegalArgumentException.class, () -> d.getOneRoll(3));
    }

    @Test
    @DisplayName("Ensure isDouble() returns true if double rolled.")
    void isDouble() {
        for (int i = 0; i < DiceTest.ITERATIONS; i++) {
            final Dice d = new Dice();
            d.rollAllDice().getTotalRoll();

            // If double
            if (d.getOneRoll(1) == d.getOneRoll(2)) {
                assertTrue(d.isDouble());
            } else {
                assertFalse(d.isDouble());
            }
        }
    }
}