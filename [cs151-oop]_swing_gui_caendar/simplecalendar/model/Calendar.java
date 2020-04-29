package simplecalendar.model;

import simplecalendar.model.event.Event;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

/**
 * Stores all information about the events
 *
 * @author Carter Gale
 */
public class Calendar {
    //LocalDate.toString -> List<Event> for simple one-time events
    private final Map<String, List<Event>> events;

    public Calendar() {
        events = new HashMap<>();
    }

    /**
     * Return the single character abbreviation of the day of week
     *
     * @param dayOfWeek the day of week to convert
     * @return a string containing a single character denoting the abbreviation
     */
    public static String stringFromDayOfWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return "M";
            case TUESDAY:
                return "T";
            case WEDNESDAY:
                return "W";
            case THURSDAY:
                return "R";
            case FRIDAY:
                return "F";
            case SATURDAY:
                return "A";
            case SUNDAY:
                return "S";
        }
        return null;
    }

    /**
     * Find any events that are occurring on the specified date
     *
     * @param date the date to look for events happening on
     * @return a list of events that occur on the specified date
     */
    public List<Event> findEventsOnDay(LocalDate date) {
        //Check the single-time events
        List<Event> eventsOnDate = events.get(date.toString());
        if (eventsOnDate != null && !eventsOnDate.isEmpty()) {
            return eventsOnDate;
        }

        return Collections.emptyList();
    }

    /**
     * Find any events that are occurring on the specified month
     *
     * @param date the month to look for events happening on
     * @return a list of events that occur on the specified month
     */
    public HashMap<LocalDate, List<Event>> findEventsOnMonth(LocalDate date) {
        HashMap<LocalDate, List<Event>> eventsMap = new HashMap<>();
        LocalDate dayIteration = LocalDate.of(date.getYear(), date.getMonth(), 1);
        while (dayIteration.getMonth().equals(date.getMonth())) {
            List<Event> eventsOnDay = findEventsOnDay(dayIteration);
            if (!eventsOnDay.isEmpty()) {
                eventsMap.put(dayIteration, eventsOnDay);
            }
            dayIteration = dayIteration.plus(Period.ofDays(1));
        }
        return eventsMap;
    }

    /**
     * Adds a single event to the calendar
     *
     * @param event the event to add
     */
    public void registerEvent(Event event) {
        String s = event.getStartDate().toString();
        if (!events.containsKey(s)) {
            events.put(s, new ArrayList<>());
        }
        events.get(s).add(event);
    }

    public Map<String, List<Event>> getEvents() {
        return events;
    }

}
