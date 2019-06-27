package edu.orbital.studyshack;

public class House {

    public static final int[] HOUSE_IMAGES = {R.drawable.house1, R.drawable.house2,R.drawable.house3,R.drawable.house4,R.drawable.house5};
    private String name;
    private String desc;
    private int level;
    private int totaltime;
    public static final int lvl1time = 0;
    public static final int lvl2time = 120;
    public static final int lvl3time = 180;
    public static final int lvl4time = 240;
    public static final int lvl5time = 300;

    public House(String name, String desc, int level, int totaltime) {
        this.name = name;
        this.desc = desc;
        this.level = level;
        this.totaltime = totaltime;
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
