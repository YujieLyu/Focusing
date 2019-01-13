package com.example.jessie.focusing.view;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import com.example.jessie.focusing.utils.LockUtil;
import com.example.jessie.focusing.R;

import java.util.Calendar;

import cn.iwgang.countdownview.CountdownView;

/**
 * @author : Yujie Lyu
 * @date : 30-12-2018
 * @time : 16:05
 */
public class LockScreenActivity extends AppCompatActivity {

    private ApplicationInfo appInfo;
    private PackageManager packageManager;
    private String pkgName; //解锁应用的包名
    private String clickBack;//按返回键
    private TextView suggestInfo;
    private RelativeLayout lockLayout;
    private CountdownView cvLockScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_screeen);
        suggestInfo=findViewById(R.id.tv_suggestInfo);
        lockLayout =findViewById(R.id.lock_layout);
        cvLockScreen =findViewById(R.id.cdv_lockScreen);
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        try {
            initData();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void initData() throws PackageManager.NameNotFoundException {
        //获取解锁应用的包名
        pkgName = getIntent().getStringExtra(AppConstants.LOCK_PACKAGE_NAME);
        clickBack=getIntent().getStringExtra(AppConstants.PRESS_BACK);
        long endTime=getIntent().getLongExtra("endTime",0);
        long currTime=System.currentTimeMillis();
        long interval=endTime-currTime;

        //初始化
        packageManager = getPackageManager();
//        long timeTest = (long)30 * 60 * 1000;//todo:仅用作测试，随后传参数
        cvLockScreen.start(interval);//todo: this interval maybe a little bit of different
                                        //todo: with main_activity countdown time

        initLayoutBackground();

    }



    private void initLayoutBackground() throws PackageManager.NameNotFoundException {
        appInfo = packageManager.getApplicationInfo(pkgName, PackageManager.MATCH_UNINSTALLED_PACKAGES);
        if (appInfo != null) {
            suggestInfo.setText(getString(R.string.focusing_suggestion));
            suggestInfo.setTextColor(getResources().getColor(R.color.text_white));
            final Drawable icon=packageManager.getApplicationIcon(appInfo);
            lockLayout.setBackground(icon);
            lockLayout.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            lockLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                            lockLayout.buildDrawingCache();
                            Bitmap bmp = LockUtil.drawableToBitmap(icon, lockLayout);
                            LockUtil.blur(LockScreenActivity.this, LockUtil.big(bmp), lockLayout);  //高斯模糊
                            return true;
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        if (clickBack.equals(AppConstants.BACK_TO_FINISH)) {
            LockUtil.goHome(this);
        }
    }

}
