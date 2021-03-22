package dev.davwheat;

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
     * Takes the action on the card against a specified player.
     * <p>
     * Will adjust their bank balance as required, and make them
     * miss the next turn if needed.
     *
     * @param actor Player to perform actions upon
     */
    public void takeAction(final Player actor) {
        if (actor == null) {
            throw new NullPointerException("actor cannot be null.");
        }

        actor.adjustBankBalance(this.balanceChange);

        if (this.missNextTurn) {
            actor.makeMissNextTurn();
        }
    }
}