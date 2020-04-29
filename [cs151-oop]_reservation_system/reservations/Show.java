package reservations;


import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Represents a Show and keeps track of what seats have and have not been reserved as well as the showtime
 *
 * @author Carter Gale
 */
public class Show implements Comparable<Show> {
    private final HashSet<Seat> reservedSeats;

    public final String showID;
    public final int year, month, day;
    public final boolean trueForSixPM;


    public static final String stage =
            "                      ...........50$...........                       \n" +
                    "                      .........................                       \n" +
                    "                               BALCONIES                              \n" +
                    "                      ...........55$...........                       \n" +
                    "                      .........................                       \n" +
                    " B .........                                             ......... B  \n" +
                    " A .........                                             ......... A  \n" +
                    " L .........    .........  ...............  .........    ......... L  \n" +
                    " C .........    .........  ...............  .........    ......... C  \n" +
                    " O .........    .........  ...............  .........    ......... O  \n" +
                    " N ...40$...    ...35$...  ......45$......  ...35$...    ...40$... N  \n" +
                    " Y .........    .........  ...............  .........    ......... Y  \n" +
                    "   .........    .........  ...............  .........    .........    \n" +
                    "   .........    .........  ...............  .........    .........    \n" +
                    "               |--------------Main Floor-------------|                \n" +
                    "                                                                      \n" +
                    "                                STAGE                                 \n";

    public Show(int year, int month, int day, boolean trueForSixPM) {
        this.reservedSeats = new HashSet<>();
        this.year = year;
        this.month = month;
        this.day = day;
        this.trueForSixPM = trueForSixPM;

        showID = toLocalDateTime().toString();
    }

    /**
     * @param showInput the date in the form of dd mm yyyy [6/8]
     * @return the Show with the specified date
     */
    public static Show fromInput(String showInput) {
        String[] s = showInput.split(Pattern.quote(" "));
        int day = Integer.parseInt(s[0]);
        int month = Integer.parseInt(s[1]);
        int year = Integer.parseInt(s[2]);
        int time = Integer.parseInt(s[3]);

        String showID = toLocalDateTime(year, month, day, time == 6).toString();

        Map<String, Show> shows = ReservationApp.instance.shows;
        if (shows.containsKey(showID)) {
            return shows.get(showID);
        }
        Show show = new Show(year, month, day, time == 6);
        shows.put(show.showID, show);
        return show;
    }

    /**
     * Get the show from the specified show id
     * @param showID a show ID, example: 2020-12-29T20:30
     * @return the show with the specified ID
     */
    public static Show fromID(String showID) {
        Map<String, Show> shows = ReservationApp.instance.shows;
        if (shows.containsKey(showID)) {
            return shows.get(showID);
        }

        LocalDateTime parse = LocalDateTime.parse(showID);
        Show show = new Show(parse.getYear(), parse.getMonth().ordinal() + 1, parse.getDayOfMonth(), parse.getHour() == 18);
        shows.put(show.showID, show);

        return show;
    }

    /**
     * Mark a seat as reserved for this show
     * @param seat the seat to be reserved
     */
    public void reserveSeat(Seat seat) {
        if (!isSeatReserved(seat)) {
            reservedSeats.add(seat);
        }
    }

    /**
     * Mark a seat as not-reserved
     * @param seat the seat to be marked as not-reserved
     */
    public void cancelSeat(Seat seat) {
        reservedSeats.remove(seat);
    }

    public boolean isSeatReserved(Seat seat) {
        return reservedSeats.contains(seat);
    }

    //You shouldn't ever have to or need to or want to use this
    public static LocalDateTime toLocalDateTime(int year, int month, int day, boolean trueForSixPM) {
        if (trueForSixPM) {
            return LocalDateTime.of(year, Month.of(month), day, 18, 30);
        } else {
            return LocalDateTime.of(year, Month.of(month), day, 20, 30);
        }
    }

    //You shouldn't ever have to or need to or want to use this
    public LocalDateTime toLocalDateTime() {
        if (trueForSixPM) {
            return LocalDateTime.of(year, Month.of(month), day, 18, 30);
        } else {
            return LocalDateTime.of(year, Month.of(month), day, 20, 30);
        }
    }

    @Override
    public int compareTo(Show o) {
        return this.toLocalDateTime().compareTo(o.toLocalDateTime());
    }
}
