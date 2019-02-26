package com.example.jessie.focusing.Model;

import android.util.Log;

import com.example.jessie.focusing.Utils.TimeHelper;

import org.litepal.LitePal;

import java.util.Calendar;

import static com.example.jessie.focusing.Utils.AppConstants.STATS_DAYS;
import static com.example.jessie.focusing.Utils.TimeHelper.HOUR_IN_MILLIS;
import static com.example.jessie.focusing.Utils.TimeHelper.toMillis;

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
        a.setTime(toMillis(20, 0), toMillis(21, 0));
        a.setRepeatId(0);
        a.saveAsync().listen(SeedData::onFinish);
        Profile b = new Profile();
        b.setProfileName("Meeting");
        b.setTime(toMillis(10, 0), toMillis(12, 0));
        b.setRepeatId(0x0000011);
        b.saveAsync().listen(SeedData::onFinish);
    }

    private static void initializeAppUsage() {
        int count = LitePal.count(AppUsage.class);
        if (count > 0) {
            return;
        }
        dataType = AppUsage.class.getSimpleName();
        for (int numOfDay = 1; numOfDay < STATS_DAYS; numOfDay++) {
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
        for (int numOfDay = 1; numOfDay < STATS_DAYS; numOfDay++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1 * numOfDay);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.HOUR_OF_DAY, (int) (24 * Math.random()));
            long start = calendar.getTimeInMillis();
            long end = (long) (start + HOUR_IN_MILLIS * 2 * Math.random());
            FocusTimeStats focusTimeStats = new FocusTimeStats(start, end, year, month, day);
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
                "com.google.android.apps.maps",
                "com.google.android.videos",
                "com.google.android.music",
                "com.google.android.youtube",
                "com.android.chrome"
        };
    }
}
