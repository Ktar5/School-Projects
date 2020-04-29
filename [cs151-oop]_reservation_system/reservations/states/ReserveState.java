package reservations.states;


import reservations.ReservationApp;
import reservations.Seat;
import reservations.Show;

/**
 * This state is for allowing the user to make reservations given dates and seats
 *
 * @author Carter Gale
 */
public class ReserveState extends State {
    @Override
    public void start(ReservationApp app) {
        System.out.println("Please type the date of the show you wish to see in the following format");
        System.out.println("dd mm yyyy [6 for 6:30pm or 8 for 8:30pm] ::: ex: 25 12 2020 6");
        Show show = Show.fromInput(app.readLine());
        if(show == null){
            System.out.println("ERROR! Show is invalid, please try again!");
            start(ReservationApp.instance);
        }else{
            reserveForShow(show);
        }
    }

    private void reserveForShow(Show show) {
        System.out.println("Available seats:");
        StringBuilder builder = new StringBuilder();
        builder.append("Main Floor (m): ");
        for (int i = 1; i <= 150; i++) {
            if(!show.isSeatReserved(new Seat("m", i))){
                builder.append(i).append(", ");
            }
        }
        builder.append("\nSouth Balcony (sb): ");
        for (int i = 1; i <= 50; i++) {
            if(!show.isSeatReserved(new Seat("sb", i))){
                builder.append(i).append(", ");
            }
        }
        builder.append("\nWest Balcony (wb): ");
        for (int i = 1; i <= 100; i++) {
            if(!show.isSeatReserved(new Seat("wb", i))){
                builder.append(i).append(", ");
            }
        }
        builder.append("\nEast Balcony (eb): ");
        for (int i = 1; i <= 100; i++) {
            if(!show.isSeatReserved(new Seat("eb", i))){
                builder.append(i).append(", ");
            }
        }
        System.out.println(builder.toString());
        System.out.println("Please type the reservation you'd like to make from the above seats. In the form of [location abbreviation][number] EX: m134");
        String input = ReservationApp.instance.readLine();
        Seat seat = new Seat(input);
        if(show.isSeatReserved(seat)){
            System.out.println("Seat already reserved please try again.");
            reserveForShow(show);
            return;
        }
        ReservationApp.instance.currentUser.makeReservation(show, seat);
        System.out.println("Success! Want to make another reservation? [Y/N]");
        String s = ReservationApp.instance.readLine();
        if(s.equalsIgnoreCase("y")){
            reserveForShow(show);
        }else{
            ReservationApp.instance.currentState = new TransactionState();
            ReservationApp.instance.currentState.start(ReservationApp.instance);
        }
    }


}
