package com.example.jessie.focusing.View.Initial;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.jessie.focusing.Model.SeedData;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Service.LockService;
import com.example.jessie.focusing.Utils.AppConstants;
import com.example.jessie.focusing.Utils.LockUtil;
import com.example.jessie.focusing.Utils.SPUtil;
import com.example.jessie.focusing.View.Main.Main_Activity;

import org.litepal.LitePal;

import static com.example.jessie.focusing.Utils.LockUtil.isStatAccessPermissionSet;

public class Welcome_Activity extends AppCompatActivity {
    private static final int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;
    private ImageView imgWelcome;
    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//        LitePal.deleteDatabase("appDB");//todo: be careful
        setStatusTransparent();
        SPUtil.getInstance().init(this);
        initData();
    }

    protected void onDestroy() {
        super.onDestroy();
        animator = null;
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

    protected void initData() {

        startService(new Intent(this, LockService.class));
        animator = ObjectAnimator.ofFloat(imgWelcome, "alpha", 0.5f, 1);
        animator.setDuration(500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                boolean isFirstLock = SPUtil.getInstance().getBoolean(AppConstants.LOCK_IS_FIRST_LOCK, true);
                if (isFirstLock) { //如果第一次//todo:更改第一次的判断机制，现在的是不太懂这个sputil
                    showDialog();
                } else {
                    Intent intent = new Intent(Welcome_Activity.this, Main_Activity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    private void showDialog() {
        if (!isStatAccessPermissionSet(Welcome_Activity.this) && LockUtil.isNoOption(Welcome_Activity.this)) {
            DialogPermission dialog = new DialogPermission(Welcome_Activity.this);
            dialog.show();
            dialog.setOnClickListener(() -> {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
            });
        } else {
            toMainActivity();
        }
    }

    private void toMainActivity() {
        SeedData.initialize();
        Intent intent = new Intent(Welcome_Activity.this, Main_Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * Get system permission and back to the main activity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case RESULT_ACTION_USAGE_ACCESS_SETTINGS:
                if (isStatAccessPermissionSet(Welcome_Activity.this)) {
                    toMainActivity();
                } else {
                    // mcl: better to prompt msg to users
                    finish();
                }
                break;
        }
    }

}
