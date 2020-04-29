package simplecalendar.model.event;

import java.time.LocalTime;

/**
 * Contains both a start time and an end time as well as methods to check for overlaps between intervals
 *
 * @author Carter Gale
 */
public class TimeInterval {
    /**
     * The start and end times of the interval
     */
    public final LocalTime start, end;

    public TimeInterval(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Check if the supplied time interval overlaps with this one
     *
     * @param interval the interval to check overlapping with
     * @return true if there is an overlap, false otherwise
     */
    public boolean isOverlapping(TimeInterval interval) {
        return isDuringInterval(interval.start) || isDuringInterval(interval.end);
    }

    private boolean isDuringInterval(LocalTime toCheck) {
        return toCheck.isAfter(start) && toCheck.isBefore(end);
    }
}
