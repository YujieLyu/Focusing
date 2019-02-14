package com.example.jessie.focusing.View.Shared;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * @author : Yujie Lyu
 */
public abstract class BaseSingleTaskActivity extends AppCompatActivity {
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}
