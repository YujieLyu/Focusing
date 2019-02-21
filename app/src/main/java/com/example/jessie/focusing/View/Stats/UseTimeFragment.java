package com.example.jessie.focusing.View.Stats;

import android.graphics.Color;

import com.example.jessie.focusing.Model.AppUsage;
import com.example.jessie.focusing.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import static com.example.jessie.focusing.Utils.AppConstants.STATS_DAYS;

/**
 * @author : Yujie Lyu
 * @date : 08-02-2019
 * @time : 02:41
 */
public class UseTimeFragment extends UsageTemplateFragment {

    public UseTimeFragment() {
        layoutId = R.layout.fragment_app_use_time;
        chartId = R.id.use_time_barchart;
        isMultiBar = false;
    }

    @Override
    protected float getOutFocusData(AppUsage appUsage) {
        return appUsage == null ? 0 : (float) (appUsage.getUsedOutFocus() / 1000 / 60.0);
    }

    @Override
    protected float getInFocusData(AppUsage appUsage) {
        return appUsage == null ? 0 : (float) (appUsage.getUsedInFocus() / 1000 / 60.0);
    }

    @Override
    protected void initData() {
        focusTime = new ArrayList<>();
        for (int i = 0; i < STATS_DAYS; i++) {
            float in = (float) (usageManager.getUsedTime(packageName, STATS_DAYS - 1 - i) / 1000 / 60.0);
            focusTime.add(new BarEntry(i, in));
        }
    }

    @Override
    protected void setData(BarChart chart) {
        BarDataSet set;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set = (BarDataSet) chart.getData().getDataSetByIndex(1);
//            set1.setValues(focusTime);
            set.setValues(focusTime);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
//            set1 = new BarDataSet(focusTime, "In Focus");
//            set1.setDrawIcons(false);
            int[] colors = new int[]{
                    Color.rgb(255, 102, 0),
                    Color.rgb(104, 241, 175),
            };
//            set1.setColors(colors[0]);

            set = new BarDataSet(focusTime, "Used Time");
            set.setDrawIcons(false);
            set.setColor(colors[1]);

            BarData barData = new BarData(set);
            barData.setValueFormatter(dataFormatter);
            barData.setValueTextColor(Color.GRAY);
            barData.setValueTextSize(11);
            chart.setData(barData);
        }
        chart.setFitBars(true);
        chart.invalidate();
    }
}
