package com.example.jessie.focusing.Interface;


import com.example.jessie.focusing.widget.TimePickerFragment;


/**
 * @author : Yujie Lyu
 * @date : 09-01-2019
 * @time : 00:52
 */
public interface TimeCallBack {
    /**
     * Execute when time has been set
     *
     * @param tp   the {@link android.widget.TimePicker} user updated.
     * @param time the time set by user, in millis.
     */
    void onTimeSet(TimePickerFragment tp, long time);
}
