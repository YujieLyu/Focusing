package com.example.jessie.focusing.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jessie.focusing.Controller.Adapter.UsageListAdapter;
import com.example.jessie.focusing.Model.AppUsage;
import com.example.jessie.focusing.Model.FocusTime;
import com.example.jessie.focusing.Model.FocusTimeManager;
import com.example.jessie.focusing.Model.UsageManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.Utils.TimeHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DataStatisticActivity extends AppCompatActivity {
    private List<FocusTime> timeData;
    private List<Integer> dates = new ArrayList<>();
    private List<Integer> mins = new ArrayList<>();
    private List<Integer> days = new ArrayList<>();
    private UsageListAdapter usAdapter;
    private FocusTimeManager focusTimeManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_statistic);

        BarChart mBarChart = findViewById(R.id.bar_chart);


        initData();
        initBarChart(mBarChart);
        setBarChartData(dates.size(), mBarChart);
        StatusBarUtil.setStatusTransparent(this);
        StatusBarUtil.setDarkStatusIcon(this, true);
        usAdapter = new UsageListAdapter(this);
        initUsageList();
        ListView lv_apps = findViewById(R.id.used_app_list);
        lv_apps.setAdapter(usAdapter);
    }

    private void initData() {

        timeData = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.DATE, -7);
        focusTimeManager = new FocusTimeManager();
        timeData = focusTimeManager.getTimeDate(weekBefore, today);
        int omission=0;
        if (timeData.size()<7){
            omission=7-timeData.size();
            for (int i = 0; i<omission;i++){
                mins.add(0);//如果没有统计数据，就统一设置时长为0
            }
        }


        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        int day=calendar.get(Calendar.DATE);

        for (FocusTime timeDatum : timeData) {
            dates.add(timeDatum.getDay());
            long minutes=timeDatum.getTime()/(1000 * 60 );//处理一下long转int四舍五入的问题
            mins.add((int)Math.ceil(minutes));
        }


        //一周之内记录日期不足的补位
        int max= dates.size()==0?day:Collections.max(dates);
        int lastDayToAdd=max- dates.size();
        dates.add(lastDayToAdd);

        if (dates.size()<7){
            if (lastDayToAdd>=omission){
                for (int i=0;i<omission;i++){
                    lastDayToAdd--;
                    if (dates.size()<7){
                        dates.add(lastDayToAdd);
                    }else
                        Collections.reverse(dates);
                }

            }else {
                for (int j=0;j<omission;j++){
                    lastDayToAdd--;
                    if (dates.size()<7){
                        if (lastDayToAdd<=0){
                            lastDayToAdd=31;
                        }
                        dates.add(lastDayToAdd);
                    }else
                        Collections.reverse(dates);
                }
            }

        }



    }


    private void initUsageList() {
        UsageManager usageManager = new UsageManager(this);
        List<AppUsage> appUsages = usageManager.getMostUsedAppsInDays(-7);
        usAdapter.setData(appUsages);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final List<AppInfo> appInfos=ScanAppsTool.scanAppsList(LockApp_Activity.this.getPackageManager());
//                appListAdapter.setData(appInfos);
//
//            }
//        }).start();

    }

    private void initBarChart(BarChart mBarChart) {


        mBarChart.setBackgroundColor(Color.WHITE);
        mBarChart.setDrawGridBackground(false); //网格
        mBarChart.getDescription().setEnabled(false);//描述
        //背景阴影
        mBarChart.setDrawBarShadow(false);

        //显示边界
        mBarChart.setDrawBorders(false);

        //设置动画效果
        mBarChart.animateY(1000, Easing.Linear);
        mBarChart.animateX(1000, Easing.Linear);

        //折线图例 标签 设置
        Legend l = mBarChart.getLegend();
        l.setEnabled(false);

        YAxis leftAxis = mBarChart.getAxisLeft();
        YAxis rightAxis = mBarChart.getAxisRight();
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(false);
        rightAxis.setEnabled(false);

        XAxis xAxis = mBarChart.getXAxis();

        //XY轴的设置
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setGranularity(1f);

        //xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        xAxis.setTextColor(0xff74828F);
        xAxis.setTextSize(12f);
        xAxis.setAxisLineColor(0xffe0e0e0);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int idx = (int) value;
                return dates.get(idx).toString();
            }
        });


    }

    private void setBarChartData(int count, BarChart mBarChart) {


        List<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            int val = mins.get(i);
            entries.add(new BarEntry(i, val));

        }


        BarDataSet mBarDataSet = new BarDataSet(entries, "Weekly focus time");

        mBarDataSet.setDrawIcons(false);
        mBarDataSet.setColor(R.color.colorPrimary);
        mBarDataSet.setValueTextSize(12f);
        mBarDataSet.setValueTextColor(R.color.colorPrimary);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(mBarDataSet);

        BarData mBarData = new BarData(dataSets);
        mBarData.setBarWidth(0.6f);

        mBarData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int idx = (int) entry.getX();
                return String.valueOf(mins.get(idx) + " Mins");
            }
        });

        mBarChart.setData(mBarData);


    }
}