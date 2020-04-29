package calendar.states;

import calendar.Calendar;
import calendar.CalendarApp;
import calendar.event.Event;
import calendar.event.EventRecurring;
import calendar.event.EventSingle;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Displays events in a list to the user
 * @author Carter Gale
 */
public class EventList extends State{
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

    @Override
    public void start(CalendarApp app) {
        Set<EventRecurring> recurringEvents = app.calendar.getRecurringEvents();
        Map<String, List<EventSingle>> singleEvents = app.calendar.getSingleEvents();

        System.out.println("ONE TIME EVENTS");
        ArrayList<String> sortedSingleEventDateStrings = new ArrayList<>(singleEvents.keySet());
        sortedSingleEventDateStrings.sort(Comparator.comparing(LocalDate::parse));
        for (String dateString : sortedSingleEventDateStrings) {
            List<EventSingle> eventSingles = singleEvents.get(dateString);
            eventSingles.sort(Comparator.comparing(o -> o.getTimeInterval().start));
            for (EventSingle eventSingle : eventSingles) {
                LocalDate startDate = eventSingle.getStartDate();
                System.out.println(eventSingle.getName() + " : " + eventSingle.getTimeInterval().start.toString()
                        + " - " + eventSingle.getTimeInterval().end.toString() + " on " + startDate.getDayOfWeek()
                        + " " + startDate.getMonth() + " " + startDate.getDayOfMonth() + ", " + startDate.getYear());
            }
        }
        System.out.println();
        System.out.println("RECURRING EVENTS");
        DayOfWeek[] days = {DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
        ArrayList<EventRecurring> sortedRecurringEvents = new ArrayList<>(recurringEvents);
        sortedRecurringEvents.sort(Comparator.comparing(Event::getStartDate));
        for (EventRecurring event : sortedRecurringEvents) {
            StringBuilder builder = new StringBuilder();
            builder.append(event.getName()).append(" (");
            for (DayOfWeek dayOfWeek : days) {
                if(event.getDaysOccurring().contains(dayOfWeek)){
                    builder.append(Calendar.stringFromDayOfWeek(dayOfWeek));
                }
            }
            builder.append(")\n");
            builder.append(event.getTimeInterval().start.toString()).append(" - ")
                    .append(event.getTimeInterval().end.toString()).append(" ")
                    .append(formatter.format(event.getStartDate())).append(" ")
                    .append(formatter.format(event.getEndDate())).append("\n");
            System.out.println(builder);
        }
        app.currentState = new MainMenu();
        app.currentState.start(app);
    }
}
