package com.example.jessie.focusing.View.Finish;

import android.os.Bundle;
import android.widget.TextView;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.Shared.BaseSingleTaskActivity;

/**
 * @author : Yujie Lyu
 * @date : 15-01-2019
 * @time : 11:15
 */
public class FinishActivity extends BaseSingleTaskActivity {

    public static final String FOCUSED_TIME = "focused_time";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        TextView tv_summary = findViewById(R.id.tv_summary);
        tv_summary.setText(initData());
        StatusBarUtil.setStatusTransparent(this);
        StatusBarUtil.setDarkStatusIcon(this, true);
    }

    public String initData() {

        long timeSummary = getIntent().getLongExtra(FOCUSED_TIME, 0);
        long hours = (timeSummary % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (timeSummary % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (timeSummary % (1000 * 60)) / 1000;
        if (seconds > 30) {
            minutes += 1;
        }
        String displayTime;
        if (hours == 0) {
            displayTime = minutes + " minutes";
        } else {
            displayTime = hours + " hour " + minutes + " minutes";
        }
        return displayTime;


    }

}
