package com.example.jessie.focusing.Model;

import android.content.ContentValues;
import android.util.Log;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.jessie.focusing.Utils.TimeHelper.toMillis;

/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 18:13
 */
public class AppInfoManager {
    private static final String TAG = AppInfoManager.class.getSimpleName();

    /**
     * Synchronize the data from system and database
     *
     * @param appInfosSys the apps info from system
     * @param profileId   the profile id
     * @return the apps with lock info and ids from database
     */
    public synchronized static List<AppInfo> syncData(List<AppInfo> appInfosSys, int profileId) {
        if (profileId == Profile.START_NOW_PROFILE_ID) {
            for (AppInfo appInfo : appInfosSys) {
                appInfo.setProfId(Profile.START_NOW_PROFILE_ID);
            }
            return appInfosSys;
        }
        List<AppInfo> appInfosDb = AppInfo.findAllByProfileId(profileId);
        if (appInfosDb.isEmpty()) {
            for (AppInfo appInfo : appInfosSys) {
                appInfo.setProfId(profileId);
            }
            return appInfosSys;
        }
        List<AppInfo> res = new ArrayList<>();
        Map<String, AppInfo> appSysMap = new HashMap<>();
        for (AppInfo info : appInfosSys) {
            appSysMap.put(info.getPackageName(), info);
        }
        for (AppInfo info : appInfosDb) {
            AppInfo appInfo = appSysMap.get(info.getPackageName());
            if (appInfo == null) {
                info.delete();
                continue;
            }
            appInfo.setId(info.getId());
            appInfo.setProfId(profileId);
            appInfo.setLocked(info.isLocked());
            res.add(appInfo);
        }
        return res;
    }

    /**
     * Delete by profId
     *
     * @param profId profile ID
     */
    public synchronized void deleteByProfId(int profId) {
        int rows = LitePal.deleteAll(AppInfo.class, "profid = ?", String.valueOf(profId));
        Log.i(TAG, "Delete by profile id, Rows effected: " + rows);
    }

    /**
     * Returns the latest end time in the apps to be locked.
     *
     * @param toLockApps the app list to be locked.
     * @return the latest end time
     */
    public long getLatestEndTime(List<AppInfo> toLockApps) {
        long endTime = Long.MIN_VALUE;
        for (AppInfo app : toLockApps) {
            Profile profile = Profile.findById(app.getProfId());
            if (profile == null) {
                continue;
            }
            int hour = profile.getEndHour();
            int min = profile.getEndMin();
            long time = toMillis(hour, min);
            endTime = Math.max(endTime, time);
        }
        return endTime == Long.MIN_VALUE ? 0 : endTime;
    }

    /**
     * Returns the app list to be locked.
     *
     * @param packageName the package name of app
     * @return the app list to be locked.
     */
    public synchronized List<AppInfo> getToLockApps(String packageName) {
        List<AppInfo> res = new ArrayList<>();
        List<AppInfo> list = AppInfo.findAllLockApps(packageName);
        for (AppInfo appInfo : list) {
            int profId = appInfo.getProfId();
            if (appInfo.isLocked() && (profId == Profile.START_NOW_PROFILE_ID || Profile.isStart(profId))) {
                res.add(appInfo);
            }
        }
        return res;
    }


    /**
     * update app status settings
     *
     * @param appInfos
     */
    public void saveOrUpdateInfos(List<AppInfo> appInfos) {
        for (AppInfo appInfo : appInfos) {
            appInfo.saveOrUpdate();
        }
    }

    /**
     * Reset the lock status of apps to 'false' in specific profile
     *
     * @param profId
     */
    public void reset(int profId) {
        ContentValues cv = new ContentValues();
        cv.put("islocked", 0);
        LitePal.updateAll(AppInfo.class, cv, "profid = ?", String.valueOf(profId));
    }
}


