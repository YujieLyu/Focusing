package com.example.jessie.focusing.Model;

import com.example.jessie.focusing.Service.LockService;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.example.jessie.focusing.Utils.TimeHelper.betweenRange;
import static com.example.jessie.focusing.Utils.TimeHelper.toMillis;

/**
 * @author : Yujie Lyu
 * @date : 25-01-2019
 * @time : 08:34
 */
public class ProfileManager {

    public ProfileManager() {

    }

    /**
     * Insert profile Infos to DB
     */
    public synchronized void updateProfiles(List<Profile> profiles) {
        for (Profile profile : profiles) {
            profile.saveOrUpdate("id=?", String.valueOf(profile.getId()));
        }

    }

    public synchronized Profile getProfile(int profileId) {
        Profile profile = LitePal.find(Profile.class, profileId);
        return profile;

    }

    public synchronized void updateProfile(Profile profile) {
        profile.saveOrUpdate("id=?", String.valueOf(profile.getId()));
    }

    public synchronized void deleteProfile(int profileId) {
        Profile p = LitePal.find(Profile.class, profileId);
        p.delete();
    }

    public synchronized List<Profile> syncProfile() {
//        LitePal.deleteAll(Profile.class);

        List<Profile> profilesDB = LitePal.findAll(Profile.class);
        return profilesDB;
    }

    public synchronized Profile syncProfileDetail(int profileId) {
        Profile profile = LitePal.find(Profile.class, profileId);
        return profile;
    }



    public synchronized List<Profile> syncProfileOnSchedule(int dayOfWeek) {
        int repeatId = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
        List<Profile> profDb = LitePal.findAll(Profile.class);

        List<Profile> profiles = new ArrayList<>();
        for (Profile profile : profDb) {
            int repId = profile.getRepeatId();
            if (repId == 0 || repId == repeatId) {
                profiles.add(profile);
            }
        }

        Collections.sort(profiles, Profile.startTimeComparator);

        return profiles;

    }

    //查看输入的profile目前是否在运行
    public synchronized boolean checkInTimeSlot(int profId) {
        Profile profile = LitePal.find(Profile.class, profId);
        long startTime = toMillis(profile.getStartHour(), profile.getStartMin());
        long endTime = toMillis(profile.getEndHour(), profile.getEndMin());
        long currTime = System.currentTimeMillis();
        if (startTime < currTime && currTime < endTime) {
            return true;
        }
        return false;
    }
}
