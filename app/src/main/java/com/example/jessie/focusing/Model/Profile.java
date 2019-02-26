package com.example.jessie.focusing.Model;

import com.example.jessie.focusing.Utils.TimeHelper;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.jessie.focusing.Utils.TimeHelper.DAY_IN_MILLIS;
import static com.example.jessie.focusing.Utils.TimeHelper.betweenRange;

/**
 * @author : Yujie Lyu
 * @date : 25-01-2019
 * @time : 08:26
 */
public class Profile extends LitePalSupport implements Comparable<Profile> {
    public static final int START_NOW_PROFILE_ID = -10;

    @Column(unique = true)
    private int id;
    @Column(unique = true)
    private String profileName;
    @Column
    private long startTime;
    @Column
    private long endTime;
    @Column(defaultValue = "-1")
    private int repeatId = -1;

    /**
     * Check if the profile is on schedule
     *
     * @param profile
     * @param dayOfWeek
     * @return
     */
    public static boolean onSchedule(Profile profile, int dayOfWeek) {
        if (profile == null) {
            return false;
        }
        if (profile.id == START_NOW_PROFILE_ID) {
            return true;
        }
        return WeekDays.getDay(dayOfWeek).isChosen(profile.repeatId);
    }

    /**
     * Check if the profile has started.
     *
     * @param profId
     * @return
     */
    public static boolean isStart(int profId) {
        Profile profile = findById(profId);
        return isStart(profile);
    }

    /**
     * Check if the profile has started.
     *
     * @param profile
     * @return
     */
    public static boolean isStart(Profile profile) {
        if (profile == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (!onSchedule(profile, dayOfWeek)) {
            return false;
        }
        long start = profile.startTime;
        long end = profile.endTime;
        long curr = System.currentTimeMillis();
        return betweenRange(start, end, curr);
    }

    public static List<Profile> findAll() {
        return LitePal.findAll(Profile.class);
    }

    public static Profile findById(int id) {
        return LitePal.find(Profile.class, id);
    }

    public static List<Profile> findAllOnSchedule(int dayOfWeek) {
        int repeatId = toRepeatId(dayOfWeek);
        String where = String.format("%s = (repeatid & %s)", repeatId, repeatId);
        List<Profile> res = LitePal.where(where).find(Profile.class);
        res.sort(Profile::compareTo);
        return res;
    }

    /**
     * Returns all profiles have started.
     *
     * @return
     */
    public static Set<Profile> findAllStarted() {
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        List<Profile> onGoing = findAllOnSchedule(dayOfWeek);
        Set<Profile> profiles = new HashSet<>();
        for (Profile profile : onGoing) {
            long start = profile.startTime;
            long end = profile.endTime;
            if (betweenRange(start, end, now)) {
                profiles.add(profile);
            }
        }
        return profiles;
    }

    public static int count(int id) {
        return LitePal.where("id = ?", id + "").count(Profile.class);
    }

    /**
     * Converts the {@link Calendar#DAY_OF_WEEK} to {@link #repeatId}
     *
     * @param dayOfWeek
     * @return
     */
    public static int toRepeatId(int dayOfWeek) {
        return WeekDays.getValue(dayOfWeek);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        if (endTime < startTime) {
            endTime += DAY_IN_MILLIS;
        }
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setTime(long startTime, long endTime) {
        this.startTime = startTime;
        if (endTime < startTime) {
            endTime += DAY_IN_MILLIS;
        }
        this.endTime = endTime;
    }

    public long getDuration() {
        if (endTime < startTime) {
            endTime += DAY_IN_MILLIS;
        }
        return endTime - startTime;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getRepeatString() {
        return WeekDays.toString(repeatId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Profile) {
            Profile profile = (Profile) obj;
            return this.id == profile.id;
        }
        return super.equals(obj);
    }

    public int getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(int repeatId) {
        this.repeatId = repeatId;
    }

    public String getStartTimeStr() {
        return TimeHelper.toString(startTime);
    }

    public String getEndTimeStr() {
        return TimeHelper.toString(endTime);
    }

    /**
     * Get the displayed time string
     *
     * @return
     */
    public String getTimeSlot() {
        return getStartTimeStr() + " ~ " + getEndTimeStr();
    }

    @Override
    public int compareTo(Profile o) {
        int res = Long.compare(startTime, o.startTime);
        return res == 0 ? Long.compare(endTime, o.endTime) : res;
    }

    public void saveOrUpdate() {
        saveOrUpdate("id = ?", String.valueOf(id));
    }

    public void deleteCascadeAsync() {
        super.deleteAsync().listen(rowsAffected -> {
            AppInfoManager manager = new AppInfoManager();
            manager.deleteByProfId(id);
        });
    }
}
