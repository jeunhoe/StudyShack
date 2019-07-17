package edu.orbital.studyshack;

import java.util.Calendar;

public class StudySessionMonth extends StudySession {
    private Calendar calendar;
    private int year;
    private int month;
    private int date;

    public StudySessionMonth(int date, int month, int year, int hour, int minute, int timeSpent) {
        super(hour, minute, timeSpent);
        this.date = date;
        this.year = year;
        this.month = month;
        this.calendar = Calendar.getInstance();
        calendar.set(year, month, date);
    }

    public boolean exceedsMonth() {
        int numMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int maxTimeInMonth = numMaxDays * 1440;
        return ((this.date - 1) * 1440) + (this.getHour() * 60) + this.getMinute() + timeSpent > maxTimeInMonth;
    }

    public int clockMonth() {
        int numMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int maxTimeInMonth = numMaxDays * 1440;
        int timeToReturn = maxTimeInMonth - (((this.date - 1) * 1440) + (this.getHour() * 60) + this.getMinute());
        this.timeSpent -= timeToReturn;
        return timeToReturn;
    }

    public int getMonth() {
        return this.month;
    }


}
