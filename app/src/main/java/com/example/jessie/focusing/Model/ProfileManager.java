package com.example.jessie.focusing.Model;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 25-01-2019
 * @time : 08:34
 */
public class ProfileManager {
    /**
     * use {@link Profile#findById(int)} instead.
     * @param profileId
     * @return
     */
    @Deprecated
    public synchronized Profile getProfile(int profileId) {
        return LitePal.find(Profile.class, profileId);

    }

    /**
     * use {@link Profile#findById(int)} instead.
     *
     * @param profileId
     * @return
     */
    @Deprecated
    public synchronized Profile syncProfileDetail(int profileId) {
        Profile profile = LitePal.find(Profile.class, profileId);
        return profile;
    }

    /**
     * use {@link Profile#findAllOnSchedule(int)}
     *
     * @param dayOfWeek
     * @return
     */
    @Deprecated
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
        profiles.sort(Profile::compareTo);
        return profiles;
    }

}
