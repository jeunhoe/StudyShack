package edu.orbital.studyshack;

public class House {

    public static final int[] HOUSE_IMAGES = {R.drawable.house1, R.drawable.house2,R.drawable.house3,R.drawable.house4,R.drawable.house5};
    private String name;
    private String desc;
    private int level;
    private int totalTime;
    private int totalTimeWeek;
    public static final int lvl1time = 0;
    public static final int lvl2time = 1;
    public static final int lvl3time = 2;
    public static final int lvl4time = 4;
    public static final int lvl5time = Integer.MAX_VALUE;

    public House(String name, String desc, int level, int totaltime) {
        this.name = name;
        this.desc = desc;
        this.level = level;
        this.totalTime = totaltime;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getLevel() {
        return level;
    }

    public int getTotaltime() {
        return totalTime;
    }

    public int getTotalTimeWeek() {
        return totalTimeWeek;
    }

    public void addTotalTime(int min) {
        totalTime += min;
    }

    public void addWeekTime(int min) {
        totalTimeWeek += min;
    }

    public static int timeLimit(int lvl){
        int result = 0;
        switch (lvl) {
            case 1 :
                result = House.lvl1time;
                break;
            case 2 :
                result = House.lvl2time;
            break;
            case 3 :
                result = House.lvl3time;
            break;
            case 4 :
                result = House.lvl4time;
            break;
            case 5 :
                result = House.lvl5time;
            break;
        }

        return result;
    }

}
