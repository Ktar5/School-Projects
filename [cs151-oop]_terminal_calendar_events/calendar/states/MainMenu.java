package calendar.states;

import calendar.CalendarApp;

/**
 * Displays the main menu to the user
 * @author Carter Gale
 */
public class MainMenu extends State{
    @Override
    public void start(CalendarApp app) {
        System.out.println("[V]iew by  [C]reate, [G]o to [E]vent list [D]elete  [Q]uit");
        String input = app.readLine().toLowerCase();
        switch (input.charAt(0)) {
            case 'v':
                app.currentState = new ViewBySelectionState();
                app.currentState.start(app);
                break;
            case 'c':
                app.currentState = new CreateEvent();
                app.currentState.start(app);
                break;
            case 'g':
                app.currentState = new GoToState();
                app.currentState.start(app);
                break;
            case 'e':
                app.currentState = new EventList();
                app.currentState.start(app);
                break;
            case 'd':
                app.currentState = new DeleteEvent();
                app.currentState.start(app);
                break;
            case 'q':
                app.currentState = new Quit();
                app.currentState.start(app);
                break;
            default:
                System.out.println("Could not understand input: '" + input + "'. Try either V, C, G, E, D, or Q.");
                this.start(app);
        }
        app.currentState = new MainMenu();
        app.currentState.start(app);
    }
}
