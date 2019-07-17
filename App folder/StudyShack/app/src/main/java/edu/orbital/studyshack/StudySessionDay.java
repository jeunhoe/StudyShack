package edu.orbital.studyshack;

public class StudySessionDay extends StudySession {
    int date;

    public StudySessionDay(int date, int hour, int minute, int timeSpent) {
        super(hour, minute, timeSpent);
        this.date = date;
    }

    public boolean exceedsDay(){
        return ((this.getHour() * 60) +  this.getMinute() + this.getTimeSpent()) > 1440;
    }

    public int clockDay() {
        int timeFirstDay = 1440 - ((this.getHour() * 60) +  this.getMinute());
        this.timeSpent -= timeFirstDay;
        return timeFirstDay;
    }

    // Changes from 1-7 and 1-31 depending on usage
    public int getDay() {
        return this.date - 1;
    }

}
