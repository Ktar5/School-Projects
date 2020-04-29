package calendar;

import calendar.event.Event;
import calendar.event.EventRecurring;
import calendar.event.EventSingle;

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
    private final Map<String, List<EventSingle>> singleEvents;

    //List of recurring events
    private final Set<EventRecurring> recurringEvents;

    public Calendar() {
        singleEvents = new HashMap<>();
        recurringEvents = new HashSet<>();
    }

    /**
     * Find any events that are occurring on the specified date
     *
     * @param date the date to look for events happening on
     * @return a list of events that occur on the specified date
     */
    public List<Event> findEventsOnDay(LocalDate date) {
        List<Event> events = new ArrayList<>();

        //Check the single-time events
        List<EventSingle> eventSingles = singleEvents.get(date.toString());
        if (eventSingles != null && !eventSingles.isEmpty()) {
            events.addAll(eventSingles);
        }

        //Check the recurring events
        for (EventRecurring recurringEvent : recurringEvents) {
            if (recurringEvent.happensOn(date)) {
                events.add(recurringEvent);
            }
        }

        return events;
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
     * @param event the event to add
     */
    public void registerSingleEvent(EventSingle event) {
        String s = event.getStartDate().toString();
        if (!singleEvents.containsKey(s)) {
            singleEvents.put(s, new ArrayList<>());
        }
        singleEvents.get(s).add(event);
    }

    public Map<String, List<EventSingle>> getSingleEvents() {
        return singleEvents;
    }

    public Set<EventRecurring> getRecurringEvents() {
        return recurringEvents;
    }

    /**
     * Return the single character abbreviation of the day of week
     * @param dayOfWeek the day of week to convert
     * @return a string containing a single character denoting the abbreviation
     */
    public static String stringFromDayOfWeek(DayOfWeek dayOfWeek){
        switch (dayOfWeek){
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

}
