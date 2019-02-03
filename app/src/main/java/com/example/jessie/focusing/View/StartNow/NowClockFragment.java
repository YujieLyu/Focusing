package com.example.jessie.focusing.View.StartNow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Service.LockService;
import com.example.jessie.focusing.Utils.OnCirclePickerTimeChangedListener;
import com.example.jessie.focusing.Utils.TimeHelper;
import com.example.jessie.focusing.View.CountDown.Countdown_Activity;
import com.example.jessie.focusing.widget.CirclePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author : Yujie Lyu
 * @date : 24-01-2019
 * @time : 08:24
 */
public class NowClockFragment extends Fragment {
    private final static int ONE_HOUR_DEGREE = 30;
    private final static int HALF_DAY_DEGREE = 360;
    private TextView tv_startTime, tv_endTime, tv_countTime;
    private Calendar timeStart, timeEnd;
    private CirclePicker circlePicker;
    private String countTime;
    private boolean startOn = false;

    public NowClockFragment() {
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startnow_timepicker, container, false);

        tv_countTime = view.findViewById(R.id.tv_countTime);
        tv_startTime = view.findViewById(R.id.tc_start_time);
        thread.start();
//        tv_startTime.setFormat12Hour(null);
//        tv_startTime.setFormat24Hour("HH:mm");
        tv_endTime = view.findViewById(R.id.tv_end_time);
        circlePicker = view.findViewById(R.id.timer);
        circlePicker.setInitialTime(startDegree(), startDegree() + ONE_HOUR_DEGREE);
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
                if (startDegree == endDegree
//                        && (endDegree + HALF_DAY_DEGREE == startDegree) && (endDegree - HALF_DAY_DEGREE == startDegree)
                        ) {
                    countTime = "0 h 0 m";
                } else if (timeStart.get(Calendar.HOUR_OF_DAY) == endHour && timeStart.get(Calendar.MINUTE) == endMinute) {
                    countTime = "0 h 0 m";
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

        Button btnStart = view.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                countTime();
                startOn = true;
                if (countTime.equals("00:00")) {
                    Toast toast = Toast.makeText(getContext(),
                            R.string.invalid_time_suggestion, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {
                    LockService.StartNow = true;
                    Intent intent = new Intent(getActivity(), Countdown_Activity.class);
                    intent.putExtra("endTime", timeEnd.getTimeInMillis());
                    intent.putExtra("startTime", timeStart.getTimeInMillis());
                    startActivity(intent);

                }
            }
        });
        return view;

    }

    //处理timeStart&countTime实时更新的线程
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);

        }
    });


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                long time = System.currentTimeMillis();
                Date date = new Date(time);
                String currTime = TimeHelper.toString(date);
                tv_startTime.setText(currTime);
                timeStart.setTime(date);
                tv_countTime.setText(countTime());

            } else {
            }
        }
    };

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


    public String countTime() {


        if (timeEnd == null) {
            //todo:处理time2时间未选择的问题.是否弹窗提示用户选择正确时间
            timeEnd = Calendar.getInstance();
            timeEnd.setTimeInMillis(System.currentTimeMillis());
            countTime = "00:00";
        } else {
            countTime = TimeHelper.showInterval(timeStart, timeEnd);


        }
        return countTime;

    }

}
