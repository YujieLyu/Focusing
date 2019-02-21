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

    /**
     * The constructor of {@link WeekDays}
     *
     * @param name  the displayed name
     * @param value the binary value for database convenience.
     */
    WeekDays(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @return the String array of week days
     */
    public static String[] names() {
        String[] res = new String[values().length];
        WeekDays[] weekDays = values();
        for (int i = 0; i < weekDays.length; i++) {
            WeekDays day = weekDays[i];
            res[i] = day.name;
        }
        return res;
    }

    /**
     * Convert the binary value to boolean array
     *
     * @param value
     * @return
     */
    public static boolean[] toChoices(int value) {
        WeekDays[] weekDays = values();
        boolean[] res = new boolean[weekDays.length];
        for (int i = 0; i < weekDays.length; i++) {
            WeekDays day = weekDays[i];
            res[i] = day.isChosen(value);
        }
        return res;
    }

    /**
     * Get strings of {@link WeekDays} via their binary value.
     *
     * @param value
     * @return
     * @see #toString(boolean[])
     */
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

    /**
     * Get strings of ({@link WeekDays} via their boolean value.
     *
     * @param choices
     * @return
     * @see #toString(int)
     */
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

    /**
     * Convert boolean array to binary value.
     * for database convenience.
     *
     * @param choices
     * @return
     */
    public static int toValue(boolean[] choices) {
        int res = 0;
        for (int i = 0; i < choices.length; i++) {
            if (choices[i]) {
                res |= values()[i].value;
            }
        }
        return res;
    }

    /**
     * Get {@link WeekDays} by {@link java.util.Calendar#DAY_OF_WEEK}
     *
     * @param dayOfWeek
     * @return
     */
    public static WeekDays getDay(int dayOfWeek) {
        return values()[dayOfWeek - 1];
    }

    /**
     * Get the binary value of specific {@link WeekDays}
     *
     * @param dayOfWeek
     * @return
     * @see #getDay(int)
     */
    public static int getValue(int dayOfWeek) {
        return getDay(dayOfWeek).value;
    }

    /**
     * Check if this day is chosen by users.
     *
     * @param value {@link Profile#repeatId}
     * @return
     */
    public boolean isChosen(int value) {
        return this.value == (this.value & value);
    }

    /**
     * Get {@link java.util.Calendar#DAY_OF_WEEK} value
     *
     * @return
     */
    public int getDayOfWeek() {
        return ordinal() + 1;
    }

    @Override
    public String toString() {
        return name;
    }

}
