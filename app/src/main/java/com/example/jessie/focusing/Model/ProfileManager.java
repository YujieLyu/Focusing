package com.example.jessie.focusing.Model;

import android.content.Context;

import org.litepal.LitePal;

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
    public synchronized void updateProfile(Profile profile){
        profile.saveOrUpdate("id=?", String.valueOf(profile.getId()));
    }

    public synchronized List<Profile> syncProfile() {
//        LitePal.deleteAll(Profile.class);

        List<Profile> profilesDB = LitePal.findAll(Profile.class);
//
//        if (profile == null) {
//            return profilesDB;
//        }
//        Profile profiledb = LitePal.find(Profile.class,profile.getId());
//        if ( !profiledb.getProfileName()
//                .equals(profile.getProfileName())) {
//            profiledb.setProfileName(profile.getProfileName());
//        } else {
//            profilesDB.add(profiledb);
//        }

        return profilesDB;
    }




}
