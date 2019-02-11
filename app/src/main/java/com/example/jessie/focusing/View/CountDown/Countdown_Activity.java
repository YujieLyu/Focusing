package com.example.jessie.focusing.View.CountDown;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jessie.focusing.Model.AppInfoManager;
import com.example.jessie.focusing.Model.FocusTimeManager;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Service.LockService;
import com.example.jessie.focusing.Utils.LockUtil;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.Finish.Finish_Activity;
import com.example.jessie.focusing.View.Main.Main_Activity;

import cn.iwgang.countdownview.CountdownView;

import static com.example.jessie.focusing.Service.LockService.END_TIME;
import static com.example.jessie.focusing.Utils.AppConstants.ONE_DAY;

/**
 * @author : Yujie Lyu
 * @date : 11-01-2019
 * @time : 10:14
 */
public class Countdown_Activity extends AppCompatActivity implements CountdownView.OnCountdownEndListener {
    private static final String TAG = Countdown_Activity.class.getSimpleName();
    private long startTime, endTime, countTime;
    private String clickBackFromLock;//按返回键
    private RelativeLayout countLayout;
    private CountdownView cdv_count;
    private TextView tv_suggestInfo;
    private Button btn_stop;
    private Handler handler;
    private AppInfoManager appInfoManager;
    private FocusTimeManager focusTimeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        processExtraData();
        cdv_count = findViewById(R.id.cdv_count);
        cdv_count.setOnCountdownEndListener(this);
        tv_suggestInfo = findViewById(R.id.tv_suggestInfo);
        countLayout = findViewById(R.id.cd_main_view);
        btn_stop = findViewById(R.id.btn_cancel_countdown);
        btn_stop.setOnClickListener(v -> {
            //TODO: if can only cancel but not return to Home page?
            finish();
            cdv_count.stop();
            LockService.START_NOW_END_TIME = -1;
            Intent intent = new Intent(Countdown_Activity.this, Main_Activity.class);
            startActivity(intent);
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
        LockService.START_NOW_END_TIME = -1;

    }

    protected void initData() {
        endTime = getIntent().getLongExtra(END_TIME, -1);
        long currTimeStart = System.currentTimeMillis();
        if (currTimeStart < endTime) {
            countTime = endTime - currTimeStart;
        } else {
            countTime = endTime - currTimeStart + ONE_DAY;
        }
        cdv_count.start(countTime);
    }

    @Override
    public void onBackPressed() {
        LockUtil.goHome(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        getIntent(); //will return the old one
        processExtraData();
    }

    @Override
    public void onEnd(CountdownView cv) {
        cv.stop();
        LockService.START_NOW_END_TIME = -1;
//        appInfoManager.reset(Profile.START_NOW_PROFILE_ID);
//        moveTaskToBack(true);
        Intent intent = new Intent(Countdown_Activity.this, Finish_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("countTime", countTime);
        startActivity(intent);
//        finishAndRemoveTask();//Kill the activity
        this.finish();
    }

    private void processExtraData() {
//        Intent intent = getIntent();
        //use the data received here
    }
}
