package com.example.jessie.focusing.Model;

import android.content.Context;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 25-01-2019
 * @time : 08:34
 */
public class ProfileManager {
    private Context context;
    List<Profile> all = LitePal.findAll(Profile.class);

    public ProfileManager(Context context) {

        this.context = context;
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

    public synchronized boolean checkProfOnSchedule(Profile profile, int dayOfWeek) {
        if (profile.getRepeatId() == -1) {
            return false;
        } else if (profile.getRepeatId() == 0) {
            return true;
        } else if (dayOfWeek == 1 && profile.getRepeatId() == 7) {
            return true;
        } else return profile.getRepeatId() == dayOfWeek - 1;
    }

    public synchronized List<Profile> syncProfileOnSchedule(int dayOfWeek) {
        int repeatId = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
        List<Profile> profDb = LitePal.findAll(Profile.class);

        List<Profile> profiles = new ArrayList<>();
        for (Profile profile : profDb) {
            int repId = profile.getRepeatId();
            if (repId==0||repId==repeatId) {
                profiles.add(profile);
            }
        }
//        for (Profile profile : all) {
//            if (profile.getRepeatId() == 0) {
//                profiles.add(profile);
//            } else if (dayOfWeek == 1 && profile.getRepeatId() == 7) {
//                profiles.add(profile);
//            } else if (profile.getRepeatId() == dayOfWeek - 1) {
//                profiles.add(profile);
//            }
//
//        }

        Collections.sort(profiles, Profile.startTimeComparator);

        return profiles;

    }


}
