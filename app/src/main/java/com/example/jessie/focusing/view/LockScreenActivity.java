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
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.jessie.focusing.AppConstants;
import com.example.jessie.focusing.LockUtil;
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
    private TextView suggestInfo;
    private RelativeLayout unLockLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_screeen);
        suggestInfo=findViewById(R.id.tv_suggestInfo);
        unLockLayout =findViewById(R.id.unlock_layout);
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        initData();
        try {
            initLayoutBackground();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    protected void initData() {
        //获取解锁应用的包名
        pkgName = getIntent().getStringExtra(AppConstants.LOCK_PACKAGE_NAME);

        //初始化
        packageManager = getPackageManager();


    }

    private void initLayoutBackground() throws PackageManager.NameNotFoundException {
        appInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
        suggestInfo.setText("You cannot open the App /n Please keep focusing");
        unLockLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        unLockLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        unLockLayout.buildDrawingCache();
                        final Drawable icon = packageManager.getApplicationIcon(appInfo);
                        unLockLayout.setBackgroundDrawable(icon);
                        Bitmap bmp = LockUtil.drawableToBitmap(icon, unLockLayout);
                        LockUtil.blur(LockScreenActivity.this, LockUtil.big(bmp), unLockLayout);  //高斯模糊
                        return true;
                    }
                });
    }


}
