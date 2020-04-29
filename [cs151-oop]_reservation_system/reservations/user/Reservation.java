package reservations.user;


import reservations.Seat;
import reservations.Show;

import java.util.UUID;

/**
 * A data class to store transaction ID, show, and seat -- the 3 components of a reservation
 *
 * @author Carter Gale
 */
public class Reservation {
    /**
     * The transaction ID that corresponds to the transaction this reservation took place during
     */
    public final UUID transactionID;

    /**
     * The show that this reservation is scheduled for
     */
    public final Show show;

    /**
     * The seat that is reserved
     */
    public final Seat seat;

    Reservation(Show show, Seat seat, UUID transactionID) {
        this.transactionID = transactionID;
        this.show = show;
        this.seat = seat;
        show.reserveSeat(seat);
    }

}
