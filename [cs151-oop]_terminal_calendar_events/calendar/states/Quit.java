package calendar.states;

import calendar.CalendarApp;

/**
 * Very simple state that saves the data and then exits the program
 *
 * @author Carter Gale
 */
public class Quit extends State {
    @Override
    public void start(CalendarApp app) {
        System.out.println("Saving data...");
        app.saveData();
        System.out.println("Saved data successfully, shutting down!");
        System.exit(0);
    }
}
