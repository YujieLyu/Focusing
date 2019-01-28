package com.example.jessie.focusing.Model;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : Yujie Lyu
 * 表示创建的profile
 * @date : 25-01-2019
 * @time : 08:26
 */
public class Profile extends LitePalSupport {
    @Column(unique = true)
    private int id;

    @Column(unique = true)
    private String profileName;

    public Profile() {
    }

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
