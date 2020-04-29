package simplecalendar.viewcontroller;

import simplecalendar.model.DataManager;
import simplecalendar.model.event.Event;
import simplecalendar.model.event.TimeInterval;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A utility class for displaying the dialog to create events
 * @author Carter Gale
 */
public class CreateEventDialog {

    /**
     * Shows a dialog to create an event and creates the event if
     * a proper event was input. If the event is invalid, it displays an error
     * message. If it was valid, then it refreshes all the rest of the view.
     */
    public static void showDialog() {
        LocalDate date = DataManager.instance.getSelectedDate();
        JTextField eventName = new JTextField();
        JTextField eventStartTime = new JTextField();
        JTextField eventEndTime = new JTextField();

        final JComponent[] inputs = new JComponent[]{
                new JLabel("Event Name:"),
                eventName,
                new JLabel("Note that all times entered are in 24 hour time and must be 0-padded, example: '09:50' = 9:50am"),
                new JLabel("Event Start Time (24hr time, ex: '15:23' for 3:23pm)"),
                eventStartTime,
                new JLabel("Event End Time (24hr time, ex: '17:20' for 5:20pm)"),
                eventEndTime
        };

        int result = JOptionPane.showConfirmDialog(null, inputs, "Create Event on "
                + date.getDayOfWeek() + ", " + date.getMonth().name() + " " + date.getDayOfMonth() + ", "
                + date.getYear(), JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            //Parse the times
            LocalTime start = LocalTime.parse(eventStartTime.getText());
            LocalTime end = LocalTime.parse(eventEndTime.getText());
            if (start.isAfter(end)) {
                JOptionPane.showMessageDialog(null, "ERROR: Start time is after end time");
                return;
            }
            TimeInterval timeInterval = new TimeInterval(start, end);

            //Make sure it is not overlapping
            Event overlappingEvent = null;
            for (Event event : DataManager.instance.calendar.findEventsOnDay(date)) {
                if (event.getTimeInterval().isOverlapping(timeInterval)) {
                    overlappingEvent = event;
                    break;
                }
            }

            //If it is, notify
            if (overlappingEvent != null) {
                JOptionPane.showMessageDialog(null, "ERROR: Event is overlapping with: " + overlappingEvent.getName());
                return;
            }

            DataManager.instance.calendar.registerEvent(new Event(eventName.getText(), timeInterval, date));
            MainView.instance.refresh();
        }
    }

}
