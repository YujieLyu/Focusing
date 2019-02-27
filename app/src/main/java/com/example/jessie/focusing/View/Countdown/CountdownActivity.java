package com.example.jessie.focusing.View.Countdown;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.jessie.focusing.Model.FocusTimeStats;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Service.LockService;
import com.example.jessie.focusing.Utils.LockUtil;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.Main.MainActivity;
import com.example.jessie.focusing.View.Shared.BaseSingleTaskActivity;

import cn.iwgang.countdownview.CountdownView;

import static com.example.jessie.focusing.Utils.AppConstants.END_TIME;
import static com.example.jessie.focusing.Utils.AppConstants.START_TIME;
import static com.example.jessie.focusing.Utils.TimeHelper.DAY_IN_MILLIS;

/**
 * @author : Yujie Lyu
 * @date : 11-01-2019
 * @time : 10:14
 */
public class CountdownActivity extends BaseSingleTaskActivity implements CountdownView.OnCountdownEndListener {

    public static final String IS_START_NOW = "is_start_now";
    private static final String TAG = CountdownActivity.class.getSimpleName();
    private CountdownView cdv_count;
    private boolean isStartNow;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        cdv_count = findViewById(R.id.cdv_count);
        cdv_count.setOnCountdownEndListener(this);
        Button btn_stop = findViewById(R.id.btn_cancel_countdown);
        btn_stop.setOnClickListener(v -> {
            finish();
            cdv_count.stop();
            Intent intent = new Intent(CountdownActivity.this, MainActivity.class);
            startActivity(intent);
            if (isStartNow) {
                FocusTimeStats focusTimeStats = new FocusTimeStats(startTime, System.currentTimeMillis());
                focusTimeStats.save();
                LockService.stopStartNow(this);
            }
        });
        StatusBarUtil.setStatusTransparent(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void initData() {
        isStartNow = getIntent().getBooleanExtra(IS_START_NOW, false);
        startTime = getIntent().getLongExtra(START_TIME, -1);
        long endTime = getIntent().getLongExtra(END_TIME, -1);
        long currTimeStart = System.currentTimeMillis();
        long countTime;
        if (currTimeStart < endTime) {
            countTime = endTime - currTimeStart;
        } else {
            countTime = endTime - currTimeStart + DAY_IN_MILLIS;
        }
        cdv_count.start(countTime);
    }

    @Override
    public void onBackPressed() {
        LockUtil.goHome(this);
    }


    @Override
    public void onEnd(CountdownView cv) {
        cv.stop();
        this.finish();
    }

}
