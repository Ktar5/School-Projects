package reservations.user;

import java.util.*;

/**
 * This class represents a transaction occurring during a signed-in session
 * It has methods for managing reservations during the session
 *
 * @author Carter Gale
 */
public class Transaction {
    /**
     * A unique ID for referencing this transaction
     */
    public final UUID transactionID;

    //Show mapped to the reservations for that show (within this transaction)
    private final Map<String, List<Reservation>> reservations;

    //Every time we add or remove a reservation from the transaction we recalculate this
    private int currentFinalPrice = -1;

    /**
     * For creating a new transaction during runtime, randomly assigns a UUID
     */
    Transaction() {
        this(UUID.randomUUID());
    }

    /**
     * This is the constructor for loading the transaction from memory
     * @param transactionID the transaction ID stored for this transaction
     */
    public Transaction(UUID transactionID) {
        this.transactionID = transactionID;
        reservations = new HashMap<>();
    }

    //We have the boolean recalculatePrice because when loading reservations we don't want to
    // recalculate the price with every single new loaded reservation
    void addReservation(Reservation reservation, boolean recalculatePrice) {
        if (reservations.containsKey(reservation.show.showID)) {
            reservations.get(reservation.show.showID).add(reservation);
        } else {
            ArrayList<Reservation> reservationsMapList = new ArrayList<>();
            reservationsMapList.add(reservation);
            reservations.put(reservation.show.showID, reservationsMapList);
        }

        if (recalculatePrice) {
            calculatePrice();
        }
    }

    void removeReservation(Reservation reservation) {
        reservation.show.cancelSeat(reservation.seat);
        if (reservations.containsKey(reservation.show.showID)) {
            reservations.get(reservation.show.showID).remove(reservation);
        }
        calculatePrice();
    }

    /**
     * This takes all the reservations made during this transaction and merges them
     * into a similarly structured map for further processing.
     *
     * @param mergeInto the map that the reservations from this transaction should
     *                  be merged into
     */
    public void mergeReservationsInto(Map<String, List<Reservation>> mergeInto) {
        for (Map.Entry<String, List<Reservation>> entry : reservations.entrySet()) {
            if (mergeInto.containsKey(entry.getKey())) {
                mergeInto.get(entry.getKey()).addAll(entry.getValue());
            } else {
                mergeInto.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
        }
    }


    //Could have made this only recalculate the discounts of whatever was modified, but instead
    // we recalculate all discounts within this transaction because it is easier to code and
    // in this application it doesn't need insane speed with billions of seats or anything
    /**
     * Calculates the price (including all discounts) for only this transaction
     * Can be called to re-calculate the prices
     *
     * @return a HashMap that maps the showID to the price of that show including discounts
     */
    public Map<String, Integer> calculatePrice() {
        HashMap<String, Integer> showPriceData = new HashMap<>();
        for (Map.Entry<String, List<Reservation>> entry : reservations.entrySet()) {
            List<Reservation> singleShowReservations = entry.getValue();
            int price = 0;

            if (singleShowReservations.isEmpty()) {
                continue;
            }

            Reservation reservation = singleShowReservations.get(0);

            if (reservation.show.month == 12 && (reservation.show.day == 26 || reservation.show.day == 27)) {
                price += singleShowReservations.size() * 20;
            } else if (singleShowReservations.size() >= 11) {
                for (Reservation res : singleShowReservations) {
                    price += res.seat.basePrice - 5;
                }
            } else if (singleShowReservations.size() >= 5) {
                for (Reservation res : singleShowReservations) {
                    price += res.seat.basePrice - 2;
                }
            } else {
                for (Reservation res : singleShowReservations) {
                    price += res.seat.basePrice;
                }
            }
            showPriceData.put(entry.getKey(), price);
        }

        currentFinalPrice = 0;
        for (Integer value : showPriceData.values()) {
            currentFinalPrice += value;
        }

        return showPriceData;
    }


    /**
     * Returns the reservations map
     * @return the reservations map
     */
    public Map<String, List<Reservation>> getReservations() {
        return reservations;
    }

    /**
     * @return the current, final, discount included price
     */
    public int getCurrentFinalPrice() {
        if(currentFinalPrice == -1){
            calculatePrice();
        }
        return currentFinalPrice;
    }
}