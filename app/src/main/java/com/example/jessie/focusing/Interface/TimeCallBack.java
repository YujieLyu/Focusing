package com.example.jessie.focusing.Interface;


import com.example.jessie.focusing.View.TimePickerFragment;

import java.util.Calendar;

/**
 * @author : Yujie Lyu
 * @date : 09-01-2019
 * @time : 00:52
 */
public interface TimeCallBack {
    void getTime(TimePickerFragment tp,Calendar displayTime);

}
