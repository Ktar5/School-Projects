package mancala.model;

/**
 * This class represents each pit and some various data about each pit
 */
public enum Pit {

    //I know the ordering of this is a little weird, and some of the methods in
    // this class are a little strange, but it was the easiest way for me to
    // implement it and I had to change some of the ordering to make sure it was
    // working properly, please do not change the ordering of these values or
    // else the program will break

    B6(Side.B, false),
    B5(Side.B, false),
    B4(Side.B, false),
    B3(Side.B, false),
    B2(Side.B, false),
    B1(Side.B, false),

    A_MANCALA(Side.A, true),


    A6(Side.A, false),
    A5(Side.A, false),
    A4(Side.A, false),
    A3(Side.A, false),
    A2(Side.A, false),
    A1(Side.A, false),

    B_MANCALA(Side.B, true);

    /**
     * The player side that this pit belongs to
     */
    public final Side side;

    /**
     * True if this pit represents one of the player mancala pits
     */
    public final boolean mancala;

    private static Pit[] OPPOSITE = {
            A1, A2, A3, A4, A5, A6, B_MANCALA, B1, B2, B3, B4, B5, B6, A_MANCALA
    };

    private static Pit[] NEXT = {
            B_MANCALA, B6, B5, B4, B3, B2, B1, A_MANCALA, A6, A5, A4, A3, A2, A1
    };

    private Pit(Side side, boolean mancala) {
        this.side = side;
        this.mancala = mancala;
    }

    /**
     * Get the pit opposite to this one
     */
    public Pit opposite() {
        return OPPOSITE[this.ordinal()];
    }

    /**
     * Get the next pit in the counterclockwise rotation from this one
     */
    public Pit next() {
        return NEXT[this.ordinal()];
    }
}