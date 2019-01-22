package com.example.jessie.focusing.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessie.focusing.Interface.TimeCallBack;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.OnCirclePickerTimeChangedListener;
import com.example.jessie.focusing.widget.CirclePicker;


import java.util.Calendar;

/**
 * @author : Yujie Lyu
 * @date : 19-01-2019
 * @time : 00:54
 */
public class StartNow_Activity extends AppCompatActivity implements TimeCallBack {
    private TextView tv_startTime, tv_endTime, tv_countTime;
    private Calendar timeStart, timeEnd;
    private CirclePicker circlePicker;
    private String countTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_now_time);
        tv_countTime = findViewById(R.id.tv_countTime);
        tv_startTime = findViewById(R.id.tv_start_time);
        tv_endTime = findViewById(R.id.tv_end_time);
        circlePicker = findViewById(R.id.timer);
        circlePicker.setInitialTime(startDegree(), startDegree() + 30);
        circlePicker.setOnTimerChangeListener(new OnCirclePickerTimeChangedListener() {


            @SuppressLint("SetTextI18n")
            @Override
            public void endTimeChanged(float startDegree, float endDegree) {
//                double endCount=(endDegree<startDegree)?(endDegree / 720) * (12 * 60):(endDegree / 720) * (24 * 60);


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
                tv_endTime.setText(((endHour < 10) ? ("0" + endHour) : (endHour + "")) + ":" + ((endMinute < 10) ? ("0" + endMinute) : (endMinute + "")));
            }

        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //两个按钮
        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                countTime();
                if (countTime.equals("00:00")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            R.string.invalid_time_suggestion, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {
                    Intent intent = new Intent(StartNow_Activity.this, Countdown_Activity.class);
                    intent.putExtra("endTime", timeEnd.getTimeInMillis());
                    intent.putExtra("startTime", timeStart.getTimeInMillis());
                    startActivity(intent);
                }
            }
        });
        Button btnLockApp = findViewById(R.id.btn_lock_app);
        btnLockApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartNow_Activity.this, LockApp_Activity.class);
                startActivity(intent);
            }
        });

        setStatusTransparent();

    }

    protected void setStatusTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0+ 实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4 实现
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void setDarkStatusIcon(boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView == null) return;

            int vis = decorView.getSystemUiVisibility();
            if (dark) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        }
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


    @Override
    public void getTime(TimePickerFragment tp, Calendar chosenCalendar) {
        //data即为fragment调用该函数传回的日期时间
        int chosenHour = chosenCalendar.get(Calendar.HOUR_OF_DAY);
        int chosenMin = chosenCalendar.get(Calendar.MINUTE);
        String displayTime = String.format("%02d:%02d", chosenHour, chosenMin);
        tv_endTime.setText(displayTime);
        timeEnd = chosenCalendar;

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
