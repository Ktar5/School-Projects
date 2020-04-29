
//5.2

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
//Taken from textbook, and modified for homework.
/**
 * A class for testing an implementation of the Observer pattern.
 */
public class ObserverTester {
    /**
     * Creates a DataModel and attaches barchart and textfield listeners
     *
     * @param args unused
     */
    public static void main(String[] args) {
        ArrayList<Double> data = new ArrayList<>();

        data.add(33.0);
        data.add(44.0);
        data.add(22.0);
        data.add(22.0);

        DataModel model = new DataModel(data);

        TextFrame frame = new TextFrame(model);

        BarFrame barFrame = new BarFrame(model);
        barFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                int x = e.getX();
                int y = e.getY();

                y = y - 30; //Account for title bar

                int index = y / (200 / data.size()); //Get which data we want to modify

                double maxValueFit = barFrame.maxValueFit;
                double widthOfWindow = 210;

                double value = (x / widthOfWindow) * maxValueFit;

                model.update(index, value);

                barFrame.stateChanged(null);

                frame.update();

                System.out.println("x: " + x + "    y: " + y);
                System.out.println(index);
            }
        });

        model.attach(barFrame);
    }
}