package com.example.jessie.focusing.Model;

import static android.text.TextUtils.isEmpty;

/**
 * @author : Yujie Lyu
 */
public enum WeekDays {
    SUNDAY("Sun.", 0b0000001),
    MONDAY("Mon.", 0b0000010),
    TUESDAY("Tue.", 0b0000100),
    WEDNESDAY("Wed.", 0b0001000),
    THURSDAY("Thu.", 0b0010000),
    FRIDAY("Fri.", 0b0100000),
    SATURDAY("Sat.", 0b1000000);

    private String name;
    private int value;

    WeekDays(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static String[] names() {
        String[] res = new String[values().length];
        WeekDays[] weekDays = values();
        for (int i = 0; i < weekDays.length; i++) {
            WeekDays day = weekDays[i];
            res[i] = day.name;
        }
        return res;
    }

    public static boolean[] toChoices(int value) {
        WeekDays[] weekDays = values();
        boolean[] res = new boolean[weekDays.length];
        for (int i = 0; i < weekDays.length; i++) {
            WeekDays day = weekDays[i];
            res[i] = day.isChosen(value);
        }
        return res;
    }

    public static String toString(int value) {
        WeekDays[] weekDays = values();
        StringBuilder res = new StringBuilder();
        for (WeekDays day : weekDays) {
            if (day.isChosen(value)) {
                res.append(day).append(' ');
            }
        }
        String resStr = res.toString().trim();
        return isEmpty(resStr) ? "None" : resStr;
    }

    public static String toString(boolean[] choices) {
        WeekDays[] weekDays = values();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < weekDays.length; i++) {
            if (choices[i]) {
                WeekDays day = weekDays[i];
                res.append(day).append(' ');
            }
        }
        String resStr = res.toString().trim();
        return isEmpty(resStr) ? "None" : resStr;
    }

    public static int toValue(boolean[] choices) {
        int res = 0;
        for (int i = 0; i < choices.length; i++) {
            if (choices[i]) {
                res |= values()[i].value;
            }
        }
        return res;
    }

    public static WeekDays getDay(int dayOfWeek) {
        return values()[dayOfWeek - 1];
    }

    public static int getValue(int dayOfWeek) {
        return getDay(dayOfWeek).value;
    }

    public boolean isChosen(int value) {
        return this.value == (this.value & value);
    }

    public int getDayOfWeek() {
        return ordinal() + 1;
    }

    @Override
    public String toString() {
        return name;
    }

}
