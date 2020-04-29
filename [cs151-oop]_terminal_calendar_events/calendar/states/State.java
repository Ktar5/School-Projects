package calendar.states;

import calendar.CalendarApp;

/**
 * The parent class of all the calendar.states in this package
 *
 * @author Carter Gale
 */
public abstract class State {

    /**
     * Call this method to start running the state
     *
     * @param app an instance of ReservationApp
     */
    public abstract void start(CalendarApp app);
}
