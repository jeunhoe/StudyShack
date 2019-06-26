package edu.orbital.studyshack;

public class House {
    private String name;
    private String desc;
    private int level;
    private int totaltime;
    public static final int lvl1time = 60;
    public static final int lvl2time = 120;
    public static final int lvl3time = 180;
    public static final int lvl4time = 240;
    public static final int lvl5time = 300;

    public House(String name, String desc, int level, int totaltime) {
        name = name;
        desc = desc;
        level = level;
        totaltime = totaltime;
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
        return totaltime;
    }

    public void addTime(int min) {
        totaltime += min;
    }

}
