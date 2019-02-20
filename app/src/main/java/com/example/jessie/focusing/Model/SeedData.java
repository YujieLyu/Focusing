package com.example.jessie.focusing.Model;

import android.util.Log;

import com.example.jessie.focusing.Utils.TimeHelper;

import org.litepal.LitePal;

import static com.example.jessie.focusing.Utils.AppConstants.STATS_DAYS;

/**
 * @author : Yujie Lyu
 * Put seed data into database for alpha test
 */
public class SeedData {
    private static final String TAG = SeedData.class.getSimpleName();
    private static String dataType;

    public static void initialize() {
        initializeProfile();
        initializeAppUsage();
        initializeFocusTime();
    }

    private static void initializeProfile() {
        int count = LitePal.count(Profile.class);
        if (count > 0) {
            return;
        }
        dataType = Profile.class.getSimpleName();
        Profile a = new Profile();
        a.setProfileName("Reading");
        a.setStartHour(20);
        a.setStartMin(0);
        a.setEndHour(21);
        a.setStartMin(0);
        a.setRepeatId(0);
        a.saveAsync().listen(SeedData::onFinish);
        Profile b = new Profile();
        b.setProfileName("Meeting");
        b.setStartHour(10);
        b.setStartMin(0);
        b.setEndHour(12);
        b.setEndMin(0);
        b.setRepeatId(4);
        b.saveAsync().listen(SeedData::onFinish);
    }

    private static void initializeAppUsage() {
        int count = LitePal.count(AppUsage.class);
        if (count > 0) {
            return;
        }
        dataType = AppUsage.class.getSimpleName();
        for (int numOfDay = 0; numOfDay < STATS_DAYS; numOfDay++) {
            int[] date = TimeHelper.getYearMonthDay(numOfDay);
            for (String packageName : getPackageNames()) {
                AppUsage appUsage = new AppUsage(packageName, date[0], date[1], date[2]);
                long usedTime = (long) (30 * 60 * 1000 * Math.random());
                appUsage.setUsedTime(usedTime, false);
                int openTimes = (int) (10 * Math.random());
                appUsage.setOpenTimes(openTimes, true);
                openTimes = (int) (10 * Math.random());
                appUsage.setOpenTimes(openTimes, false);
                appUsage.saveAsync().listen(SeedData::onFinish);
            }
        }
    }

    private static void initializeFocusTime() {
        int count = LitePal.count(FocusTimeStats.class);
        if (count > 0) {
            return;
        }
        dataType = FocusTimeStats.class.getSimpleName();
        for (int numOfDay = 0; numOfDay < STATS_DAYS; numOfDay++) {
            long time = (long) (2 * 60 * 60 * 1000 * Math.random());
            int[] date = TimeHelper.getYearMonthDay(numOfDay);
            FocusTimeStats focusTimeStats = new FocusTimeStats(time, date[0], date[1], date[2]);
            focusTimeStats.saveAsync().listen(SeedData::onFinish);
        }
    }

    private static void onFinish(boolean success) {
        if (success) {
            Log.i(TAG, String.format("Initialize [%s] seed data successfully.", dataType));
        } else {
            Log.e(TAG, String.format("Seed data [%s] cannot be saved.", dataType));
        }
    }

    private static String[] getPackageNames() {
        return new String[]{
                //TODO: use the packages installed in system
                "com.spotify.music",
                "com.instagram.android",
                "com.ticktick.task",
                "com.airbnb.android",
                "com.android.chrome"
        };
    }
}
