package dev.davwheat.enums;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum AnimalLevel {
    LEVEL_ZERO(0),
    LEVEL_ONE(1),
    LEVEL_TWO(2),
    LEVEL_THREE(3),
    LEVEL_FOUR(4),
    ;

    public final int value;

    /**
     * Create a new enum entry.
     *
     * @param i Level value
     */
    AnimalLevel(int i) {
        this.value = i;
    }

    /**
     * Fetches an AnimalLevel from a level number (0-4).
     * <p>
     * Calling this with a number outside 0-4 <b>will</b> throw an
     * IllegalArgumentException.
     * <p>
     * We should try our best to NOT use this where we can, as the
     * enum values themselves should be good enough for most cases.
     *
     * @param value Level integer (0-4)
     * @return
     */
    @Contract(pure = true)
    public static @NotNull
    AnimalLevel fromNumberValue(int value) {
        switch (value) {
            case 0:
                return AnimalLevel.LEVEL_ZERO;
            case 1:
                return AnimalLevel.LEVEL_ONE;
            case 2:
                return AnimalLevel.LEVEL_TWO;
            case 3:
                return AnimalLevel.LEVEL_THREE;
            case 4:
                return AnimalLevel.LEVEL_FOUR;
            default:
                throw new IllegalArgumentException("Levels only exist for values 0-4.");
        }
    }
}
