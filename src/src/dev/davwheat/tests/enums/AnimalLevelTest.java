package dev.davwheat.tests.enums;

import dev.davwheat.enums.AnimalLevel;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

// Make the tests run in order (for display purposes only).
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimalLevelTest {

    @Test
    @Order(0)
    @DisplayName("Correct Level for 0.")
    void providesCorrectLevelForZero() {
        assertEquals(
                AnimalLevel.LEVEL_ZERO,
                AnimalLevel.fromNumberValue(0)
        );
    }

    @Test
    @Order(1)
    @DisplayName("Correct Level for 1.")
    void providesCorrectLevelForOne() {
        assertEquals(
                AnimalLevel.LEVEL_ONE,
                AnimalLevel.fromNumberValue(1)
        );
    }

    @Test
    @Order(2)
    @DisplayName("Correct Level for 2.")
    void providesCorrectLevelForTwo() {
        assertEquals(
                AnimalLevel.LEVEL_TWO,
                AnimalLevel.fromNumberValue(2)
        );
    }

    @Test
    @Order(3)
    @DisplayName("Correct Level for 3.")
    void providesCorrectLevelForThree() {
        assertEquals(
                AnimalLevel.LEVEL_THREE,
                AnimalLevel.fromNumberValue(3)
        );
    }

    @Test
    @Order(4)
    @DisplayName("Correct Level for 4.")
    void providesCorrectLevelForFour() {
        assertEquals(
                AnimalLevel.LEVEL_FOUR,
                AnimalLevel.fromNumberValue(4)
        );
    }
}