package reservations.states;

import reservations.ReservationApp;
import reservations.user.Reservation;
import reservations.user.Transaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * This state calculates the end of a transaction and prints out a confirmation receipt
 * then signs the user out before returning to the welcome screen
 *
 * @author Carter Gale
 */
public class OutState extends State {
    @Override
    public void start(ReservationApp app) {
        System.out.println("Completing transaction...");
        Transaction transaction = app.currentUser.getCurrentTransaction();
        app.currentUser.endTransaction();
        if(transaction == null){
            System.out.println("Empty transaction... Quitting");
            app.currentUser = null;
            app.currentState = new WelcomeState();
            app.currentState.start(app);
            return;
        }

        String confirmationNumber = transaction.transactionID.toString();
        Map<String, Integer> pricePerShow = transaction.calculatePrice();
        Map<String, List<Reservation>> reservations = transaction.getReservations();

        ArrayList<String> sortedShowIDs = new ArrayList<>(reservations.keySet());
        sortedShowIDs.sort(Comparator.comparing(app.shows::get));

        System.out.println("Confirmation #: " + confirmationNumber);
        StringBuilder builder = new StringBuilder();
        for (String showID : sortedShowIDs) {
            builder.append("\n").append(showID).append(" Price: ").append(pricePerShow.get(showID)).append(" Seats: ");
            for (Reservation reservation : reservations.get(showID)) {
                builder.append(reservation.seat.toString()).append(", ");
            }
            builder.delete(builder.length() - 2, builder.length());
        }
        System.out.println(builder.toString());
        System.out.println("Total Price: " + transaction.getCurrentFinalPrice());
        System.out.println("Press enter to continue..");
        app.readLine();
        app.currentUser = null;
        app.currentState = new WelcomeState();
        app.currentState.start(app);
    }
}
