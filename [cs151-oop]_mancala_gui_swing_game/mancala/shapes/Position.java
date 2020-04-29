package mancala.shapes;

/**
 * A simple class for defining an x and y immutable position.
 * Everything in here should be self explanatory.
 */
public class Position {
    public final int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position of(int x, int y){
        return new Position(x, y);
    }
}
