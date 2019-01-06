package com.example.jessie.focusing.view;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_screeen);
        suggestInfo=findViewById(R.id.tv_suggestInfo);
        lockLayout =findViewById(R.id.lock_layout);
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

        //初始化
        packageManager = getPackageManager();

        initLayoutBackground();

    }

    private void initLayoutBackground() throws PackageManager.NameNotFoundException {
        appInfo = packageManager.getApplicationInfo(pkgName, PackageManager.MATCH_UNINSTALLED_PACKAGES);
        if (appInfo != null) {
            suggestInfo.setText("You cannot open the App  Please keep focusing");
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
