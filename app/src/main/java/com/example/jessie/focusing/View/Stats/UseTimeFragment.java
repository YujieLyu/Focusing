package com.example.jessie.focusing.View.Stats;

import android.graphics.Color;

import com.example.jessie.focusing.Model.AppUsage;
import com.example.jessie.focusing.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;

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
//        dataFormatter = (value, entry, dataSetIndex, viewPortHandler) -> String.format(Locale.getDefault(), "%.0f mins", value);
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
    protected void setData(BarChart chart) {
        BarDataSet set2;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
//            set1.setValues(focusTime);
            set2.setValues(openTimes);
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

            set2 = new BarDataSet(openTimes, "Used Time");
            set2.setDrawIcons(false);
            set2.setColor(colors[1]);

            BarData barData = new BarData(set2);
            barData.setValueFormatter(dataFormatter);
            barData.setValueTextColor(Color.GRAY);
            barData.setValueTextSize(11);
            chart.setData(barData);
        }
        chart.setFitBars(true);
        chart.invalidate();
    }
}
