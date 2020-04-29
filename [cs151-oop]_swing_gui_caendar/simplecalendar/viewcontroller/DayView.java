package simplecalendar.viewcontroller;

import simplecalendar.model.DataManager;
import simplecalendar.model.event.Event;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Shows the day view part of the calendar
 *
 * @author Carter Gale
 */
public class DayView extends JPanel {
    private JLabel day;
    private JTextArea textArea;

    public DayView() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel topBar = new JPanel();
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));

        day = new JLabel();
        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(350, 250));
        textArea.setMinimumSize(new Dimension(350, 250));
        textArea.setEditable(false);

        JButton createEventButton = new JButton("Create Event");
        createEventButton.addActionListener(e -> CreateEventDialog.showDialog());

        topBar.add(createEventButton);
        topBar.add(Box.createHorizontalGlue());
        topBar.add(day);

        this.add(topBar);
        this.add(textArea);

        refresh();
    }

    /**
     * Refresh the display
     */
    public void refresh() {
        day.setText(getDate().getDayOfWeek() + ", " + getDate().getMonth().name() + " " + getDate().getDayOfMonth() + ", " + getDate().getYear());
        List<Event> eventsOnDay = DataManager.instance.calendar.findEventsOnDay(getDate());
        eventsOnDay.sort(Comparator.comparing(Event::getStartDate));
        Collections.reverse(eventsOnDay);

        StringBuilder builder = new StringBuilder();
        for (Event event : eventsOnDay) {
            LocalTime start = event.getTimeInterval().start;
            LocalTime end = event.getTimeInterval().end;

            builder.append(start.getHour()).append(":").append(start.getMinute());
            builder.append(" - ");
            builder.append(end.getHour()).append(":").append(end.getMinute());

            builder.append("  =>  ");
            builder.append(event.getName());
            builder.append("\n");
        }
        textArea.setText(builder.toString());
    }

    private LocalDate getDate() {
        return DataManager.instance.getSelectedDate();
    }
}
