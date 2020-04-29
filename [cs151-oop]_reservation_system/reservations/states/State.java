package reservations.states;

import reservations.ReservationApp;

/**
 * The parent class of all the states in this package
 *
 * @author Carter Gale
 */
public abstract class State {

    /**
     * Call this method to start running the state
     *
     * @param app an instance of ReservationApp
     */
    public abstract void start(ReservationApp app);
}
