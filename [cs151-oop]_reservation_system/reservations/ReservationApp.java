package reservations;


import reservations.states.State;
import reservations.states.WelcomeState;
import reservations.user.Reservation;
import reservations.user.Transaction;
import reservations.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The main class for handling data and the application status
 *
 * @author Carter Gale
 */
public class ReservationApp {
    public static ReservationApp instance;

    public static void main(String[] args) {
        ReservationApp.instance = new ReservationApp();
        instance.loadData();

        instance.currentState = new WelcomeState();
        instance.currentState.start(instance);
    }

    //A buffered reader attached to the console for the various inputs needed by the program
    private final BufferedReader reader;

    //The current program state
    public State currentState;

    //Username -> user object
    public final Map<String, User> users;
    //The currently logged-in user, null if none
    public User currentUser;

    //showID -> theatre object
    public final Map<String, Show> shows;

    private ReservationApp() {
        reader = new BufferedReader(new InputStreamReader(java.lang.System.in));
        users = new HashMap<>();
        shows = new HashMap<>();
    }

    private void loadData() {
        File file = new File("reservations.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split(Pattern.quote("$"));
                if(split[0].equals("user")){
                    currentUser = new User(split[1], split[2]);
                    users.put(split[1], currentUser);
                }else{
                    currentUser.loadReservation(split);
                }

                line = reader.readLine();
            }
            currentUser = null;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call this to save all current in-memory data to the drive
     */
    public void saveData() {
        File file = new File("reservations.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        List<String> lines = new ArrayList<>();
        for (User user : users.values()) {
            lines.add("user$" + user.username + "$" + user.password);
            for (Transaction transaction : user.getTransactions()) {
                for (List<Reservation> reservations : transaction.getReservations().values()) {
                    for (Reservation reservation : reservations) {
                        lines.add("resv$" + reservation.transactionID + "$" + reservation.show.showID +"$" + reservation.seat.toString());
                    }
                }
            }
        }

        FileWriter writer;
        try {
            writer = new FileWriter(file);
            for (String str : lines) {
                writer.write(str + java.lang.System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use this to read a line from the console
     * @return the line read from console by user
     */
    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("There was an error with your input.");
            return null;
        }
    }

}
