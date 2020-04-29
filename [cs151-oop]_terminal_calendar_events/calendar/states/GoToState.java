package calendar.states;

import calendar.CalendarApp;
import calendar.event.Event;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The user is able to go to a specific date and examine events at that date
 *
 * @author Carter Gale
 */
public class GoToState extends State {
    @Override
    public void start(CalendarApp app) {
        LocalDate localDate = retrieveDate(app);
        if (localDate != null) {
            displayEvents(app, localDate);
        } else {
            System.out.println("Something went wrong.");
        }
        app.currentState = new MainMenu();
        app.currentState.start(app);
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

    private void displayEvents(CalendarApp app, LocalDate date) {
        System.out.println(date.getDayOfWeek() + ", " + date.getMonth() + " " + date.getDayOfMonth() + ", " + date.getYear());
        List<Event> eventsOnDay = app.calendar.findEventsOnDay(date);
        if (eventsOnDay.isEmpty()) {
            System.out.println("You don't have any events on this day!");
        } else {
            eventsOnDay.sort(Comparator.comparing(Event::getStartDate));
            for (Event event : eventsOnDay) {
                System.out.println(event.getName() + " : " + event.getTimeInterval().start.toString() + " - " + event.getTimeInterval().end.toString());
            }
        }
        System.out.println("[P]revious day, [N]ext day, or [B]ack to main menu?");
        String input = app.readLine().toLowerCase();
        switch (input.charAt(0)) {
            case 'p':
                displayEvents(app, date.minus(Period.ofDays(1)));
                break;
            case 'n':
                displayEvents(app, date.plus(Period.ofDays(1)));
                break;
            case 'b':
                app.currentState = new MainMenu();
                app.currentState.start(app);
                return;
            default:
                System.out.println("Could not understand input: '" + input + "'. Try either P, N, or B.");
                displayEvents(app, date);
        }
    }

}
