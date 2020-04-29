package calendar.states;

import calendar.CalendarApp;
import calendar.event.Event;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;

/**
 * Allows the user to view the events on a day by day basis
 * @author Carter Gale
 */
public class ViewDayEvents extends State{
    @Override
    public void start(CalendarApp app) {
        displayEvents(app, LocalDate.now());
        app.currentState = new MainMenu();
        app.currentState.start(app);
    }

    private void displayEvents(CalendarApp app, LocalDate date){
        System.out.println(date.getDayOfWeek() + ", " + date.getMonth() + " " + date.getDayOfMonth() + ", " + date.getYear());
        List<Event> eventsOnDay = app.calendar.findEventsOnDay(date);
        if(eventsOnDay.isEmpty()){
            System.out.println("You don't have any events on this day!");
        }
        else{
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
