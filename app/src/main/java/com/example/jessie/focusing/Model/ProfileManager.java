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

    public synchronized List<ProfileInfo> insertProfileInfos(ProfileInfo profileInfo) {


        ProfileInfo profiledb = LitePal.find(ProfileInfo.class, profileInfo.getProfileId());
        if (!profiledb.getProfileName().equals(profileInfo.getProfileName())) {
            profiledb.setProfileName(profileInfo.getProfileName());
        }

        List<ProfileInfo> profilesDB = LitePal.findAll(ProfileInfo.class);
        if (!profilesDB.contains(profileInfo)) {
            profilesDB.add(profileInfo);
            profileInfo.save();//todo:这里是？
        }

        return profilesDB;
    }

}
