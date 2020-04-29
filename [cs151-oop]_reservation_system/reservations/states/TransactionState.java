package reservations.states;

import reservations.ReservationApp;

/**
 * This state represents the signed-in menu for reserving, viewing, cancelling, and signing out
 *
 * @author Carter Gale
 */
public class TransactionState extends State {
    @Override
    public void start(ReservationApp app) {
        app.currentUser.startTransaction();
        System.out.println("Beginning transaction.. Please select from the following options");
        System.out.println("[R]eserve    [V]iew    [C]ancel    [O]ut");
        String input = app.readLine();
        switch (input.charAt(0)) {
            case 'r':
                app.currentState = new ReserveState();
                app.currentState.start(app);
                break;
            case 'v':
                app.currentState = new ViewState();
                app.currentState.start(app);
                break;
            case 'c':
                app.currentState = new CancelState();
                app.currentState.start(app);
                break;
            case 'o':
                app.currentState = new OutState();
                app.currentState.start(app);
                break;
            default:
                System.out.println("Could not understand input: '" + input + "'. Try either R, V, C, or O.");
                this.start(app);
        }
    }
}
