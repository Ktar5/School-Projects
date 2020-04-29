package calendar.states;

import calendar.CalendarApp;

/**
 * Allows the user to select how they want to view the selection screen then redirects
 * them to the appropriate state
 *
 * @author Carter Gale
 */
public class ViewBySelectionState extends State {
    @Override
    public void start(CalendarApp app) {
        System.out.println("Would you like to view by [D]ay or by [M]onth, or go [B]ack to main menu?");
        String input = app.readLine().toLowerCase();
        switch (input.charAt(0)) {
            case 'd':
                app.currentState = new ViewDayEvents();
                app.currentState.start(app);
                break;
            case 'm':
                app.currentState = new ViewMonthEvents();
                app.currentState.start(app);
                break;
            case 'b':
                app.currentState = new MainMenu();
                app.currentState.start(app);
                break;
            default:
                System.out.println("Could not understand input: '" + input + "'. Try either D, M, or B.");
                this.start(app);
        }
        app.currentState = new MainMenu();
        app.currentState.start(app);
    }
}
