package reservations.states;


import reservations.ReservationApp;

/**
 * The initial menu that pops up when the application starts
 *
 * @author Carter Gale
 */
public class WelcomeState extends State {
    @Override
    public void start(ReservationApp app) {
        System.out.println("Sign [U]p    Sign [I]n    E[X]it");
        String input = app.readLine();
        switch (input.charAt(0)) {
            case 'u':
                app.currentState = new SignUpState();
                app.currentState.start(app);
                break;
            case 'i':
                app.currentState = new SignInState();
                app.currentState.start(app);
                break;
            case 'x':
                app.currentState = new ExitState();
                app.currentState.start(app);
                break;
            default:
                System.out.println("Could not understand input: '" + input + "'. Try either U, I, or X.");
                this.start(app);
        }
    }

}
