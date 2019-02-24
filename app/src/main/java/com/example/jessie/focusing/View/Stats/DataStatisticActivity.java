package com.example.jessie.focusing.View.Stats;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jessie.focusing.Controller.Adapter.UsageListAdapter;
import com.example.jessie.focusing.Model.AppUsage;
import com.example.jessie.focusing.Model.FocusTimeManager;
import com.example.jessie.focusing.Model.UsageManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.Utils.TimeHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.jessie.focusing.Utils.AppConstants.STATS_DAYS;

public class DataStatisticActivity extends AppCompatActivity {
    private UsageListAdapter usAdapter;
    private List<BarEntry> focusTime;
    private FocusTimeManager focusTimeManager = new FocusTimeManager();
    private float savedTime;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_statistic);
        BarChart mBarChart = findViewById(R.id.bar_chart);
        initData();
        initBarChart(mBarChart);
        setData(mBarChart);
        StatusBarUtil.setStatusTransparent(this);
        StatusBarUtil.setDarkStatusIcon(this, true);
        usAdapter = new UsageListAdapter(this);
        initUsageList();
        ListView lv_apps = findViewById(R.id.used_app_list);
        lv_apps.setAdapter(usAdapter);
        TextView tvSavedTime = findViewById(R.id.saved_time);
        String tvText = tvSavedTime.getText().toString();
        String newTxt = String.format(Locale.getDefault(), "%s%.0f mins", tvText, savedTime);
        tvSavedTime.setText(newTxt);
    }

    private void initData() {
        savedTime = 0;
        focusTime = new ArrayList<>();
        for (int i = 0; i < STATS_DAYS; i++) {
//            FocusTimeStats time = focusTimeManager.getTimeData(STATS_DAYS - 1 - i);
//            float t = time == null ? 0 : (float) (time.getTime() / 1000 / 60);
            long totalTime = focusTimeManager.getTotalFocusTime(STATS_DAYS - 1 - i);
            float t = (float) (totalTime / 1000 / 60);
            savedTime += t;
            focusTime.add(new BarEntry(i, t));
        }
    }

    private void setData(BarChart chart) {

        BarDataSet set1;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(focusTime);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(focusTime, "Focus Time");
            set1.setDrawIcons(false);
            set1.setColors(R.color.colorPrimary);
            BarData barData = new BarData(set1);
//            barData.setValueFormatter((value, entry, dataSetIndex, viewPortHandler) -> String.format(Locale.getDefault(), "%.0f mins", value));
            barData.setValueFormatter(new DefaultValueFormatter(0));

            barData.setValueTextColor(Color.GRAY);
            barData.setValueTextSize(11);
            chart.setData(barData);
        }
        chart.setFitBars(true);
        chart.invalidate();
    }

    private void initUsageList() {
        UsageManager usageManager = new UsageManager(this);
        List<AppUsage> appUsages = usageManager.getMostUsedAppsInDays(-7);
        usAdapter.setData(appUsages);
    }

    private void initBarChart(BarChart chart) {

        chart.setBackgroundColor(Color.WHITE);
        chart.getDescription().setEnabled(false);
        chart.animateY(500, Easing.Linear);
        chart.setDrawValueAboveBar(true);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        YAxis leftAxis = chart.getAxisLeft();
        YAxis rightAxis = chart.getAxisRight();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setEnabled(false);
        rightAxis.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter((value, axis) -> {
            int idx = (int) value;
            if (idx < 0 || idx >= STATS_DAYS) {
                return "";
            }
            return TimeHelper.getDayOfWeek(STATS_DAYS - 1 - idx);
        });


    }

}