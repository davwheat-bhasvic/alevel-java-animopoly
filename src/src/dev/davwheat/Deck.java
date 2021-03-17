package dev.davwheat;

import dev.davwheat.exceptions.DeckIsLockedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Deck {
    /**
     * If the Deck is locked, no new cards can be added.
     */
    private boolean isLocked = false;

    /**
     * ArrayList of all cards in the Deck.
     */
    private ArrayList<Card> allCards = new ArrayList<>();

    public Deck() {

    }

    /**
     * Add a card to the Deck. Will throw an exception if the Deck is locked.
     * <p>
     * Returns the deck so that it can be chained:
     * `.addCard(...).addCard(...)`
     *
     * @param card Card to be added
     * @return the Deck after the card has been added
     * @throws DeckIsLockedException The Deck has been locked. No more modifications can be made.
     */
    public Deck addCard(Card card) throws DeckIsLockedException {
        if (this.isLocked) {
            throw new DeckIsLockedException("Deck is locked. No modifications are allowed.");
        }

        this.allCards.add(card);
        return this;
    }

    /**
     * Locks the Deck to prevent modifications.
     * <p>
     * This is a one-way process and cannot be reversed.
     *
     * @return the Deck
     */
    public Deck lock() {
        this.isLocked = true;
        return this;
    }

    /**
     * Shuffle all the cards.
     *
     * @return the Deck
     */
    public Deck shuffleCards() {
        Collections.shuffle(allCards);
        return this;
    }

    /**
     * Take the top card, return it, and move it to the bottom of the pile.
     *
     * @return a Card
     */
    public Card takeCard() {
        // Get the top card
        Card card = allCards.get(0);
        // Remove the top card
        allCards.remove(0);
        // Push the card to the bottom of the pile
        allCards.add(card);

        return card;
    }

    /**
     * Creates a Deck with the standard set of cards.
     *
     * @return the Deck
     */
    public static Deck createBaseDeck() {
        Deck deck = new Deck();

        try {
            deck.addCard(new Card("Your animal won second prize in a beauty contest. Collect £50.", 50, false))
                    .addCard(new Card("It's your animal's birthday! Collect £200.", 200, false))
                    .addCard(new Card("Your animal gave birth and you sold their babies! Collect £300.", 300, false))
                    .addCard(new Card("You crashed your tractor. Pay £400 in insurance premiums.", -400, false))
                    .addCard(new Card("You got a raise! Collect £320.", 320, false))
                    .addCard(new Card("You forgot to pay your taxes. Pay £200 in fines and miss your next turn.", -200, true))
                    .addCard(new Card("Your animals have contracted disease and need medication. Pay £100.", -100, false))
                    .addCard(new Card("Your animals won Best in Show. Collect £100.", 100, false))
                    .addCard(new Card("You won the the local lottery. Collect £500.", 500, false))
                    .addCard(new Card("Your stocks diminished in value. Lose £500.", -500, false))
                    .addCard(new Card("One of your animals has died. Miss your next turn.", 0, true))
                    .lock()
                    .shuffleCards();
        } catch (DeckIsLockedException e) {
            // This should never happen, but we need to handle it so Java doesn't have a fit.
            throw new RuntimeException("Deck was somehow locked! Argh!");
        }

        return deck;
    }
}
