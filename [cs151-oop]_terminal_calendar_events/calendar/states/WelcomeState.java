package calendar.states;

import calendar.CalendarApp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;

/**
 * The initial menu that pops up when the application starts
 *
 * @author Carter Gale
 */
public class WelcomeState extends State {

    //This prints out the starting calendar
    @Override
    public void start(CalendarApp app) {
        LocalDate cal = LocalDate.now(); // capture today
        System.out.println(cal.getMonth() + " " + cal.getYear());
        System.out.println("Su Mo Tu We Th Fr Sa");
        LocalDate dayIteration = LocalDate.of(cal.getYear(), cal.getMonth(), 1);
        StringBuilder builder = new StringBuilder();
        DayOfWeek[] days = {DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};

        //Add the necessary spaces in the first week of the month
        for (DayOfWeek value : days) {
            if (!dayIteration.getDayOfWeek().equals(value)) {
                builder.append("   ");
            } else {
                break;
            }
        }
        //Start adding numbers
        while (dayIteration.getMonth().equals(cal.getMonth())) {
            //Add opening bracket if it is the current day of the month
            if(cal.getDayOfMonth() == dayIteration.getDayOfMonth()){
                builder.append("[");
            }
            //Print the number itself
            builder.append(dayIteration.getDayOfMonth());
            //Add the closing bracket
            if(cal.getDayOfMonth() == dayIteration.getDayOfMonth()){
                builder.append("]");
            }
            //Do the correct amount of spacing after the number printout to make everything nicely formatted
            if(dayIteration.getDayOfMonth() <= 9){
                builder.append("  ");
            }else{
                builder.append(" ");
            }

            if(dayIteration.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
                builder.append("\n");
            }

            dayIteration = dayIteration.plus(Period.ofDays(1));
        }
        System.out.println(builder.toString());
        app.currentState = new MainMenu();
        app.currentState.start(app);
    }

}
