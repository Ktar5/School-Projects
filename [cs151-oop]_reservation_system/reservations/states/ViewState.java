package reservations.states;


import reservations.ReservationApp;
import reservations.user.Reservation;
import reservations.user.Transaction;

import java.util.*;

/**
 * View all reservations you have made
 *
 * @author Carter Gale
 */
public class ViewState extends State {
    @Override
    public void start(ReservationApp app) {
        System.out.println("List of reservations you have made:");
        Collection<Transaction> transactions = app.currentUser.getTransactions();
        Map<String, List<Reservation>> reservations = new HashMap<>();
        for (Transaction transaction : transactions) {
            transaction.mergeReservationsInto(reservations);
        }
        ArrayList<String> sortedShowIDs = new ArrayList<>(reservations.keySet());
        sortedShowIDs.sort(Comparator.comparing(app.shows::get));

        StringBuilder builder = new StringBuilder();
        for (String showID : sortedShowIDs) {
            builder.append("\n").append(showID).append(": ");
            for (Reservation reservation : reservations.get(showID)) {
                builder.append(reservation.seat.toString()).append(", ");
            }
            builder.delete(builder.length() - 2, builder.length());
        }
        System.out.println(builder.toString());
        System.out.println("Press any button to return to previous menu.");
        ReservationApp.instance.readLine();
        app.currentState = new TransactionState();
        app.currentState.start(app);
    }
}
