package com.example.jessie.focusing.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jessie.focusing.AppConstants;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.service.LockService;
import com.example.jessie.focusing.utils.LockUtil;

import cn.iwgang.countdownview.CountdownView;

/**
 * @author : Yujie Lyu
 * @date : 11-01-2019
 * @time : 10:14
 */
public class Countdown_Activity extends AppCompatActivity implements CountdownView.OnCountdownEndListener {

    private CountdownView countdownView;
    private long startTime;
    private long endTime;
    private String clickBackFromLock;//按返回键
    //    private String clickBackFromMain;
    private TextView suggestInfo;
    private RelativeLayout cdvLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        countdownView = findViewById(R.id.cdv_count);
        countdownView.setOnCountdownEndListener(this);
        suggestInfo = findViewById(R.id.tv_suggestInfo);
        cdvLayout = findViewById(R.id.cd_main_view);
        //沉浸式状态栏
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initData();
        Intent intent = new Intent(this, LockService.class);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        startService(intent); //TODO
    }

    protected void initData() {
//        String timeInput = getIntent().getStringExtra("countTime");
        clickBackFromLock = getIntent().getStringExtra(AppConstants.PRESS_BACK);
        startTime = getIntent().getLongExtra("startTime", 0);
        endTime = getIntent().getLongExtra("endTime", 0);
//        clickBackFromMain=AppConstants.BACK_TO_FINISH;
        long countTime = 0;
        long currTime = System.currentTimeMillis();
        if (startTime < currTime && currTime < endTime) {
            countTime = endTime - currTime;
        }
//        String[] count = timeInput.split(":");
//        int hour = Integer.parseInt(count[0]);
//        int min = Integer.parseInt(count[1]);
//        long countTime = (long) (hour * 60 * 60 * 1000 + min * 60 * 1000);
        countdownView.start(countTime);
        initLayoutBackground();

    }

    private void initLayoutBackground() {

        Resources resources = this.getResources();
        final Drawable bgPic = resources.getDrawable(R.drawable.bg_pure);
        cdvLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cdvLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        cdvLayout.buildDrawingCache();
                        Bitmap bmp = LockUtil.drawableToBitmap(bgPic, cdvLayout);
                        LockUtil.blur(Countdown_Activity.this, LockUtil.big(bmp), cdvLayout);  //高斯模糊
                        return true;
                    }
                });

    }

    @Override
    public void onBackPressed() {//todo:返回键和home键的稳定性:从countdown返回main是空指针
        if (clickBackFromLock != null && clickBackFromLock.equals(AppConstants.BACK_TO_FINISH)) {
            LockUtil.goHome(this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onEnd(CountdownView cv) {
        cv.stop();
        moveTaskToBack(true);
//        finishAndRemoveTask();
    }
}
