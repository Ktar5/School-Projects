package reservations.states;

import reservations.ReservationApp;

/**
 * This state allows the user to sign in
 *
 * @author Carter Gale
 */
public class SignInState extends State {

    @Override
    public void start(ReservationApp app) {
        System.out.println("Please enter your username: ");
        String input = app.readLine();
        if (!app.users.containsKey(input)) {
            System.out.println("That username does not exist, please try again.");
            this.start(app);
        }
        System.out.println("Username accepted.");
        System.out.println("Please enter the password for your account: ");
        String password = app.users.get(input).password;
        String input2 = app.readLine();
        if (!input2.equalsIgnoreCase(password)) {
            System.out.println("Sorry but that password: '" + input2 + "' does not match, try again.");
            this.start(app);
        }
        System.out.println("Password accepted, you're now logged in.");
        app.currentUser = app.users.get(input);
        app.currentState = new TransactionState();
        app.currentState.start(app);
    }

}
