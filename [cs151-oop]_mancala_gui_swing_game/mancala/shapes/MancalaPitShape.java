package mancala.shapes;

import mancala.model.Side;
import mancala.strategy.MancalaStyle;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Mancala pit in the View
 */
public class MancalaPitShape extends PitShape {

    /**
     * A list of positions to choose from for drawing the circles that represent the shapes
     * These are randomly shuffled during construction of the object in order to give a more
     * natural feel to the game board
     */
    private final List<Position> mancalaPositions = Arrays.asList(
            Position.of(10, 0), Position.of(20, 0), Position.of(30, 0), Position.of(40, 0),
            Position.of(0, 10), Position.of(10, 10), Position.of(20, 10), Position.of(30, 10),
            Position.of(40, 10), Position.of(0, 20), Position.of(10, 20), Position.of(20, 20),
            Position.of(30, 20), Position.of(40, 20), Position.of(0, 30), Position.of(10, 30),
            Position.of(20, 30), Position.of(30, 30), Position.of(40, 30), Position.of(0, 40),
            Position.of(10, 40), Position.of(20, 40), Position.of(30, 40), Position.of(40, 40),
            Position.of(0, 50), Position.of(10, 50), Position.of(20, 50), Position.of(30, 50),
            Position.of(40, 50), Position.of(0, 60), Position.of(10, 60), Position.of(20, 60),
            Position.of(30, 60), Position.of(40, 60), Position.of(0, 70), Position.of(10, 70),
            Position.of(20, 70), Position.of(30, 70), Position.of(40, 70), Position.of(0, 80),
            Position.of(10, 80), Position.of(20, 80), Position.of(30, 80), Position.of(40, 80),
            Position.of(0, 90), Position.of(10, 90), Position.of(20, 90), Position.of(30, 90),
            Position.of(40, 90), Position.of(0, 100), Position.of(10, 100), Position.of(20, 100),
            Position.of(30, 100), Position.of(40, 100), Position.of(0, 110), Position.of(10, 110),
            Position.of(20, 110), Position.of(30, 110), Position.of(40, 110), Position.of(0, 120)
    );

    public MancalaPitShape(MancalaStyle style, Side side) {
        super(style, 0, side, 0);
        Collections.shuffle(mancalaPositions);
    }

    @Override
    public void paintIcon(Component c, Graphics graphics, int x, int y) {
        Graphics2D g = (Graphics2D) graphics;
        //Draw the pit itself
        Ellipse2D.Double pit = new Ellipse2D.Double(25, 25, 150, 250);
        g.draw(pit);

        //Draw each stone individually
        for (int i = 0; i < stoneCount; i++) {
            if (i >= mancalaPositions.size()){
                break;
            }

            //Get the position of the stone
            Ellipse2D.Double stone = new Ellipse2D.Double(50 + mancalaPositions.get(i).x * 2, 25 + mancalaPositions.get(i).y * 2, 20, 20);
            //Draw the fill
            g.setPaint(getStoneColor(i));
            g.fill(stone);

            //Draw the border
            g.setPaint(new Color(0, 0, 0));
            g.draw(stone);
        }

        //If updated set the paint to red
        if(updated){
            g.setPaint(new Color(255, 0, 0));
        }
        //Draw the text underneath the pit
        g.drawString(side.name() + " ( " + stoneCount + " )", 85, 300);
        updated = false;
        g.setPaint(new Color(0, 0, 0));
    }

    @Override
    public int getIconWidth() {
        return 200;
    }

    @Override
    public int getIconHeight() {
        return 300;
    }
}
