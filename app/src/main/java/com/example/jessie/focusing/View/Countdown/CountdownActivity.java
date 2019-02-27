package com.example.jessie.focusing.View.Countdown;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.jessie.focusing.Model.FocusTimeStats;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Service.BlockService;
import com.example.jessie.focusing.Utils.LockUtil;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.Main.MainActivity;
import com.example.jessie.focusing.View.Shared.BaseSingleTaskActivity;

import cn.iwgang.countdownview.CountdownView;

import static com.example.jessie.focusing.Utils.AppConstants.END_TIME;
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
                long startTime = BlockService.getStartNowStartTime();
                FocusTimeStats focusTimeStats = new FocusTimeStats(startTime, System.currentTimeMillis());
                focusTimeStats.save();
                BlockService.stopStartNow();
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
        long endTime = getIntent().getLongExtra(END_TIME, -1);
        long now = System.currentTimeMillis();

        long countTime = endTime - now;
        if (countTime < 0) {
            countTime += DAY_IN_MILLIS;
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
