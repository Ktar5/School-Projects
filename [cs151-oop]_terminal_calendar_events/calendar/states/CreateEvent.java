package calendar.states;

import calendar.CalendarApp;
import calendar.event.Event;
import calendar.event.EventSingle;
import calendar.event.TimeInterval;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Allows the user to create events
 *
 * @author Carter Gale
 */
public class CreateEvent extends State {
    private String eventName;
    private LocalDate eventDate;
    private TimeInterval eventTimeInterval;

    @Override
    public void start(CalendarApp app) {
        eventName = retrieveName(app);
        eventDate = retrieveDate(app);
        eventTimeInterval = retrieveTime(app);

        checkOverlap(app);

        app.calendar.registerSingleEvent(new EventSingle(eventName, eventTimeInterval, eventDate));

        System.out.println("Event successfully created! Press ENTER to go back to main menu.");
        System.out.println();
        app.readLine();
        app.currentState = new MainMenu();
        app.currentState.start(app);
    }

    private void checkOverlap(CalendarApp app){
        Event overlappingEvent = null;
        List<Event> eventsOnDay = app.calendar.findEventsOnDay(eventDate);
        for (Event event : eventsOnDay) {
            if (event.getTimeInterval().isOverlapping(eventTimeInterval)) {
                overlappingEvent = event;
                break;
            }
        }
        if (overlappingEvent != null) {
            System.out.println("We found that your date/time conflicts with another event: " + overlappingEvent);
            char c = chooseOption(app);
            switch (c){
                case 'd':
                    eventDate = retrieveDate(app);
                    break;
                case 't':
                    eventTimeInterval = retrieveTime(app);
                    break;
                case 'b':
                    app.currentState = new MainMenu();
                    app.currentState.start(app);
                    break;
            }
            checkOverlap(app);
        }
    }

    private char chooseOption(CalendarApp app) {
        System.out.println("You can change your [D]ate, change your [T]ime, or go [B]ack to main menu");
        String input = app.readLine().toLowerCase();
        switch (input.charAt(0)) {
            case 'd':
                return 'd';
            case 't':
                return 't';
            case 'b':
                return 'b';
            default:
                System.out.println("Could not understand input: '" + input + "'. Try either D, T, or B.");
                this.chooseOption(app);
        }
        return 'b';
    }

    private String retrieveName(CalendarApp app) {
        System.out.println("Please type in the name of the event, or 'cancel' to return to main menu.");
        String input = app.readLine();
        if (input.equalsIgnoreCase("cancel")) {
            app.currentState = new MainMenu();
            app.currentState.start(app);
            return null;
        }
        return input;
    }

    private LocalDate retrieveDate(CalendarApp app) {
        System.out.println("Please type in the date of the event, or 'cancel' to return to main menu.");
        System.out.println("Use the format: MM/DD/YYYY, ex: 03/25/2020");
        String input = app.readLine();
        if (input.equalsIgnoreCase("cancel")) {
            app.currentState = new MainMenu();
            app.currentState.start(app);
            return null;
        }

        try {
            String[] parts = input.split(Pattern.quote("/"));
            int month = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            System.out.println("Something went wrong please try again. ( " + e.getClass() + " )");
            return retrieveDate(app);
        }
    }

    private TimeInterval retrieveTime(CalendarApp app) {
        System.out.println("Please type in the time of the event, or 'cancel' to return to the main menu");
        System.out.println("Use the format HH:MM HH:MM, where it is the start time, a space, and then the end time in 24-hour format");
        String input = app.readLine();
        if (input.equalsIgnoreCase("cancel")) {
            app.currentState = new MainMenu();
            app.currentState.start(app);
            return null;
        }

        try {
            String[] times = input.split(Pattern.quote(" "));
            String startTimeString = times[0];
            String endTimeString = times[1];

            String[] startSplit = startTimeString.split(Pattern.quote(":"));
            int startHour = Integer.parseInt(startSplit[0]);
            int startMinute = Integer.parseInt(startSplit[1]);

            String[] endSplit = endTimeString.split(Pattern.quote(":"));
            int endHour = Integer.parseInt(endSplit[0]);
            int endMinute = Integer.parseInt(endSplit[1]);

            return new TimeInterval(LocalTime.of(startHour, startMinute), LocalTime.of(endHour, endMinute));
        } catch (Exception e) {
            System.out.println("Something went wrong please try again. ( " + e.getClass() + " )");
            return retrieveTime(app);
        }

    }
}
