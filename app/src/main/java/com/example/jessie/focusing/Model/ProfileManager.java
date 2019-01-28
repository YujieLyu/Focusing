package com.example.jessie.focusing.Model;

import android.content.Context;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 25-01-2019
 * @time : 08:34
 */
public class ProfileManager {
    private Context context;

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

    public synchronized void updateProfile(Profile profile) {
        profile.saveOrUpdate("id=?", String.valueOf(profile.getId()));
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

    public synchronized List<Profile> syncProfileOnSchedule(int today){

       List<Profile> all=LitePal.findAll(Profile.class);
        List<Profile> profiles = new ArrayList<>();
        for (Profile profile : all) {
            if (profile.getRepeatId()==0||profile.getRepeatId()==(today-1)){
                profiles.add(profile);
            }

        }
        return profiles;

    }


}
