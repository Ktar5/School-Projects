package calendar;

import calendar.states.WelcomeState;

public class MyCalendarTester {
    public static void main(String[] args){
        CalendarApp.instance = new CalendarApp();
        CalendarApp.instance.loadData();
        System.out.println("Finished loading data!");

        CalendarApp.instance.currentState = new WelcomeState();
        CalendarApp.instance.currentState.start(CalendarApp.instance);
    }
}
