package simplecalendar.viewcontroller;

import simplecalendar.model.DataManager;
import simplecalendar.viewcontroller.monthview.MonthView;

import javax.swing.*;

/**
 * Represents the root view object for the gui portion
 * of the program
 *
 * @author Carter Gale
 */
public class MainView {
    public static MainView instance;
    private MonthView monthView;
    private DayView dayView;

    public MainView() {
        instance = this;

        monthView = new MonthView();
        dayView = new DayView();

        JFrame frame = new JFrame();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));

        jPanel.add(monthView);
        jPanel.add(dayView);

        JButton saveAndExit = new JButton("Save & Exit");
        saveAndExit.addActionListener(e -> {
            DataManager.instance.saveData();
            System.exit(0);
        });
        saveAndExit.setAlignmentX(0.5f);

        frame.add(jPanel);
        frame.add(saveAndExit);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.setTitle("Get Organized!");
    }

    /**
     * Refresh the display of the month and day views
     */
    public void refresh() {
        monthView.refresh();
        dayView.refresh();
    }

}
