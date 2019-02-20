package com.example.jessie.focusing.View.Countdown;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.jessie.focusing.Model.AppInfoManager;
import com.example.jessie.focusing.Model.FocusTimeManager;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Service.LockService;
import com.example.jessie.focusing.Utils.LockUtil;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.Finish.FinishActivity;
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

    private static final String TAG = CountdownActivity.class.getSimpleName();
    private long countTime;
    private CountdownView cdv_count;
    private AppInfoManager appInfoManager;
    private FocusTimeManager focusTimeManager;

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
            LockService.stopStartNow(this);
        });
        appInfoManager = new AppInfoManager();
        focusTimeManager = new FocusTimeManager();
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
        long remainTime = cdv_count.getRemainTime();
        long timeSummary = countTime - remainTime;
        if (timeSummary < 0) {
            throw new IllegalArgumentException("Time summary is: " + timeSummary);
        }
        focusTimeManager.saveOrUpdateTime(timeSummary);
        appInfoManager.reset(Profile.START_NOW_PROFILE_ID);
    }

    protected void initData() {
        long endTime = getIntent().getLongExtra(END_TIME, -1);
        long currTimeStart = System.currentTimeMillis();
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
//        appInfoManager.reset(Profile.START_NOW_PROFILE_ID);
//        moveTaskToBack(true);
        Intent intent = new Intent(CountdownActivity.this, FinishActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("countTime", countTime);
        startActivity(intent);
//        finishAndRemoveTask();//Kill the activity
        this.finish();
    }

}
