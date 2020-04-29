package simplecalendar.model.event;

import java.time.LocalDate;

/**
 * Represents the basic data shared between both EventRecurring and EventSingle
 *
 * @author Carter Gale
 */
public class Event {
    /**
     * The name of the event
     */
    private final String name;

    /**
     * The  time interval at which this event occurs during
     */
    private final TimeInterval timeInterval;

    /**
     * The start date for the event, also the "occurring" date for a one-time event
     */
    private final LocalDate startDate;

    public Event(String name, TimeInterval timeInterval, LocalDate startDate) {
        this.name = name;
        this.timeInterval = timeInterval;
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

}
