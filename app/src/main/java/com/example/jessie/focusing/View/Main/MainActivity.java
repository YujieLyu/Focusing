package com.example.jessie.focusing.View.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amitshekhar.DebugDB;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.Profile.ProfileListActivity;
import com.example.jessie.focusing.View.Schedule.Schedule_Activity;
import com.example.jessie.focusing.View.Shared.BaseSingleTaskActivity;
import com.example.jessie.focusing.View.StartNow.StartNowActivity;
import com.example.jessie.focusing.View.Stats.DataStatisticActivity;

public class MainActivity extends BaseSingleTaskActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DebugDB.getAddressLog();
        StatusBarUtil.setStatusTransparent(this);
        TextView tv_start = findViewById(R.id.tv_start_now);
        tv_start.setOnClickListener(this);
        TextView tv_profile = findViewById(R.id.tv_profile);
        tv_profile.setOnClickListener(this);
        TextView tv_schedule = findViewById(R.id.tv_schedule);
        tv_schedule.setOnClickListener(this);
        TextView tv_statistic = findViewById(R.id.tv_statistic);
        tv_statistic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Class<? extends Activity> cls = null;
        switch (id) {
            case R.id.tv_start_now:
                cls = StartNowActivity.class;
                break;
            case R.id.tv_profile:
                cls = ProfileListActivity.class;
                break;
            case R.id.tv_schedule:
                cls = Schedule_Activity.class;
                break;
            case R.id.tv_statistic:
                cls = DataStatisticActivity.class;
                break;
        }
        if (cls != null) {
            Intent intent = new Intent(this, cls);
            startActivity(intent);
        }
    }
}
