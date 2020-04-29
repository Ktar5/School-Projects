package reservations.states;

import reservations.ReservationApp;

/**
 * Very simple state that saves the data and then exits the program
 *
 * @author Carter Gale
 */
public class ExitState extends State {
    @Override
    public void start(ReservationApp app) {
        System.out.println("Saving data...");
        app.saveData();
        System.out.println("Saved data successfully, shutting down!");
        System.exit(0);
    }
}
