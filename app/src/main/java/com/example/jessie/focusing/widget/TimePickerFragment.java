package com.example.jessie.focusing.widget;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.example.jessie.focusing.Interface.TimeCallBack;
import com.example.jessie.focusing.Utils.TimeHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * @author : Yujie Lyu
 * @date : 09-01-2019
 * @time : 00:48
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private TimeCallBack listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int mins=calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getContext(), this, hour, mins, true);
    }

    public void setCallBackListener(TimeCallBack listener) {


        this.listener = listener;
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (listener != null) {
            Calendar chosenCalendar = Calendar.getInstance();
            chosenCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            chosenCalendar.set(Calendar.MINUTE, minute);
            Date time = chosenCalendar.getTime();
            String timeStr = TimeHelper.toString(time);
            listener.onTimeSet(this, timeStr);
        }

    }

}
