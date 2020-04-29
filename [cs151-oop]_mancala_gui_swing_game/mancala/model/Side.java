package mancala.model;

/**
 * Represents the two players in the game
 */
public enum Side {
    A,
    B;

    /**
     * Simply returns the other side.
     * @return B if side is A or returns A if side is B
     */
    public Side opposite() {
        if (this.equals(Side.A)) {
            return Side.B;
        } else {
            return Side.A;
        }
    }
}
