package edu.orbital.studyshack;

public class StudySession {
    private int hour;
    private int minute;
    private int timeSpent;

    public StudySession(int hour, int minute, int timeSpent) {
        this.hour = hour;
        this.minute = minute;
        this.timeSpent = timeSpent;
    }

    public void incrementHour() {
        this.hour++;
    }

    public boolean exceedsFirstHour() {
        return (timeSpent + minute) > 60;
    }

    public boolean exceedsSecondHour() {
        return timeSpent > 60;
    }

    public int clockFirstHour() {
        int timeSpentFirstHour = 60 - minute;
        timeSpent = timeSpent - timeSpentFirstHour;
        hour++;
        return timeSpentFirstHour;
    }

    public int clockSecondHour() {
        timeSpent = timeSpent - 60;
        hour++;
        return 60;
    }

    public int clockRemaining() {
        return timeSpent;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

}
