package com.example.jessie.focusing.Model;

import android.content.ContentValues;
import android.util.Log;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
     * Reset the lock status of apps to 'false' in specific profile
     *
     * @param profId
     */
    public static void reset(int profId) {
        Log.i(TAG, String.format("Reset all apps belonging to %s unlocked.", profId));
        ContentValues cv = new ContentValues();
        cv.put("islocked", 0);
        LitePal.updateAll(AppInfo.class, cv, "profid = ?", String.valueOf(profId));
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
     * @param packageName
     * @param startedProfiles
     * @return
     * @see #getToLockApps(String)
     */
    public synchronized List<AppInfo> getToLockApps(String packageName, Collection<Profile> startedProfiles) {
        List<AppInfo> res = new ArrayList<>();
        List<AppInfo> list = AppInfo.findAllLockApps(packageName);
        Set<Integer> profIds = startedProfiles.stream().mapToInt(Profile::getId).boxed().collect(Collectors.toSet());
        for (AppInfo appInfo : list) {
            int profId = appInfo.getProfId();
            if (appInfo.isLocked() &&
                    (profId == Profile.START_NOW_PROFILE_ID || profIds.contains(profId))) {
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
     * Returns the latest end time in the apps to be locked.
     *
     * @param toLockApps the app list to be locked.
     * @return the latest end time
     */
    public long getLatestEndTime(List<AppInfo> toLockApps) {
        long endTime = -1;
        for (AppInfo app : toLockApps) {
            Profile profile = Profile.findById(app.getProfId());
            if (profile == null) {
                continue;
            }
            long time = profile.getEndTime();
            endTime = Math.max(endTime, time);
        }
        return endTime;
    }

    /**
     * @param toLockApps
     * @param startedProfiles
     * @return
     * @see #getLatestEndTime(List)
     */
    public long getLatestEndTime(List<AppInfo> toLockApps, Collection<Profile> startedProfiles) {
        long endTime = -1;
        for (AppInfo app : toLockApps) {
            int profId = app.getProfId();
            if (profId == Profile.START_NOW_PROFILE_ID) {
                continue;
            }
            Optional<Profile> opt = startedProfiles.stream().filter(profile -> profId == profile.getId()).findAny();
            if (opt.isPresent()) {
                Profile profile = opt.get();
                long time = profile.getEndTime();
                endTime = Math.max(endTime, time);
            }
        }
        return endTime;
    }
}


