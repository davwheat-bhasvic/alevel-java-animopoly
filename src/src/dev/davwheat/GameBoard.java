package dev.davwheat;

import dev.davwheat.enums.BoardSpaceType;

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

                new Animal("Slug", 145d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 1, this.gameInstance),
                new Animal("Snail", 125d, 100d, new double[]{4d, 8d, 12d, 20d, 35d}, 2, this.gameInstance),

                new Animal("Leopard", 335d, 100d, new double[]{14d, 20d, 26d, 32d, 38d}, 3, this.gameInstance),
                new Animal("Cheetah", 350d, 100d, new double[]{18d, 24d, 30d, 36d, 42d}, 4, this.gameInstance),
                new Animal("Cougar", 335d, 100d, new double[]{14d, 20d, 26d, 32d, 38d}, 5, this.gameInstance),

                new Animal("Gorilla", 275d, 100d, new double[]{14d, 22d, 30d, 45d, 65d}, 6, this.gameInstance),
                new Animal("Monkey", 240d, 100d, new double[]{12d, 10d, 15d, 25d, 40d}, 7, this.gameInstance),

                new Animal("Crocodile", 380d, 100d, new double[]{18d, 24d, 30d, 36d, 42d}, 8, this.gameInstance),
                new Animal("Alligator", 370d, 100d, new double[]{18d, 24d, 30d, 36d, 42d}, 9, this.gameInstance),

                new Animal("Haddock", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 10, this.gameInstance),
                new Animal("Cod", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 11, this.gameInstance),
                new Animal("Salmon", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 12, this.gameInstance),

                new BoardSpace("Miss next turn", 13, BoardSpaceType.MISS_NEXT_TURN, false, this.gameInstance),

                new Animal("Dog", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 14, this.gameInstance),
                new Animal("Cat", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 15, this.gameInstance),

                new Animal("Pheasant", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 16, this.gameInstance),
                new Animal("Peacock", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 17, this.gameInstance),
                new Animal("Eagle", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 18, this.gameInstance),

                new Animal("Ant", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 19, this.gameInstance),
                new Animal("Beetle", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 20, this.gameInstance),

                new Animal("Sheep", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 21, this.gameInstance),
                new Animal("Cow", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 22, this.gameInstance),
                new Animal("Pig", 250d, 100d, new double[]{5d, 10d, 15d, 25d, 40d}, 23, this.gameInstance),

                new Animal("Tiger", 525d, 200d, new double[]{40d, 85d, 125d, 200d, 300d}, 24, this.gameInstance),
                new Animal("Lion", 600d, 200d, new double[]{50d, 100d, 150d, 250d, 400d}, 25, this.gameInstance),
        };
    }
}
