package com.example.jessie.focusing.View.StartNow;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Service.LockService;
import com.example.jessie.focusing.Utils.BaseTimeChangeListener;
import com.example.jessie.focusing.Utils.TimeHelper;
import com.example.jessie.focusing.View.Countdown.CountdownActivity;
import com.example.jessie.focusing.widget.CirclePicker;

import java.util.Calendar;
import java.util.Date;

import static com.example.jessie.focusing.Utils.AppConstants.END_TIME;
import static com.example.jessie.focusing.Utils.AppConstants.START_TIME;

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
    private String countTime;

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

            }
        }
    };
    Thread thread = new Thread(() -> {
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

    });

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startnow_timepicker, container, false);

        tv_countTime = view.findViewById(R.id.tv_countTime);
        tv_startTime = view.findViewById(R.id.tc_start_time);
        thread.start();//更改当前时间
        tv_endTime = view.findViewById(R.id.tv_end_time);
        CirclePicker circlePicker = view.findViewById(R.id.timer);
        circlePicker.setInitialTime(startDegree(), startDegree() + ONE_HOUR_DEGREE);
        circlePicker.setOnTimerChangeListener(new BaseTimeChangeListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onEndTimeChanged(float startDegree, float endDegree) {

                double endCount = (endDegree / 720) * (24 * 60);
                int endHour = (int) Math.floor(endCount / 60);
                int endMin = (int) Math.floor(endCount % 60);
                timeEnd = Calendar.getInstance();
                timeEnd.set(Calendar.HOUR_OF_DAY, endHour);
                timeEnd.set(Calendar.MINUTE, endMin);
                tv_endTime.setText(((endHour < 10) ? ("0" + endHour) : (endHour + "")) + ":" + ((endMin < 10) ? ("0" + endMin) : (endMin + "")));
                if (startDegree == endDegree
//                        && (endDegree + HALF_DAY_DEGREE == startDegree) && (endDegree - HALF_DAY_DEGREE == startDegree)
                ) {
                    countTime = "0 h 0 m";
                } else if (timeStart.get(Calendar.HOUR_OF_DAY) == endHour && timeStart.get(Calendar.MINUTE) == endMin) {
                    countTime = "0 h 0 m";
                } else {
                    countTime();
                }
                tv_countTime.setText(countTime);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onInitTime(float startDegree, float endDegree) {
//                String startTime = CircleTimeUtils.toString(startDegree);
//                tv_startTime.setText(startTime);
//                String endTime = CircleTimeUtils.toString(endDegree);
//                tv_endTime.setText(endTime);
//

                double startCount = (startDegree / 720) * (24 * 60);
                int startHour = (int) Math.floor(startCount / 60);
                int startMin = (int) Math.floor(startCount % 60);
                tv_startTime.setText(((startHour < 10) ? ("0" + startHour) : (startHour + "")) + ":" + ((startMin < 10) ? ("0" + startMin) : (startMin + "")));


                double endCount = (endDegree / 720) * (24 * 60);
                int endHour = (int) Math.floor(endCount / 60);
                int endMin = (int) Math.floor(endCount % 60);
                timeEnd = Calendar.getInstance();
                timeEnd.set(Calendar.HOUR_OF_DAY, endHour);
                timeEnd.set(Calendar.MINUTE, endMin);
                tv_endTime.setText(((endHour < 10) ? ("0" + endHour) : (endHour + "")) + ":" + ((endMin < 10) ? ("0" + endMin) : (endMin + "")));
                countTime = String.format("%02d h %02d m", (endHour - startHour), (endMin - startMin));
                tv_countTime.setText(countTime);
            }

        });

        Button btnStart = view.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> {
//                countTime();
            if (countTime.equals("00:00")) {
                Toast toast = Toast.makeText(getContext(),
                        R.string.invalid_time_suggestion, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            } else {
                if (timeEnd.getTimeInMillis() < timeStart.getTimeInMillis()) {
                    timeEnd.add(Calendar.DATE, 1);
                }
                long startTime = timeStart.getTimeInMillis();
                long endTime = timeEnd.getTimeInMillis();
                Intent intent = new Intent(getActivity(), CountdownActivity.class);
                intent.putExtra(CountdownActivity.IS_START_NOW, true);
                intent.putExtra(END_TIME, endTime);
                intent.putExtra(START_TIME, startTime);
                startActivity(intent);

            }
        });
        return view;

    }

    @Override
    public void onStop() {
        super.onStop();
        LockService.startNow(getContext(), timeStart.getTimeInMillis(), timeEnd.getTimeInMillis());
    }

    private float startDegree() {
        timeStart = Calendar.getInstance();
        int hour = timeStart.get(Calendar.HOUR_OF_DAY);
        int min = timeStart.get(Calendar.MINUTE);
        int startTime = hour * 60 + min;
        return (float) startTime / 2;

    }

    public String countTime() {


        if (timeEnd == null) {
            //todo:处理time2时间未选择的问题.是否弹窗提示用户选择正确时间
            timeEnd = Calendar.getInstance();
            countTime = "00:00";
        } else {
            countTime = TimeHelper.showInterval(timeStart, timeEnd);


        }
        return countTime;

    }
}
