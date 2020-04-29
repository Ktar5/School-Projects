package reservations;

/**
 * Represents a seat and all the functions along with validating and pricing that seat
 *
 * @author Carter Gale
 */
public class Seat {
    /**
     * The seat location in the form of "m" "sb" "wb" or "eb"
     */
    public final String seatLocation;

    /**
     * The seat number (ex 100)
     */
    public final int seatNumber;

    /**
     * The base price of the seat without  any discounts applied
     */
    public final int basePrice;

    public Seat(String seatLocation, int seatNumber) {
        this.seatLocation = seatLocation.toLowerCase();
        this.seatNumber = seatNumber;
        this.basePrice = calculateBasePrice();
        if (!isValidSeat()) {
            throw new RuntimeException("Could not validate seats: " + seatLocation + "" + seatNumber);
        }
    }

    /**
     * Construct a new seat
     * @param seatID for example "m104"
     */
    public Seat(String seatID) {
        if (seatID.startsWith("m")) {
            seatLocation = "m";
            seatNumber = Integer.parseInt(seatID.replace("m", ""));
        } else if (seatID.startsWith("sb")) {
            seatLocation = "sb";
            seatNumber = Integer.parseInt(seatID.replace("sb", ""));
        } else if (seatID.startsWith("wb")) {
            seatLocation = "wb";
            seatNumber = Integer.parseInt(seatID.replace("wb", ""));
        } else if (seatID.startsWith("eb")) {
            seatLocation = "eb";
            seatNumber = Integer.parseInt(seatID.replace("eb", ""));
        } else {
            throw new RuntimeException("Could not validate seat: " + seatID);
        }

        if (!isValidSeat()) {
            throw new RuntimeException("Could not validate seats: " + seatLocation + "" + seatNumber);
        }
        this.basePrice = calculateBasePrice();
    }

    /**
     * Checks if the seat is a valid seat
     * @return true if it is a valid seat
     */
    public boolean isValidSeat() {
        if (seatLocation == null || seatNumber == 0) {
            return false;
        }
        switch (seatLocation) {
            case "m":
                return seatNumber > 0 && seatNumber <= 150;
            case "sb":
                return seatNumber > 0 && seatNumber <= 50;
            case "wb":
            case "eb":
                return seatNumber > 0 && seatNumber <= 100;
        }
        return false;
    }

    private int calculateBasePrice() {
        switch (seatLocation) {
            case "m":
                if (seatNumber > 100) {
                    return 45;
                } else {
                    return 35;
                }
            case "sb":
                if (seatNumber > 25) {
                    return 55;
                } else {
                    return 50;
                }
            case "wb":
            case "eb":
                return 40;
            default:
                throw new IllegalStateException("Unexpected value: " + seatLocation.toLowerCase());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return seatNumber == seat.seatNumber &&
                seatLocation.equals(seat.seatLocation);
    }

    @Override
    public String toString() {
        return seatLocation + "" + seatNumber;
    }

    @Override
    public int hashCode() {
        return (seatLocation + seatNumber).hashCode();
    }
}
