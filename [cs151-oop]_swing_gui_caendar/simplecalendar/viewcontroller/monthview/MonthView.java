package simplecalendar.viewcontroller.monthview;

import simplecalendar.model.DataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static simplecalendar.model.Calendar.stringFromDayOfWeek;

/**
 * The view component for displaying the month and days in the month
 *
 * @author Carter Gale
 */
public class MonthView extends JPanel {
    private JLabel monthText;
    private List<JLabel> labels;
    private List<DayOfMonthIcon> icons;

    //Reorder the days of the week
    private DayOfWeek[] daysOfWeek = {DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};

    public MonthView() {
        labels = new ArrayList<>();
        icons = new ArrayList<>();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //Create the "April 2020" text display
        monthText = new JLabel();

        //Create a grid to display days and day icons
        JPanel dateFrame = new JPanel();
        dateFrame.setLayout(new GridLayout(7, 7));

        //Add the "M T W T F" icons
        for (DayOfWeek value : daysOfWeek) {
            dateFrame.add(new JLabel(new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    ((Graphics2D) g).drawString(stringFromDayOfWeek(value), 5, 17);
                }

                @Override
                public int getIconWidth() {
                    return 25;
                }

                @Override
                public int getIconHeight() {
                    return 25;
                }
            }));
        }

        //Create labels for the days of the month
        for (int i = 0; i < 42; i++) {
            icons.add(new DayOfMonthIcon());
            labels.add(new JLabel(icons.get(i)));
            dateFrame.add(labels.get(i));
            int finalI = i;
            labels.get(i).addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    labelClicked(finalI);
                }
            });
        }

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        JButton goForward = new JButton(">>>");
        goForward.addActionListener(e -> DataManager.instance.setSelectedDate(getDate().plusDays(1)));
        JButton goBack = new JButton("<<<");
        goBack.addActionListener(e -> DataManager.instance.setSelectedDate(getDate().minusDays(1)));
        buttonsPanel.add(goBack);
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(goForward);

        monthText.setAlignmentX(0.5f);
        this.add(monthText);
        this.add(dateFrame);
        this.add(buttonsPanel);

        refresh();
    }

    /**
     * Refresh the MonthView display to represent the currently selected date
     */
    public void refresh() {
        monthText.setText(getDate().getMonth().name() + " " + getDate().getYear());
        for (DayOfMonthIcon icon : icons) {
            icon.setSelected(false);
            icon.setDay(-1);
        }
        int indent = getIndentFromDayOfWeek(getDate().withDayOfMonth(1).getDayOfWeek());
        for (int i = indent; i < getDate().lengthOfMonth() + indent; i++) {
            //if its the selected day
            if (i == getDate().getDayOfMonth() + indent - 1) {
                icons.get(i).setSelected(true);
            }
            icons.get(i).setDay((i - indent) + 1);
        }
        monthText.repaint();
        for (JLabel label : labels) {
            label.repaint();
        }
    }

    private int getIndentFromDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return 1;
            case TUESDAY:
                return 2;
            case WEDNESDAY:
                return 3;
            case THURSDAY:
                return 4;
            case FRIDAY:
                return 5;
            case SATURDAY:
                return 6;
            case SUNDAY:
                return 0;
        }
        return 0;
    }

    private LocalDate getDate() {
        return DataManager.instance.getSelectedDate();
    }

    private void labelClicked(int i) {
        int indent = getIndentFromDayOfWeek(getDate().withDayOfMonth(1).getDayOfWeek());
        int dayOfMonth = i - indent + 1;
        if (dayOfMonth > getDate().lengthOfMonth() || dayOfMonth < 1) {
            return;
        }
        DataManager.instance.setSelectedDate(getDate().withDayOfMonth(dayOfMonth));
        System.out.println("Clicked on label for day index: " + i + " Representing: " + getDate().withDayOfMonth(dayOfMonth).toString());
    }

}
