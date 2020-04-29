//4.18

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.time.LocalTime;

public class ClockTester {


    public static void main(String[] args) {
        JFrame frame = new JFrame();

        ClockIcon clockIcon = new ClockIcon(250);
        JLabel clockLabel = new JLabel(clockIcon);
        clockLabel.setBorder(new EmptyBorder(0, 20, 0, 20));


        frame.setLayout(new FlowLayout());

        frame.add(clockLabel);

        Timer timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clockLabel.repaint();
            }
        });
        timer.start();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static class ClockIcon implements Icon {
        private int radius;

        public ClockIcon(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D)g;

            LocalTime now = LocalTime.now();
            Line2D hour = new Line2D.Double(radius, radius, radius, radius + (radius * .4));
            Line2D minute = new Line2D.Double(radius, radius, radius, radius + (radius * .7));
            Line2D second = new Line2D.Double(radius, radius, radius, radius + (radius * 1d));


            AffineTransform hourRotation = AffineTransform.getRotateInstance(Math.toRadians(((now.getHour() % 12) / 12d) * 360) + Math.toRadians(180), radius, radius);
            AffineTransform minuteRotation = AffineTransform.getRotateInstance(Math.toRadians((now.getMinute() / 60d) * 360d) + Math.toRadians(180), radius, radius);
            AffineTransform secondRotation = AffineTransform.getRotateInstance(Math.toRadians((now.getSecond() / 60d) * 360d) + Math.toRadians(180), radius, radius);

            // Draw the rotated line
//            g2.draw(hour);
            g2.draw(hourRotation.createTransformedShape(hour));
            g2.draw(minuteRotation.createTransformedShape(minute));
            g2.draw(secondRotation.createTransformedShape(second));
        }

        @Override
        public int getIconWidth() {
            return radius * 2;
        }

        @Override
        public int getIconHeight() {
            return radius * 2;
        }
    }

}
