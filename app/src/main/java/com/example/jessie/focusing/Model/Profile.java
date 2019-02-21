package com.example.jessie.focusing.Model;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.jessie.focusing.Utils.TimeHelper.betweenRange;
import static com.example.jessie.focusing.Utils.TimeHelper.toMillis;

/**
 * @author : Yujie Lyu
 * @date : 25-01-2019
 * @time : 08:26
 */
public class Profile extends LitePalSupport implements Comparable<Profile> {
    public static final int START_NOW_PROFILE_ID = -10;
    public static final String[] REPEAT_TYPE = {
            "None",
            "Everyday",
            "Every Monday", "Every Tuesday", "Every Wednesday",
            "Every Thursday", "Every Friday", "Every Saturday", "Every Sunday"
    };

    @Column(unique = true)
    private int id;
    @Column(unique = true)
    private String profileName;
    @Column
    private int startHour;
    @Column
    private int startMin;
    @Column
    private int endHour;
    @Column
    private int endMin;
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
        long start = toMillis(profile.getStartHour(), profile.getStartMin());
        long end = toMillis(profile.getEndHour(), profile.getEndMin());
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

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMin() {
        return startMin;
    }

    public void setStartMin(int startMin) {
        this.startMin = startMin;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMin() {
        return endMin;
    }

    public void setEndMin(int endMin) {
        this.endMin = endMin;
    }

    public void setStartTime(Calendar calendar) {
        startHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMin = calendar.get(Calendar.MINUTE);
    }

    public void setEndTime(Calendar calendar) {
        endHour = calendar.get(Calendar.HOUR_OF_DAY);
        endMin = calendar.get(Calendar.MINUTE);
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
        return getTimeStr(startHour, startMin);
    }

    public String getEndTimeStr() {
        return getTimeStr(endHour, endMin);
    }

    private String getTimeStr(int hour, int min) {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, min);
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
        if (startHour != o.startHour) {
            return startHour - o.startHour;
        }
        if (startMin != o.startMin) {
            return startMin - o.startMin;
        }
        if (endHour != o.endHour) {
            return o.endHour - endHour;
        }
        return o.endMin - endMin;
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
