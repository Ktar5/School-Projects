package calendar.event;

import java.time.LocalDate;

/**
 * Represents a single-day event
 * @author Carter Gale
 */
public class EventSingle extends Event {
    public EventSingle(String name, TimeInterval timeInterval, LocalDate startDate) {
        super(name, timeInterval, startDate);
    }
}
