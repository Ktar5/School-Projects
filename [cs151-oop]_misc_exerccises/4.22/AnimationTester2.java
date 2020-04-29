//4.20

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class AnimationTester2 {

    public static final int FRAME_WIDTH = 250;

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        MoveableShape shape = new CarShape(0, 0, 50, FRAME_WIDTH);
        MoveableShape shape2 = new CarShape(25, 50, 50, FRAME_WIDTH);
        MoveableShape shape3 = new CarShape(50, 25, 50, FRAME_WIDTH);

        ShapeIcon icon = new ShapeIcon(FRAME_WIDTH, FRAME_WIDTH, shape, shape2, shape3);

        JLabel label = new JLabel(icon);
        frame.setLayout(new FlowLayout());
        frame.add(label);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        final int DELAY = 100;
        // Milliseconds between timer ticks
        Timer t = new Timer(DELAY, event -> {
            shape.move();
            shape2.move();
            shape3.move();
            label.repaint();
        });
        t.start();
    }

    public static class ShapeIcon implements Icon {
        private MoveableShape[] shapes;
        private int width, height;

        public ShapeIcon(int width, int height, MoveableShape... shapes) {
            this.shapes = shapes;
            this.width = width;
            this.height = height;
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g;
            for (MoveableShape shape : shapes) {
                shape.draw(g2);
            }
        }
    }

    public interface MoveableShape {
        /**
         * Draws the shape.
         *
         * @param g2 the graphics context
         */
        void draw(Graphics2D g2);

        /**
         * Moves the shape.
         * It is up to the shape to move itself, for example by tracking the time since
         * its last movement, its position, and velocity.
         */
        void move();
    }

    public static class CarShape implements MoveableShape {
        private int frameWidth;
        private int x;
        private int y;
        private int width;

        public CarShape(int x, int y, int width, int frameWidth) {
            this.x = x;
            this.y = y;
            this.frameWidth = frameWidth;
            this.width = width;
        }

        public void move() {
            x = (x + 5) % frameWidth;
        }

        public void draw(Graphics2D g2) {
            Rectangle2D.Double body
                    = new Rectangle2D.Double(x, y + width / 6,
                    width - 1, width / 6);
            Ellipse2D.Double frontTire
                    = new Ellipse2D.Double(x + width / 6, y + width / 3,
                    width / 6, width / 6);
            Ellipse2D.Double rearTire
                    = new Ellipse2D.Double(x + width * 2 / 3, y + width / 3,
                    width / 6, width / 6);

            // The bottom of the front windshield
            Point2D.Double r1
                    = new Point2D.Double(x + width / 6, y + width / 6);
            // The front of the roof
            Point2D.Double r2
                    = new Point2D.Double(x + width / 3, y);
            // The rear of the roof
            Point2D.Double r3
                    = new Point2D.Double(x + width * 2 / 3, y);
            // The bottom of the rear windshield
            Point2D.Double r4
                    = new Point2D.Double(x + width * 5 / 6, y + width / 6);
            Line2D.Double frontWindshield
                    = new Line2D.Double(r1, r2);
            Line2D.Double roofTop
                    = new Line2D.Double(r2, r3);
            Line2D.Double rearWindshield
                    = new Line2D.Double(r3, r4);

            g2.draw(body);
            g2.draw(frontTire);
            g2.draw(rearTire);
            g2.draw(frontWindshield);
            g2.draw(roofTop);
            g2.draw(rearWindshield);
        }

    }

}
