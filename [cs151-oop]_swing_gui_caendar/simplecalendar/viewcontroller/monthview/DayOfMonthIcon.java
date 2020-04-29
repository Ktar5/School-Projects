package simplecalendar.viewcontroller.monthview;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DayOfMonthIcon implements Icon {
    private int day = -1;
    private boolean selected = false;

    /**
     * Set whether or not this icon is the selected day or not
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Set the day of month that this icon represents, -1 if blank
     */
    public void setDay(int day) {
        if (day < 1) {
            day = -1;
        }
        this.day = day;
    }

    @Override
    public void paintIcon(Component c, Graphics graphics, int x, int y) {
        Graphics2D g = (Graphics2D) graphics;

        if (selected && day != -1) {
            Rectangle2D.Double selectedHighlightBox = new Rectangle2D.Double(0, 1, 23, 23);
            g.draw(selectedHighlightBox);
        }
        if (day != -1) {
            g.drawString(String.valueOf(day), 5, 17);
        }
    }

    @Override
    public int getIconWidth() {
        return 25;
    }

    @Override
    public int getIconHeight() {
        return 25;
    }
}
