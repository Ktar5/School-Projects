package reservations.states;

import reservations.ReservationApp;
import reservations.user.User;

/**
 * This state allows the user to sign up
 *
 * @author Carter Gale
 */
public class SignUpState extends State {

    @Override
    public void start(ReservationApp app) {
        System.out.println();
        System.out.println("SIGN UP");
        System.out.println("Please enter desired username: ");
        String username = app.readLine();
        if (username.equalsIgnoreCase("x")) {
            app.currentState = new WelcomeState();
            app.currentState.start(app);
            return;
        } else if (app.users.containsKey(username)) {
            System.out.println("That username already exists, choose another username or type 'x' at the prompt to exit.");
            this.start(app);
        }
        System.out.println("Username accepted, please enter desired password: ");
        String password = app.readLine();
        System.out.println("Account created, logging you in...");
        app.users.put(username, new User(username, password));
        app.currentUser = app.users.get(username);
        System.out.println("Successfully logged in!");
        app.currentState = new TransactionState();
        app.currentState.start(app);
    }
}
