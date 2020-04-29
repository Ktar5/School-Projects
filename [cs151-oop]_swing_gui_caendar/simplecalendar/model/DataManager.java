package simplecalendar.model;

import simplecalendar.model.event.Event;
import simplecalendar.model.event.TimeInterval;
import simplecalendar.viewcontroller.MainView;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Manage all the data for the program
 *
 * @author Carter Gale
 */
public class DataManager {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    /**
     * And instance of this class for referencing from other places
     */
    public static DataManager instance;
    /**
     * The calendar that keeps track of all events
     */
    public final Calendar calendar;
    /**
     * The currently selected date
     */
    private LocalDate selectedDate;

    public DataManager() {
        instance = this;
        this.selectedDate = LocalDate.now();
        this.calendar = new Calendar();
        loadData();
    }

    /**
     * Return the date currently selected
     */
    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    /**
     * Set the currently selected date
     */
    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
        //update all the views
        MainView.instance.refresh();
    }

    /**
     * Load data
     */
    private void loadData() {
        File file = new File("events.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String name = line;
                line = reader.readLine();
                String[] parts = line.split(Pattern.quote(" "));

                //load data
                String[] numbers = parts[0].split(Pattern.quote("/"));
                int month = Integer.parseInt(numbers[0]);
                int day = Integer.parseInt(numbers[1]);
                int year = Integer.parseInt(numbers[2]);
                LocalDate of = LocalDate.of(year, month, day);

                LocalTime start = LocalTime.parse(parts[1]);
                LocalTime end = LocalTime.parse(parts[2]);
                TimeInterval timeInterval = new TimeInterval(start, end);

                calendar.registerEvent(new Event(name, timeInterval, of));

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call this to save all current in-memory data to the drive
     */
    public void saveData() {
        File file = new File("events.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        List<String> lines = new ArrayList<>();
        for (List<Event> eventsOnDay : calendar.getEvents().values()) {
            for (Event event : eventsOnDay) {
                lines.add(event.getName());
                String linetwo = event.getStartDate().format(formatter) +
                        " " + event.getTimeInterval().start.toString() +
                        " " + event.getTimeInterval().end.toString();
                lines.add(linetwo);
            }
        }

        FileWriter writer;
        try {
            writer = new FileWriter(file);
            for (String str : lines) {
                writer.write(str + java.lang.System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
