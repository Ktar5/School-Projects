package calendar;

import calendar.event.EventRecurring;
import calendar.event.EventSingle;
import calendar.event.TimeInterval;
import calendar.states.State;
import calendar.states.WelcomeState;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The main class for handling basic functions of the app
 *
 * @author Carter Gale
 */
public class CalendarApp {
    public static CalendarApp instance;

    public static void main(String[] args) {
        CalendarApp.instance = new CalendarApp();
        instance.loadData();
        System.out.println("Finished loading data!");

        instance.currentState = new WelcomeState();
        instance.currentState.start(instance);
    }

    //A buffered reader attached to the console for the various inputs needed by the program
    private final BufferedReader reader;

    //The current program state
    public State currentState;

    //The main calendar object
    public final Calendar calendar;

    protected CalendarApp() {
        reader = new BufferedReader(new InputStreamReader(java.lang.System.in));
        calendar = new Calendar();
    }

    protected void loadData() {
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
                if (parts.length == 3) {
                    String[] numbers = parts[0].split(Pattern.quote("/"));
                    int month = Integer.parseInt(numbers[0]);
                    int day = Integer.parseInt(numbers[1]);
                    int year = Integer.parseInt(numbers[2]);
                    LocalDate of = LocalDate.of(year, month, day);

                    LocalTime start = LocalTime.parse(parts[1]);
                    LocalTime end = LocalTime.parse(parts[2]);
                    TimeInterval timeInterval = new TimeInterval(start, end);

                    EventSingle eventSingle = new EventSingle(name, timeInterval, of);
                    calendar.registerSingleEvent(eventSingle);
                } else {
                    HashSet<DayOfWeek> daysOfWeek = new HashSet<>();
                    for (char c : parts[0].toLowerCase().toCharArray()) {
                        switch (c) {
                            case 's':
                                daysOfWeek.add(DayOfWeek.SUNDAY);
                                break;
                            case 'm':
                                daysOfWeek.add(DayOfWeek.MONDAY);
                                break;
                            case 't':
                                daysOfWeek.add(DayOfWeek.TUESDAY);
                                break;
                            case 'w':
                                daysOfWeek.add(DayOfWeek.WEDNESDAY);
                                break;
                            case 'r':
                                daysOfWeek.add(DayOfWeek.THURSDAY);
                                break;
                            case 'f':
                                daysOfWeek.add(DayOfWeek.FRIDAY);
                                break;
                            case 'a':
                                daysOfWeek.add(DayOfWeek.SATURDAY);
                                break;
                        }
                    }

                    LocalTime start = LocalTime.parse(parts[1]);
                    LocalTime end = LocalTime.parse(parts[2]);
                    TimeInterval timeInterval = new TimeInterval(start, end);

                    String[] startDateString = parts[3].split(Pattern.quote("/"));
                    int month = Integer.parseInt(startDateString[0]);
                    int day = Integer.parseInt(startDateString[1]);
                    int year = Integer.parseInt(startDateString[2]);
                    LocalDate startDate = LocalDate.of(year, month, day);

                    String[] endDateString = parts[4].split(Pattern.quote("/"));
                    month = Integer.parseInt(endDateString[0]);
                    day = Integer.parseInt(endDateString[1]);
                    year = Integer.parseInt(endDateString[2]);
                    LocalDate endDate = LocalDate.of(year, month, day);

                    EventRecurring eventRecurring = new EventRecurring(name, timeInterval, startDate, endDate, daysOfWeek);
                    calendar.getRecurringEvents().add(eventRecurring);
                }


                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    /**
     * Call this to save all current in-memory data to the drive
     */
    public void saveData() {
        File file = new File("output.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        List<String> lines = new ArrayList<>();
        for (EventRecurring event : calendar.getRecurringEvents()) {
            lines.add(event.getName());
            StringBuilder linetwo = new StringBuilder();
            for (DayOfWeek dayOfWeek : event.getDaysOccurring()) {
                linetwo.append(dayOfWeek.name().charAt(0));
            }
            linetwo.append(" ");
            linetwo.append(event.getTimeInterval().start.toString());
            linetwo.append(" ");
            linetwo.append(event.getTimeInterval().end.toString());
            linetwo.append(" ");
            linetwo.append(event.getStartDate().format(formatter));
            linetwo.append(" ");
            linetwo.append(event.getEndDate().format(formatter));
            lines.add(linetwo.toString());
        }
        for (List<EventSingle> value : calendar.getSingleEvents().values()) {
            for (EventSingle event : value) {
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

    /**
     * Use this to read a line from the console
     *
     * @return the line read from console by user
     */
    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an error with your input.");
            return null;
        }
    }
}
