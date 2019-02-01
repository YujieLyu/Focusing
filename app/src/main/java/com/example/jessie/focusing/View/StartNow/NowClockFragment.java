package com.example.jessie.focusing.View.StartNow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.OnCirclePickerTimeChangedListener;
import com.example.jessie.focusing.View.CountDown.Countdown_Activity;
import com.example.jessie.focusing.widget.CirclePicker;

import java.util.Calendar;

/**
 * @author : Yujie Lyu
 * @date : 24-01-2019
 * @time : 08:24
 */
public class NowClockFragment extends Fragment {

    private TextView tv_startTime, tv_endTime, tv_countTime;
    private Calendar timeStart, timeEnd;
    private CirclePicker circlePicker;
    private String countTime;
    private boolean startOn=false;

    public NowClockFragment() {
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startnow_timepicker, container, false);

        tv_countTime = view.findViewById(R.id.tv_countTime);
        tv_startTime = view.findViewById(R.id.tv_start_time);
        tv_endTime = view.findViewById(R.id.tv_end_time);
        circlePicker = view.findViewById(R.id.timer);
        circlePicker.setInitialTime(startDegree(), startDegree() + 30);
        circlePicker.setOnTimerChangeListener(new OnCirclePickerTimeChangedListener() {


            @SuppressLint("SetTextI18n")
            @Override
            public void endTimeChanged(float startDegree, float endDegree) {

                double endCount = (endDegree / 720) * (24 * 60);
                int endHour = (int) Math.floor(endCount / 60);
                int endMinute = (int) Math.floor(endCount % 60);
                timeEnd = Calendar.getInstance();
                timeEnd.set(Calendar.HOUR_OF_DAY, endHour);
                timeEnd.set(Calendar.MINUTE, endMinute);
                tv_endTime.setText(((endHour < 10) ? ("0" + endHour) : (endHour + "")) + ":" + ((endMinute < 10) ? ("0" + endMinute) : (endMinute + "")));
                if (startDegree == endDegree && (endDegree + 360 == startDegree) && (endDegree - 360 == startDegree)) {
                    countTime = "00:00";
                } else if (timeStart.get(Calendar.HOUR_OF_DAY) == endHour && timeStart.get(Calendar.MINUTE) == endMinute) {
                    countTime = "00:00";
                } else {
                    countTime();
                }
                tv_countTime.setText(countTime);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void initTime(float startDegree, float endDegree) {
                double startCount = (startDegree / 720) * (24 * 60);
                int startHour = (int) Math.floor(startCount / 60);
                int startMinute = (int) Math.floor(startCount % 60);
                tv_startTime.setText(((startHour < 10) ? ("0" + startHour) : (startHour + "")) + ":" + ((startMinute < 10) ? ("0" + startMinute) : (startMinute + "")));


                double endCount = (endDegree / 720) * (24 * 60);
                int endHour = (int) Math.floor(endCount / 60);
                int endMinute = (int) Math.floor(endCount % 60);
                timeEnd = Calendar.getInstance();
                timeEnd.set(Calendar.HOUR_OF_DAY, endHour);
                timeEnd.set(Calendar.MINUTE, endMinute);
                tv_endTime.setText(((endHour < 10) ? ("0" + endHour) : (endHour + "")) + ":" + ((endMinute < 10) ? ("0" + endMinute) : (endMinute + "")));
                countTime = String.format("%02d h %02d m", (endHour - startHour), (endMinute - startMinute));
                tv_countTime.setText(countTime);
            }

        });


        //两个按钮
        Button btnStart = view.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                countTime();
                startOn=true;
                if (countTime.equals("00:00")) {
                    Toast toast = Toast.makeText(getContext(),
                            R.string.invalid_time_suggestion, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {
                    Intent intent = new Intent(getActivity(),Countdown_Activity.class);
                    intent.putExtra("endTime", timeEnd.getTimeInMillis());
                    intent.putExtra("startTime", timeStart.getTimeInMillis());
//                    intent.putExtra("isStart", startOn);
                    startActivity(intent);
                }
            }
        });
        return view;

    }

    public boolean isStartOn() {
        return startOn;
    }

    private float startDegree() {
        timeStart = Calendar.getInstance();
        int hour = timeStart.get(Calendar.HOUR_OF_DAY);
        int min = timeStart.get(Calendar.MINUTE);
        int startTime = hour * 60 + min;
        float startDegree = (float) startTime / 2;
        return startDegree;

    }

    //todo:动态更新时间
    private void updateCurrTime() {
        timeStart = Calendar.getInstance();
        int hour = timeStart.get(Calendar.HOUR_OF_DAY);
        int min = timeStart.get(Calendar.MINUTE);
        String displayTime = String.format("%02d:%02d", hour, min);
        tv_startTime.setText(displayTime);
    }


    public String countTime() {
        //todo:判断过了0点的时间计算；是否做成只选第二个时间？
        //todo:need to optimize the calculate,do it later
        int hours;
        int mins;
        if (timeEnd == null) {
            //todo:处理time2时间未选择的问题.是否弹窗提示用户选择正确时间
            timeEnd = Calendar.getInstance();
            timeEnd.setTimeInMillis(System.currentTimeMillis());
            countTime = "00:00";
        } else {
            hours = timeEnd.get(Calendar.HOUR_OF_DAY) - timeStart.get(Calendar.HOUR_OF_DAY);
            mins = timeEnd.get(Calendar.MINUTE) - timeStart.get(Calendar.MINUTE);
            if (hours == 0) {
                if (mins >= 0) {
                    countTime=String.format("%02d h %02d m",hours,mins);
                }else {
                    countTime=String.format("%02d h %02d m",23,mins+60);
                }
            }else if(hours>0) {
                if (mins >= 0) {
                    countTime = String.format("%02d h %02d m", hours, mins);
                } else {
                    countTime = String.format("%02d h %02d m", hours - 1, mins + 60);
                }
            }else {
                if(mins>=0){
                    countTime=String.format("%02d h %02d m",hours+24,mins);
                }else {
                    countTime=String.format("%02d h %02d m",hours+23,mins+60);
                }
            }


        }
        return countTime;

    }

}
