package com.example.jessie.focusing.View.Schedule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.ListView;

import com.example.jessie.focusing.Controller.Adapter.ScheduleListAdapter;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;

import java.util.Calendar;


/**
 * @author : Yujie Lyu
 * @date : 19-01-2019
 * @time : 01:02
 */
public class ScheduleActivity extends AppCompatActivity {
    private ScheduleListAdapter scheduleListAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ListView lv_profInSchedule = findViewById(R.id.lv_prof_list);
        scheduleListAdapter = new ScheduleListAdapter(this);
        lv_profInSchedule.setAdapter(scheduleListAdapter);
        CalendarView calendarView = findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            initData(calendar);
        });
        initData(Calendar.getInstance());
        StatusBarUtil.setStatusTransparent(this);
        StatusBarUtil.setDarkStatusIcon(this, true);
    }

    private void initData(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        scheduleListAdapter.setData(dayOfWeek);
    }

}
