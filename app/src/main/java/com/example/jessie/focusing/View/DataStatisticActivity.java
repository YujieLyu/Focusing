package com.example.jessie.focusing.View;

import android.os.Bundle;

import com.example.jessie.focusing.Model.FocusTime;
import com.example.jessie.focusing.Model.FocusTimeManager;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;


import com.example.jessie.focusing.R;
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
    private List<Integer> temp=new ArrayList<>();


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
    }

    private void initData() {

        timeData = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.DATE, -7);
        focusTimeManager = new FocusTimeManager();
        focusTimeManager.deleteItem(12);
        timeData = focusTimeManager.getTimeDate(weekBefore, today);

        if (timeData.size()<7){
            int omission;
            omission=7-timeData.size();
            for (int i = 0; i<omission;i++){
                mins.add(0);
            }
        }



        for (FocusTime timeDatum : timeData) {
            temp.add(timeDatum.getDay());
            long minutes=timeDatum.getTime()/(1000 * 60 );//处理一下long转int四舍五入的问题

            mins.add((int)Math.ceil(minutes));
        }


        if (temp.size()<7){

            int min=Collections.min(temp);
            int omission;
            omission=7-temp.size();
            for (int i=0;i<omission;i++){
                min--;
                if (min>0) {
                    dates.add(min);
                }else {
                    min=31;
                    dates.add(min);

                }
            }
        }
        Collections.reverse(dates);
        dates.addAll(temp);

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
        mBarChart.animateY(1000, Easing.EasingOption.Linear);
        mBarChart.animateX(1000, Easing.EasingOption.Linear);

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
                return String.valueOf(mins.get(idx)+" Mins");
            }
        });

        mBarChart.setData(mBarData);


    }
}