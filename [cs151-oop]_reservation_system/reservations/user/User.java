package reservations.user;

import reservations.Seat;
import reservations.Show;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * All the relevant data and methods for managing a user and their reservations/transactions
 *
 * @author Carter Gale
 */
public class User {
    //Notes:
    //Transactions keep track of discounts because multiple transactions don't share discounts
    //Reservations for one show may include multiple transactions
    private final Map<UUID, Transaction> transactions;
    public final String username, password;

    //null if there is no current transaction
    private UUID currentTransaction;

    //For creating a new user
    public User(String username, String password) {
        this.username = username.toLowerCase();
        this.password = password.toLowerCase();
        transactions = new HashMap<>();
    }

    /**
     * Starts a "transaction session" by creating a new transaction, and setting it as the current one
     */
    public void startTransaction() {
        if(currentTransaction != null){
            transactions.get(currentTransaction);
            return;
        }
        Transaction transaction = new Transaction();
        transactions.put(transaction.transactionID, transaction);
        currentTransaction = transaction.transactionID;
    }

    /**
     * Ends a "transaction session" by setting the currentTransaction to null
     */
    public void endTransaction() {
        currentTransaction = null;
    }

    /**
     * @return returns the  Transaction object that is the current transaction
     */
    public Transaction getCurrentTransaction(){
        return transactions.get(currentTransaction);
    }

    /**
     * Creates a reservation for the current transaction with the specified seat and show
     * Note that this method does not make any checks for if the seat is available, and it used only
     * by the inside piece of code, you shouldn't ever need to use this method.
     *
     * @param show the show to make the reservation for
     * @param seat the seat to make the reservation for
     */
    public void makeReservation(Show show, Seat seat) {
        transactions.get(currentTransaction).addReservation(
                new Reservation(show, seat, currentTransaction)
                , true
        );
    }

    /**
     * Only used by the ReservationApp class to load reservations from the serialized data
     * @param data the data split by dollar signs ($)
     */
    public void loadReservation(String[] data){
        UUID transactionID = UUID.fromString(data[1]);
        Show show = Show.fromID(data[2]);
        Seat seat = new Seat(data[3]);
        Reservation reservation = new Reservation(show, seat, transactionID);
        if(!transactions.containsKey(transactionID)){
            transactions.put(transactionID, new Transaction(transactionID));
        }
        transactions.get(transactionID).addReservation(reservation, false);
    }

    public Collection<Transaction> getTransactions(){
        return transactions.values();
    }

    /**
     * Cancel the specified reservation
     * @param reservation the reservation to cancel
     */
    public void cancelReservation(Reservation reservation) {
        transactions.get(reservation.transactionID).removeReservation(reservation);
    }


}
