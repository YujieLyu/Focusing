package com.example.jessie.focusing.View.Schedule;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.jessie.focusing.Controller.Adapter.ScheduleListAdapter;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.CountDown.Countdown_Activity;

import java.util.Calendar;

import static com.example.jessie.focusing.Utils.RenderScriptBlur.rsBlur;


/**
 * @author : Yujie Lyu
 * @date : 19-01-2019
 * @time : 01:02
 */
public class Schedule_Activity extends AppCompatActivity {
    private ScheduleListAdapter scheduleListAdapter;
    private ListView lv_profInSchedule;
    private RelativeLayout scheduleLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        lv_profInSchedule = findViewById(R.id.lv_prof_list);
        scheduleListAdapter = new ScheduleListAdapter(this);
        lv_profInSchedule.setAdapter(scheduleListAdapter);
        scheduleLayout=findViewById(R.id.schedulelayout);
        initData();
//        initLayoutBackground();
        StatusBarUtil.setStatusTransparent(this);
        StatusBarUtil.setDarkStatusIcon(this, true);
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        scheduleListAdapter.setData(today);

    }
    private void initLayoutBackground() {

        final Resources resources = this.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.fruitclock);
        Bitmap b=rsBlur(Schedule_Activity.this, bmp, 25);
        scheduleLayout.setBackground(new BitmapDrawable(b));

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initData();
    }
}
