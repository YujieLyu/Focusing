package com.example.jessie.focusing.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.service.LockService;

import cn.iwgang.countdownview.CountdownView;

/**
 * @author : Yujie Lyu
 * @date : 11-01-2019
 * @time : 10:14
 */
public class Countdown_Activity extends AppCompatActivity {

    private CountdownView countdownView;
    private long startTime;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        countdownView = findViewById(R.id.cdv_count);
        initData();
        Intent intent = new Intent(this, LockService.class);
        intent.putExtra("startTime",startTime);
        intent.putExtra("endTime",endTime);
        startService(intent); //TODO
    }

    protected void initData() {
        String timeInput = getIntent().getStringExtra("countTime");
        startTime=getIntent().getLongExtra("startTime",0);
        endTime=getIntent().getLongExtra("endTime",0);
        String[] count = timeInput.split(":");
        int hour = Integer.parseInt(count[0]);
        int min = Integer.parseInt(count[1]);
        long countTime = (long) (hour * 60 * 60 * 1000 + min * 60 * 1000);
        countdownView.start(countTime);

    }


}
