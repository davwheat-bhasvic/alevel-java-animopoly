package dev.davwheat;

import dev.davwheat.exceptions.InvalidActorException;
import org.jetbrains.annotations.Contract;

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
    public Card(String cardMessage, double balanceChange, boolean missNextTurn) {
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
    public void takeAction(Player actor) {
        if (actor == null) {
            throw new NullPointerException("actor cannot be null.");
        }

        actor.adjustBankBalance(balanceChange);

        if (missNextTurn) {
            actor.makeMissNextTurn();
        }
    }
}
