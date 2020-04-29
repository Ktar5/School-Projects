package calendar.states;

import calendar.CalendarApp;
import calendar.event.Event;
import calendar.event.EventRecurring;
import calendar.event.EventSingle;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Allows the user to delete or cancel events
 *
 * @author Carter Gale
 */
public class DeleteEvent extends State {
    @Override
    public void start(CalendarApp app) {
        System.out.println("Delete: [S]elect an event, [A]ll events in a day, [R]ecurring event, or go [B]ack to main menu.");
        String input = app.readLine().toLowerCase();
        switch (input.charAt(0)) {
            case 's':
                deleteSelect(app);
                break;
            case 'a':
                deleteAll(app);
                break;
            case 'r':
                deleteRecurring(app);
                break;
            case 'b':
                app.currentState = new MainMenu();
                app.currentState.start(app);
                break;
            default:
                System.out.println("Could not understand input: '" + input + "'. Try either S, A, R, or B.");
                this.start(app);
        }
        app.currentState = new MainMenu();
        app.currentState.start(app);

    }

    private void deleteSelect(CalendarApp app){
        System.out.println("Please enter the date in dd/mm/yyyy that you'd like to select for events to browse and delete.");
        String input = app.readLine();
        LocalDate date = null;
        try {
            String[] parts = input.split(Pattern.quote("/"));
            int month = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            date = LocalDate.of(year, month, day);
        } catch (Exception e) {
            System.out.println("Something went wrong please try again. ( " + e.getClass() + " )");
            deleteSelect(app);
        }
        if(date == null){
            return;
        }

        List<EventSingle> eventSingles = app.calendar.getSingleEvents().get(date.toString());
        if(eventSingles.isEmpty()){
            System.out.println("There are no events at this date.");
            start(app);
        }
        eventSingles.sort(Comparator.comparing(o -> o.getTimeInterval().start));
        for (int i = 0; i < eventSingles.size(); i++) {
            Event event = eventSingles.get(i);
            System.out.println(i + ". " + event.getTimeInterval().start.toString() + " - " + event.getTimeInterval().end.toString() + " " + event.getName());
        }
        System.out.println("Please type the event # (displayed next to the event) of the event you wish to cancel.");
        input = app.readLine();
        int i = Integer.parseInt(input);
        if(i < 0 || i > eventSingles.size() - 1){
            System.out.println("You entered a number too high or low, please try again");
            deleteSelect(app);
        }
        eventSingles.remove(i);
        System.out.println("Event successfully removed! Returning you to the Delete menu.");
        start(app);
    }

    private void deleteAll(CalendarApp app){
        System.out.println("Please enter the date in dd/mm/yyyy that you'd like to select for events to delete.");
        String input = app.readLine();
        LocalDate date = null;
        try {
            String[] parts = input.split(Pattern.quote("/"));
            int month = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            date = LocalDate.of(year, month, day);
        } catch (Exception e) {
            System.out.println("Something went wrong please try again. ( " + e.getClass() + " )");
            deleteSelect(app);
        }
        if(date == null){
            return;
        }

        List<EventSingle> eventSingles = app.calendar.getSingleEvents().get(date.toString());
        if(eventSingles.isEmpty()){
            System.out.println("There are no events at this date.");
            start(app);
        }
        eventSingles.clear();
        System.out.println("All events on this day cancelled.");
        start(app);
    }

    private void deleteRecurring(CalendarApp app){
        System.out.println("Please enter the full name of a recurring event that you wish to delete");
        String input = app.readLine().toLowerCase();
        Set<EventRecurring> recurringEvents = app.calendar.getRecurringEvents();
        EventRecurring deleteEvent = null;
        for (EventRecurring recurringEvent : recurringEvents) {
            if(recurringEvent.getName().equalsIgnoreCase(input)){
                deleteEvent = recurringEvent;
                break;
            }
        }
        if(deleteEvent == null){
            System.out.println("Unable to find a recurring event with that name, try again.");
            deleteRecurring(app);
        }
        recurringEvents.remove(deleteEvent);
        System.out.println("Event deleted.");
        start(app);
    }
}
