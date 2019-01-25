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

//    public synchronized List<Profile> insertProfileInfos(Profile profile) {
////        LitePal.deleteAll(Profile.class);
//
//        List<Profile> profilesDB = LitePal.findAll(Profile.class);
//        if (profile == null) {
//            return profilesDB;
//        }
//        Profile profiledb = LitePal.find(Profile.class,profile);
//        if (profiledb != null && !profiledb.getProfileName()
//                .equals(profile.getProfileName())) {
//            profiledb.setProfileName(profile.getProfileName());
//        } else {
//            profilesDB.add(profiledb);
//        }
//
//
//        if (!profilesDB.contains(profile)) {
//            profilesDB.add(profile);
//            profile.save();//todo:这里是？
//        }
//
//        return profilesDB;
//    }

//    public void saveInfos(List<Profile> profileInfos) {
//        List<AppInfo> appInfosDatabase = LitePal.findAll(AppInfo.class);
//        for (AppInfo info : appInfos) {
//            for (AppInfo infoDB : appInfosDatabase) {
//                if(info.getPackageName().equals(infoDB.getPackageName())){
//                    infoDB.setLocked(info.isLocked());
//                    infoDB.save();
//                }
//            }
//            info.save();
//        }
//    }

}
