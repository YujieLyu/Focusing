package com.example.jessie.focusing_demo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.jessie.focusing_demo.AppConstants;
import com.example.jessie.focusing_demo.DialogPermission;
import com.example.jessie.focusing_demo.LoadAppListService;
import com.example.jessie.focusing_demo.LockService;
import com.example.jessie.focusing_demo.LockUtil;
import com.example.jessie.focusing_demo.R;
import com.example.jessie.focusing_demo.SPUtil;

public class WelcomeActivity extends AppCompatActivity {
    private ImageView imgWelcome;
    private ObjectAnimator animator;
    private int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //沉浸式状态栏
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        SPUtil.getInstance().init(this);
        initData();
    }

    protected void initData(){
        startService(new Intent(this, LockService.class));
        animator = ObjectAnimator.ofFloat(imgWelcome, "alpha", 0.5f, 1);
        animator.setDuration(1500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                boolean isFirstLock = SPUtil.getInstance().getBoolean(AppConstants.LOCK_IS_FIRST_LOCK, true);
                if (isFirstLock) { //如果第一次
                    showDialog();
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                    intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME); //传自己的包名
//                    intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    private void showDialog() {
        if (!LockUtil.isStatAccessPermissionSet(WelcomeActivity.this) && LockUtil.isNoOption(WelcomeActivity.this)) {
            DialogPermission dialog = new DialogPermission(WelcomeActivity.this);
            dialog.show();
            dialog.setOnClickListener(new DialogPermission.onClickListener() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                }
            });
        } else {
            toMainActivity();
        }
    }
    private void toMainActivity() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    protected void onDestroy() {
        super.onDestroy();
        animator = null;
    }

}
