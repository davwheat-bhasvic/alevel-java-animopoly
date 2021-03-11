package dev.davwheat;

import java.util.ArrayList;

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
    public GameBoard(Game gameInstance) {
        this.gameInstance = gameInstance;
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

    public BoardSpace getBoardSpaceAtPosition(int index) {
        if (index < 0 || index >= totalSpaces) {
            throw new IllegalArgumentException("Index provided is outside allowed range (0 to " + (this.totalSpaces - 1) + ").");
        }

        BoardSpace bs = allBoardSpaces[index];

        if (bs == null) {
            throw new NullPointerException("BoardSpace at index " + index + " is null.");
        }

        return bs;
    }

    private void propagateSpacesList() {
        // TODO: Propagate spaces list
    }
}
