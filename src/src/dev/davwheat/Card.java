package dev.davwheat;

import dev.davwheat.enums.Color;
import dev.davwheat.exceptions.InsufficientBalanceException;

import java.util.List;

/**
 * Represents a Card in the Deck. Contains info about the card (message,
 * balance change as a result), and methods to take the action.
 */
public class Card {
    public final String message;
    public final double balanceChange;
    public final boolean missNextTurn;

    /**
     * Creates a new Card.
     *
     * @param cardMessage   Message displayed on the card
     * @param balanceChange Balance change caused by the card
     * @param missNextTurn  Whether to miss next turn because of this card
     */
    public Card(final String cardMessage, final double balanceChange, final boolean missNextTurn) {
        this.message = cardMessage;
        this.balanceChange = balanceChange;
        this.missNextTurn = missNextTurn;
    }

    /**
     * Takes the action on the card against a specified player and
     * prints info about that action.
     * <p>
     * Will adjust their bank balance as required, and make them
     * miss the next turn if needed.
     *
     * @param actor Player to perform actions upon
     */
    public void takeAction(final Player actor) throws InsufficientBalanceException {
        if (actor == null) {
            throw new NullPointerException("actor cannot be null.");
        }

        actor.adjustBankBalance(this.balanceChange);

        if (this.missNextTurn) {
            actor.makeMissNextTurn();
            if (this.balanceChange != 0) {
                System.out.printf("You %s £%.2f and will miss your next turn.\n", this.balanceChange < 0 ? "lost" : "won", this.balanceChange);
            } else {
                System.out.print("You will miss your next turn.\n");
            }
        } else {
            System.out.printf("You %s £%.2f.\n", this.balanceChange < 0 ? "lost" : "won", this.balanceChange);
        }
    }

    public void printCard() {
        final int width = 35;
        final String innerBorder = "─".repeat(width);

        System.out.printf("╭%s╮\n", innerBorder);

        final List<String> lines = StringTools.splitStringAtWhitespace(this.message, width - 2);
        final List<String> centredLines = StringTools.centreText(lines, width);

        System.out.printf("│%s%s%s│\n", Color.WHITE_BOLD_BRIGHT, StringTools.centreText("Chance", width), Color.RESET);
        System.out.printf("│%s│\n", " ".repeat(width));

        centredLines.forEach(s -> System.out.printf("│%s│\n", s));

        System.out.printf("╰%s╯\n", innerBorder);
    }
}
