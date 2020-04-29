package calendar.event;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a recurring event
 *
 * @author Carter Gale
 */
public class EventRecurring extends Event {
    /**
     * The ending date of the recurring events
     */
    private final LocalDate endDate;
    /**
     * The days of the week that this event occurs on
     */
    private final Set<DayOfWeek> daysOccurring;

    public EventRecurring(String name, TimeInterval timeInterval, LocalDate startDate, LocalDate endDate, HashSet<DayOfWeek> days) {
        super(name, timeInterval, startDate);
        this.endDate = endDate;
        this.daysOccurring = days;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Set<DayOfWeek> getDaysOccurring() {
        return daysOccurring;
    }

    /**
     * Lets you know if this event will happen on the specified day
     * @param date the day to check
     * @return true if the event occurs on this day, false otherwise
     */
    public boolean happensOn(LocalDate date){
        if((date.isEqual(this.getStartDate()) || date.isEqual(this.getEndDate()))
                ||
                (date.isAfter(this.getStartDate()) && date.isBefore(this.getEndDate()))){
            return daysOccurring.contains(date.getDayOfWeek());
        }
        return false;
    }

}
