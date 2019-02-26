package com.example.jessie.focusing.widget;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.example.jessie.focusing.Interface.TimeCallBack;

import java.util.Calendar;

import static com.example.jessie.focusing.Utils.TimeHelper.toMillis;

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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int mins = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getContext(), this, hour, mins, true);
    }

    public void setCallBackListener(TimeCallBack listener) {
        this.listener = listener;
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (listener != null) {
            long time = toMillis(hourOfDay, minute);
            listener.onTimeSet(this, time);
        }

    }

}
