package reservations.states;


import reservations.ReservationApp;
import reservations.Seat;
import reservations.Show;
import reservations.user.Reservation;
import reservations.user.Transaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This state represents the "Cancel" menu and has the goal of allowing the user
 * to cancel any reservations that exist
 *
 * @author Carter Gale
 */
public class CancelState extends State {


    @Override
    public void start(ReservationApp app) {
        System.out.println("What date would you like to cancel your reservation for? Please type in the following format:");
        System.out.println("dd mm yyyy [6 for 6:30pm or 8 for 8:30pm] ::: ex: 25 12 2020 6");
        Show show = Show.fromInput(app.readLine());

        Collection<Transaction> transactions = app.currentUser.getTransactions();
        Map<String, List<Reservation>> reservations = new HashMap<>();
        for (Transaction transaction : transactions) {
            transaction.mergeReservationsInto(reservations);
        }

        if (show == null) {
            System.out.println("ERROR! Show is invalid, please try again!");
            start(ReservationApp.instance);
        } else if (!reservations.containsKey(show.showID)) {
            System.out.println("You don't have any reservations for that show! Try again.");
            start(ReservationApp.instance);
        } else {
            cancelSeat(reservations.get(show.showID));
        }
    }

    private void cancelSeat(List<Reservation> reservations) {
        System.out.println("These are the seats available to cancel for the show: ");
        StringBuilder builder = new StringBuilder();
        for (Reservation reservation : reservations) {
            builder.append(reservation.seat.toString()).append(", ");
        }
        builder.delete(builder.length() - 2, builder.length());
        System.out.println(builder.toString());
        System.out.println("Please type the seat that you'd like to cancel: ");
        String input = ReservationApp.instance.readLine().toLowerCase();
        Seat seatToCancel = new Seat(input);
        for (Reservation reservation : reservations) {
            if(reservation.seat.equals(seatToCancel)){
                ReservationApp.instance.currentUser.cancelReservation(reservation);
                System.out.println("Reservation successfully cancelled!");
                break;
            }
        }
        System.out.println("Would you like to cancel another reservation? [Y/N]");
        input = ReservationApp.instance.readLine();
        if(input.equalsIgnoreCase("y")){
            start(ReservationApp.instance);
        }else{
            ReservationApp.instance.currentState = new TransactionState();
            ReservationApp.instance.currentState.start(ReservationApp.instance);
        }
    }
}
