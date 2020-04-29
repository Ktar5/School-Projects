package calendar.states;

import calendar.CalendarApp;
import calendar.event.Event;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;

/**
 * Allows the user to view the events on a month by month basis
 * @author Carter Gale
 */
public class ViewMonthEvents extends State {
    @Override
    public void start(CalendarApp app) {
        displayEvents(app, LocalDate.now());
    }

    private void displayEvents(CalendarApp app, LocalDate date){
        System.out.println(date.getMonth() + " " + date.getYear());
        System.out.println("Su Mo Tu We Th Fr Sa");
        LocalDate dayIteration = LocalDate.of(date.getYear(), date.getMonth(), 1);
        StringBuilder builder = new StringBuilder();
        DayOfWeek[] days = {DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};

        //Get events
        HashMap<LocalDate, List<Event>> eventsOnMonth = app.calendar.findEventsOnMonth(date);

        //Add the necessary spaces in the first week of the month
        for (DayOfWeek value : days) {
            if (!dayIteration.getDayOfWeek().equals(value)) {
                builder.append("   ");
            } else {
                break;
            }
        }
        //Start adding numbers
        while (dayIteration.getMonth().equals(date.getMonth())) {
            //Add opening bracket if it is the current day of the month
            if(eventsOnMonth.containsKey(dayIteration)){
                builder.append("{");
            }
            //Print the number itself
            builder.append(dayIteration.getDayOfMonth());
            //Add the closing bracket
            if(eventsOnMonth.containsKey(dayIteration)){
                builder.append("}");
            }
            //Do the correct amount of spacing after the number printout to make everything nicely formatted
            if(dayIteration.getDayOfMonth() <= 9){
                builder.append("  ");
            }else{
                builder.append(" ");
            }

            if(dayIteration.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
                builder.append("\n");
            }

            dayIteration = dayIteration.plus(Period.ofDays(1));
        }
        System.out.println(builder.toString());

        System.out.println("[P]revious month, [N]ext month, or [B]ack to main menu?");
        String input = app.readLine().toLowerCase();
        switch (input.charAt(0)) {
            case 'p':
                displayEvents(app, date.minus(Period.ofMonths(1)));
                break;
            case 'n':
                displayEvents(app, date.plus(Period.ofMonths(1)));
                break;
            case 'b':
                app.currentState = new MainMenu();
                app.currentState.start(app);
                return;
            default:
                System.out.println("Could not understand input: '" + input + "'. Try either P, N, or B.");
                this.start(app);
        }
        app.currentState = new MainMenu();
        app.currentState.start(app);
    }
}
