package dev.davwheat;

import dev.davwheat.enums.BoardSpaceType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that handles storing and managing the data relating to the board,
 * including the spaces on the board.
 */
public class GameBoard {
    /**
     * Total spaces on the board.
     * <p>
     * Fixed value used within this class only.
     */
    private final int totalSpaces = 26;

    /**
     * All spaces on the board.
     * <p>
     * Indexes 0 to 25 inclusive are supported.
     */
    private BoardSpace[] allBoardSpaces;

    /**
     * Instance of Game which this board is related to.
     */
    private final Game gameInstance;

    /**
     * Create a new instance of GameBoard.
     *
     * @param gameInstance The instance of Game to which this board is related.
     */
    public GameBoard(final Game gameInstance) {
        this.gameInstance = gameInstance;

        this.createBoard();
    }

    /**
     * Print the current game board to the console.
     */
    public void printCurrentBoard() {
        // TODO: Print current board method
        System.out.println("*****  TO DO  *****");
        System.out.println("*   PRINT BOARD   *");
        System.out.println("*****  TO DO  *****");
    }

    public BoardSpace getBoardSpaceAtPosition(final int index) {
        if (index < 0 || index >= this.totalSpaces) {
            throw new IllegalArgumentException("Index provided is outside allowed range (0 to " + (this.totalSpaces - 1) + ").");
        }

        final BoardSpace bs = this.allBoardSpaces[index];

        if (bs == null) {
            throw new NullPointerException("BoardSpace at index " + index + " is null.");
        }

        return bs;
    }

    /**
     * Creates all the cards for the board.
     * <p>
     * Should only be called from the constructor.
     */
    private void createBoard() {
        if (this.gameInstance == null) {
            throw new NullPointerException("Game instance is null, and not a valid instance of the Game class.");
        }

        // We add `d` to the end of the number to force Java to interpret it as a double, not an int.
        this.allBoardSpaces = new BoardSpace[]{
                new BoardSpace("Start", 0, BoardSpaceType.START, false, this.gameInstance),

                new Animal("Slug", 145d, 100d, new double[]{5d, 10d, 15d, 25d}, 1, this.gameInstance),
                new Animal("Snail", 125d, 100d, new double[]{4d, 8d, 12d, 20d}, 2, this.gameInstance),

                new Animal("Leopard", 335d, 100d, new double[]{14d, 20d, 26d, 32d}, 3, this.gameInstance),
                new Animal("Cheetah", 350d, 100d, new double[]{18d, 24d, 30d, 36d}, 4, this.gameInstance),
                new Animal("Cougar", 335d, 100d, new double[]{14d, 20d, 26d, 32d}, 5, this.gameInstance),

                new Animal("Gorilla", 275d, 100d, new double[]{14d, 22d, 30d, 45d}, 6, this.gameInstance),
                new Animal("Monkey", 240d, 100d, new double[]{12d, 20d, 28d, 42d}, 7, this.gameInstance),

                new Animal("Crocodile", 380d, 100d, new double[]{18d, 32d, 42d, 50d}, 8, this.gameInstance),
                new Animal("Alligator", 370d, 100d, new double[]{18d, 24d, 30d, 48d}, 9, this.gameInstance),

                new Animal("Haddock", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 10, this.gameInstance),
                new Animal("Cod", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 11, this.gameInstance),
                new Animal("Salmon", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 12, this.gameInstance),

                new BoardSpace("Miss next turn", 13, BoardSpaceType.MISS_NEXT_TURN, false, this.gameInstance),

                new Animal("Dog", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 14, this.gameInstance),
                new Animal("Cat", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 15, this.gameInstance),

                new Animal("Pheasant", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 16, this.gameInstance),
                new Animal("Peacock", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 17, this.gameInstance),
                new Animal("Eagle", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 18, this.gameInstance),

                new Animal("Ant", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 19, this.gameInstance),
                new Animal("Beetle", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 20, this.gameInstance),

                new Animal("Sheep", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 21, this.gameInstance),
                new Animal("Cow", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 22, this.gameInstance),
                new Animal("Pig", 250d, 100d, new double[]{5d, 10d, 15d, 25d}, 23, this.gameInstance),

                new Animal("Tiger", 525d, 200d, new double[]{40d, 85d, 125d, 220d}, 24, this.gameInstance),
                new Animal("Lion", 600d, 200d, new double[]{50d, 100d, 150d, 275d}, 25, this.gameInstance),
        };
    }

    /**
     * Fetches all the Animals owned by the provided actor.
     *
     * @param actor Player
     * @return List of Animals owned by the player
     */
    public List<Animal> getOwnedAnimals(final Player actor) {
        final ArrayList<Animal> animals = new ArrayList<>();

        Arrays.stream(this.allBoardSpaces).forEachOrdered(bs -> {
            if (bs instanceof Animal) {
                final Player owner = ((Animal) bs).getOwner();

                if (owner != null && owner.equals(actor)) {
                    animals.add((Animal) bs);
                }
            }
        });

        return animals;
    }
}
