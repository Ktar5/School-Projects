//4.15

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ZoomTester {

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        CarShape shape = new CarShape(100, 100, 20, 20);

        JLabel carLabel = new JLabel(shape);
        carLabel.setBorder(new EmptyBorder(0, 20, 0, 20));

        JButton zoomIn = new JButton("Zoom In");
        zoomIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shape.setWidth(shape.getIconWidth() + 10);
                carLabel.repaint();
            }
        });

        JButton zoomOut = new JButton("Zoom Out");
        zoomOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (shape.getIconWidth() <= 10) {
                    return;
                }
                shape.setWidth(shape.getIconWidth() - 10);
                carLabel.repaint();
            }
        });


        frame.setLayout(new FlowLayout());

        frame.add(carLabel);
        frame.add(zoomIn);
        frame.add(zoomOut);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static class CarShape implements Icon {
        private int width, height;
        private int x, y;

        public CarShape(int width, int height, int x, int y) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g;
            draw(g2);
        }

        @Override
        public int getIconWidth() {
            return width;
        }

        @Override
        public int getIconHeight() {
            return height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        //This shape is taken from the textbook
        public void draw(Graphics2D g2) {
            Rectangle2D.Double body = new Rectangle2D.Double(x, y + width / 6, width - 1, width / 6);
            Ellipse2D.Double frontTire = new Ellipse2D.Double(x + width / 6, y + width / 3, width / 6, width / 6);
            Ellipse2D.Double rearTire = new Ellipse2D.Double(x + width * 2 / 3, y + width / 3, width / 6, width / 6);

            // The bottom of the front windshield
            Point2D.Double r1 = new Point2D.Double(x + width / 6, y + width / 6);
            // The front of the roof
            Point2D.Double r2 = new Point2D.Double(x + width / 3, y);
            // The rear of the roof
            Point2D.Double r3 = new Point2D.Double(x + width * 2 / 3, y);
            // The bottom of the rear windshield
            Point2D.Double r4 = new Point2D.Double(x + width * 5 / 6, y + width / 6);
            Line2D.Double frontWindshield = new Line2D.Double(r1, r2);
            Line2D.Double roofTop = new Line2D.Double(r2, r3);
            Line2D.Double rearWindshield = new Line2D.Double(r3, r4);

            g2.draw(body);
            g2.draw(frontTire);
            g2.draw(rearTire);
            g2.draw(frontWindshield);
            g2.draw(roofTop);
            g2.draw(rearWindshield);
        }


    }


}
