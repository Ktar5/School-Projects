package mancala.shapes;

import mancala.model.Side;
import mancala.strategy.MancalaStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Pit holding stones on the game board in
 * relation to the View portion of the program
 */
public class PitShape implements Icon {
    /**
     * The number of stones currently in this pit
     */
    int stoneCount;

    /**
     * The style for determining colors to draw
     */
    MancalaStyle style;

    /**
     * The player side that this pit belongs to
     */
    Side side;

    /**
     * The pit number of this pit, for example A1-A6 pits
     */
    int pitNumber;

    /**
     * True if this pit was updated in the last turn, will make the text
     * turn red when true
     */
    boolean updated;

    /**
     * A list of positions to choose from for drawing the circles that represent the shapes
     * These are randomly shuffled during construction of the object in order to give a more
     * natural feel to the game board
     */
    private final List<Position> positions = Arrays.asList(
            Position.of(5, 15), Position.of(15, 15),
            Position.of(5, 25), Position.of(15, 25), Position.of(25, 15),
            Position.of(25, 5), Position.of(15, 5),
            Position.of(35, 15), Position.of(25, 25),
            Position.of(35, 25), Position.of(15, 35), Position.of(25, 35)
    );

    /**
     * Represents colors chosen for the stones, so we don't have sudden color
     * changes throughout the game on every turn
     */
    private final List<Color> colors = new ArrayList<>();

    public PitShape(MancalaStyle style, int amount, Side side, int pitNumber) {
        this.stoneCount = amount;
        this.style = style;
        this.side = side;
        this.pitNumber = pitNumber;
        Collections.shuffle(positions);
    }

    /**
     * Set the amount of stones in this pit
     */
    public void setStoneCount(int amount) {
        if (amount == stoneCount) {
            return;
        }
        this.stoneCount = amount;
        this.updated = true;
    }

    /**
     * Because each stone pit is redrawn on the end of each turn, by default, the colors of the stones would
     * be randomly shuffled and it would be very visually busy. In order to fix that we store the colors
     * that we generate in the constructor, and then access them so each stone remains the same colors
     * through turns
     * @param stoneNumber the index of the stone
     * @return a color within the defined style
     */
    public Color getStoneColor(int stoneNumber) {
        if (stoneNumber >= colors.size()) {
            colors.add(style.pickRandomColor());
        }
        return colors.get(stoneNumber);
    }

    @Override
    public void paintIcon(Component c, Graphics graphics, int x, int y) {
        Graphics2D g = (Graphics2D) graphics;
        //Draw the pit itself
        Ellipse2D.Double pit = new Ellipse2D.Double(25, 25, 100, 100);
        g.draw(pit);

        //Draw each stone individually
        for (int i = 0; i < stoneCount; i++) {
            if (i >= positions.size()) {
                break;
            }

            //Get the position of the stone
            Ellipse2D.Double stone = new Ellipse2D.Double(25 + positions.get(i).x * 2, 25 + positions.get(i).y * 2, 20, 20);
            //Draw the fill
            g.setPaint(getStoneColor(i));
            g.fill(stone);

            //Draw the border
            g.setPaint(new Color(0, 0, 0));
            g.draw(stone);
        }

        //If updated set the paint to red
        if (updated) {
            g.setPaint(new Color(255, 0, 0));
        } else {
            g.setPaint(new Color(0, 0, 0));
        }
        //Draw the text underneath the pit
        g.drawString(side.name() + pitNumber + " ( " + stoneCount + " )", 60, 150);
        updated = false;
        g.setPaint(new Color(0, 0, 0));
    }

    @Override
    public int getIconWidth() {
        return 150;
    }

    @Override
    public int getIconHeight() {
        return 150;
    }
}
