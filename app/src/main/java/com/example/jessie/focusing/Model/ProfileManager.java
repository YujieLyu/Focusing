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

    public synchronized void deleteProfile(int profileId){
        Profile p=LitePal.find(Profile.class,profileId);
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

    public synchronized List<Profile> syncProfileOnSchedule(int today){

       List<Profile> all=LitePal.findAll(Profile.class);
        List<Profile> profiles = new ArrayList<>();
        for (Profile profile : all) {
            switch (profile.getRepeat()){
                case "Everyday":
                    profiles.add(profile);
                    break;
                case "Every Monday":
                    if(today==2){
                        profiles.add(profile);
                    }
                    break;
                case "Every Tuesday"   :
                    if(today==3){
                        profiles.add(profile);
                    }
                    break;
                case "Every Wednesday":
                    if(today==4){
                        profiles.add(profile);
                    }
                case "Every Thursday":
                    if (today==5){
                        profiles.add(profile);
                    }
                case "Every Friday":
                    if (today==6){
                        profiles.add(profile);
                    }
                case "Every Saturday":
                    if (today==7){
                        profiles.add(profile);
                    }
                case "Every Sunday":
                    if (today==1){
                        profiles.add(profile);
                    }
            }
//            if((today==1)&&(profile.getRepeatId()==7)){
//                profiles.add(profile);
//            }else if (profile.getRepeatId()==0||profile.getRepeatId()==(today-1)){
//                profiles.add(profile);
//            }

        }
        return profiles;

    }


}
