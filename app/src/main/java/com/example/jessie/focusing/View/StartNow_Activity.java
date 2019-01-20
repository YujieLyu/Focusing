package com.example.jessie.focusing.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author : Yujie Lyu
 * @date : 19-01-2019
 * @time : 00:54
 */
public class StartNow_Activity extends AppCompatActivity implements TimeCallBack {
    private TextView tv_currtime, tv_endtime, tv_bg;
    private TimePickerFragment timePicker;
    private Calendar time1, time2;
    private String countTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_now_time);
        tv_currtime = findViewById(R.id.tx_time1);
        tv_endtime = findViewById(R.id.tx_time2);
        new Timer().schedule(timerTask, new Date(), 5000);

        //Second time picker
        tv_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time_picker");
            }
        });

        tv_bg = findViewById(R.id.tv_background);
        tv_bg.setBackgroundColor(Color.argb(20, 255, 255, 255)); //背景透明度

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //两个按钮
        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTime();
                if (countTime.equals("00:00")) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            R.string.invalid_time_suggestion, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                } else {
                    Intent intent = new Intent(StartNow_Activity.this, Countdown_Activity.class);
                    intent.putExtra("endTime", time2.getTimeInMillis());
                    intent.putExtra("startTime", time1.getTimeInMillis());
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
//        setDarkStatusIcon(true);
//        initView();
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

    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            myHandler.sendMessage(message);
        }
    };

    //todo:修正莫名其妙但能运行的代码magic
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    updateCurrTime();
                    break;
            }

        }
    };

    private void updateCurrTime() {
        time1 = Calendar.getInstance();
        int hour = time1.get(Calendar.HOUR_OF_DAY);
        int min = time1.get(Calendar.MINUTE);
        String displayTime = String.format("%02d:%02d", hour, min);
        tv_currtime.setText(displayTime);
    }

//    public void initView() {
//
//        final Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        displayCurrTime = String.format("%02d:%02d", hour, minute);
//        tv_currtime.setText(displayCurrTime);//todo:显示当前时间
//
//    }


    @Override
    public void getTime(TimePickerFragment tp, Calendar chosenCalendar) {
        //data即为fragment调用该函数传回的日期时间
        int chosenHour = chosenCalendar.get(Calendar.HOUR_OF_DAY);
        int chosenMin = chosenCalendar.get(Calendar.MINUTE);
        String displayTime = String.format("%02d:%02d", chosenHour, chosenMin);
        tv_endtime.setText(displayTime);
        time2 = chosenCalendar;

    }

    public void countTime() {
        //todo:判断过了0点的时间计算；是否做成只选第二个时间？
        //todo:need to optimize the calculate,do it later
        int hours;
        int mins;
        if (time2 == null) {
            //todo:处理time2时间未选择的问题.是否弹窗提示用户选择正确时间
            time2 = Calendar.getInstance();
            time2.setTimeInMillis(System.currentTimeMillis());
            countTime = "00:00";
        } else {
            hours = time2.get(Calendar.HOUR_OF_DAY) - time1.get(Calendar.HOUR_OF_DAY);
            mins = time2.get(Calendar.MINUTE) - time1.get(Calendar.MINUTE);
            if (hours == 0 && mins == 0) {
                countTime = "00:00";
            }else if (hours < 0 && mins >= 0) {
                countTime = (24 + hours) + ":" + mins;
            } else if (hours > 0 && mins < 0) {
                countTime = (hours - 1) + ":" + (60 + mins);

            } else if (hours <= 0) {
                countTime = (24 + hours) + ":" + (60 + mins);
            }
        }

    }


}
