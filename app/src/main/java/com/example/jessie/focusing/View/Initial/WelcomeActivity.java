package com.example.jessie.focusing.View.Initial;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.AppInfoUtils;
import com.example.jessie.focusing.Utils.ShredPreferenceUtils;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.Main.MainActivity;
import com.example.jessie.focusing.View.Shared.BaseSingleTaskActivity;

import static com.example.jessie.focusing.Utils.LockUtil.hasUsagePermission;

public class WelcomeActivity extends BaseSingleTaskActivity {
    private static final int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;
    private ImageView imgWelcome;
    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        imgWelcome = findViewById(R.id.welcome_img);
        StatusBarUtil.setStatusTransparent(this);
        ShredPreferenceUtils.getInstance().init(this);
        initData();
    }

    protected void onDestroy() {
        super.onDestroy();
        animator = null;
    }

    protected void initData() {
        animator = ObjectAnimator.ofFloat(imgWelcome, "alpha", 0.5f, 1);
        animator.setDuration(500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!hasUsagePermission(WelcomeActivity.this)) {
                    PermissionDialog dialog = new PermissionDialog(WelcomeActivity.this);
                    dialog.show();
                    dialog.setOnClickListener(() -> {
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                    });
                } else {
                    toMainActivity();
                }
            }
        });
    }

    private void toMainActivity() {
//        SeedData.initialize(); // dummy data for experiment
        AppInfoUtils appInfoUtils = new AppInfoUtils(this);
        appInfoUtils.scanAppsList();
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
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
                if (hasUsagePermission(WelcomeActivity.this)) {
                    toMainActivity();
                } else {
                    finish();
                }
                break;
        }
    }

}
