package mancala.strategy;

import java.awt.*;
import java.util.Random;

/**
 * Represents a style of colors used in the game
 * Represents the Strategy pattern
 */
public abstract class MancalaStyle {
    private Color[] colors;
    private Random random;

    /**
     * Create a style with defined colors
     * @param colors the colors of the style
     */
    public MancalaStyle(Color... colors){
        this.colors = colors;
        this.random = new Random();
    }

    /**
     * Pick a random color from the list of colors defined by this style
     * @return a random color from the list of colors defined by this style
     */
    public Color pickRandomColor(){
        return colors[random.nextInt(colors.length)];
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName().replace("Style", "");
    }
}
