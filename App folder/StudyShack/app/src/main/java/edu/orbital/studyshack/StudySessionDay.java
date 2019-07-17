package edu.orbital.studyshack;

public class StudySessionDay extends StudySession {
    int day;

    public StudySessionDay(int day, int hour, int minute, int timeSpent) {
        super(hour, minute, timeSpent);
        this.day = day;
    }

    public boolean exceedsDay(){
        return ((this.getHour() * 60) +  this.getMinute() + this.getTimeSpent()) > 1440;
    }

    public int clockDay() {
        int timeFirstDay = 1440 - ((this.getHour() * 60) +  this.getMinute());
        this.timeSpent -= timeFirstDay;
        return timeFirstDay;
    }

    public int getDay() {
        return this.day - 1;
    }

}
