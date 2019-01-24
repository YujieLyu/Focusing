package com.example.jessie.focusing.Model;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @author : Yujie Lyu
 * 表示创建的profile
 * @date : 25-01-2019
 * @time : 08:26
 */
public class ProfileInfo extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private int profileId;

    @Column(unique = true)
    private String profileName;

    public ProfileInfo(String name) {
        profileName = name;
    }

    public int getProfileId() {
        return profileId;
    }


    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }
}
