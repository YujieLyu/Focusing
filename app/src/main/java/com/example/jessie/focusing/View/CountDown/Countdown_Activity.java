package com.example.jessie.focusing.View.CountDown;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jessie.focusing.Utils.AppConstants;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Service.LockService;
import com.example.jessie.focusing.Utils.LockUtil;

import cn.iwgang.countdownview.CountdownView;

/**
 * @author : Yujie Lyu
 * @date : 11-01-2019
 * @time : 10:14
 */
public class Countdown_Activity extends AppCompatActivity implements CountdownView.OnCountdownEndListener {


    private long startTime,endTime,countTime;
    private String clickBackFromLock;//按返回键
    private RelativeLayout countLayout;
    private CountdownView cdv_count;
    private TextView tv_suggestInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        cdv_count = findViewById(R.id.cdv_count);
        cdv_count.setOnCountdownEndListener(this);
        tv_suggestInfo = findViewById(R.id.tv_suggestInfo);
        countLayout = findViewById(R.id.cd_main_view);
        setStatusTransparent();
        initData();
        Intent intent = new Intent(this, LockService.class);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        startService(intent); //TODO
    }

    protected void initData() {
//        String timeInput = getIntent().getStringExtra("countTime");
        final long DAY=86400000;
        clickBackFromLock = getIntent().getStringExtra(AppConstants.PRESS_BACK);
        endTime = getIntent().getLongExtra("endTime", 0);

//        clickBackFromMain=AppConstants.BACK_TO_FINISH;
        long currTime = System.currentTimeMillis();
        if (currTime < endTime) {
            countTime = endTime - currTime;
        }else {
            countTime=endTime-currTime+DAY;
        }
//        String[] count = timeInput.split(":");
//        int hour = Integer.parseInt(count[0]);
//        int min = Integer.parseInt(count[1]);
//        long countTime = (long) (hour * 60 * 60 * 1000 + min * 60 * 1000);
        cdv_count.start(countTime);
//        initLayoutBackground();

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

    private void initLayoutBackground() {

        Resources resources = this.getResources();
        final Drawable bgPic = resources.getDrawable(R.drawable.bg_aurora);
        countLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        countLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        countLayout.buildDrawingCache();
                        Bitmap bmp = LockUtil.drawableToBitmap(bgPic, countLayout);
                        LockUtil.blur(Countdown_Activity.this, LockUtil.big(bmp), countLayout);  //高斯模糊
                        return true;
                    }
                });

    }

    @Override
    public void onBackPressed() {
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
//        Intent intent=new Intent(Countdown_Activity.this,Finish_Activity.class);
//        intent.putExtra("countTime",countTime);
//        startActivity(intent);
//        finishAndRemoveTask();//Kill the activity

    }
}
