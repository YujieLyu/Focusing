package com.example.jessie.focusing.Model;

import com.example.jessie.focusing.Service.LockService;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.example.jessie.focusing.Utils.TimeHelper.betweenRange;
import static com.example.jessie.focusing.Utils.TimeHelper.toMillis;

/**
 * @author : Yujie Lyu
 * 表示创建的profile
 * @date : 25-01-2019
 * @time : 08:26
 */
public class Profile extends LitePalSupport {
    public static final int START_NOW_PROFILE_ID = -10;
    public static Comparator<Profile> nameComparator = new Comparator<Profile>() {
        @Override
        public int compare(Profile o1, Profile o2) {
            String p1 = o1.getProfileName();
            String p2 = o2.getProfileName();
            String a = null;
            String b = null;
            if (!p1.isEmpty() && !p1.isEmpty()) {
                a = p1.substring(0, 1);
                b = p1.substring(0, 1);
            }
            return (a.compareTo(b));
        }
    };
    public static Comparator<Profile> startTimeComparator = new Comparator<Profile>() {
        @Override
        public int compare(Profile o1, Profile o2) {
            int o1H = o1.getStartHour();
            int o1M = o1.getStartMin();
            int o2H = o2.getStartHour();
            int o2M = o2.getStartMin();
            if (o1H > o2H) {
                return 1;
            } else if (o1H == o2H) {
                if (o1M >= o2M) {
                    return 1;
                } else return -1;
            } else
                return -1;
        }
    };
    @Column(unique = true)
    private int id;
    @Column(unique = true)
    private String profileName;
    @Column
    private Date date;
    @Column
    private int startHour;
    @Column
    private int startMin;
    @Column
    private int endHour;
    @Column
    private int endMin;
    @Column
    private String repeat;
    @Column
    private int repeatId;
    @Column
    private String alarm;
    @Column
    private Boolean isOn;
    private List<AppInfo> appInfos = new ArrayList<>();

    public Profile() {
    }

    public static boolean onSchedule(Profile profile, int dayOfWeek) {
        if (profile == null) {
            return false;
        }
        int repeatId = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
        int proRepeatId = profile.getRepeatId();
        return proRepeatId == 0 || proRepeatId == repeatId;
    }

    public static boolean isStart(int profId) {
        if (profId == START_NOW_PROFILE_ID) {
            return LockService.START_NOW_END_TIME > 0;
        }
        Profile profile = LitePal.find(Profile.class, profId);
        return isStart(profile);
    }

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

    public Boolean getOn() {
        return isOn;
    }

    public void setOn(Boolean on) {
        isOn = on;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
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

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public int getRepeatId() {
        return repeatId;
    }

    public void setRepeatId(int repeatId) {
        this.repeatId = repeatId;
    }

}
