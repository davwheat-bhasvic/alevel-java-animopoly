package dev.davwheat;

import dev.davwheat.enums.BoardSpaceType;

/**
 * A space on the GameBoard which a Player can land on and interact with.
 */
public class BoardSpace {
    /**
     * The position of this BoardSpace on the GameBoard.
     */
    public final int index;

    /**
     * The type of BoardSpace.
     */
    public final BoardSpaceType type;

    /**
     * The name displayed to Players for this Board Space.
     */
    public final String displayName;

    /**
     * Whether this BoardSpace can be owned by a Player.
     */
    public final boolean isOwnable;

    /**
     * The Game that this BoardSpace is a part of.
     */
    private final Game gameInstance;

    /**
     * Create a new instance of BoardSpace.
     *
     * @param displayName The name of the BoardSpace shown to the players.
     * @param index       The position of this BoardSpace on the GameBoard.
     * @param type        The type of this BoardSpace.
     * @param isOwnable   Whether this BoardSpace can be owned by a Player.
     * @param game        The Game that this BoardSpace belongs to.
     */
    public BoardSpace(final String displayName, final int index, final BoardSpaceType type, final boolean isOwnable, final Game game) {
        this.index = index;
        this.displayName = displayName;
        this.type = type;
        this.isOwnable = isOwnable;
        this.gameInstance = game;
    }
}
