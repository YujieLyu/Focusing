package com.example.jessie.focusing.Utils;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import static com.example.jessie.focusing.Utils.AppConstants.STATS_DAYS;

/**
 * @author : Yujie Lyu
 */
public class ChartUtils {

    public static void initBarChart(BarChart chart, boolean isMultiBar) {
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
        xAxis.setDrawGridLines(isMultiBar);
        xAxis.setCenterAxisLabels(isMultiBar);
        xAxis.setValueFormatter((value, axis) -> {
            int idx = (int) value;
            if (idx < 0 || idx >= STATS_DAYS) {
                return "";
            }
            return TimeHelper.getDayOfWeek(STATS_DAYS - 1 - idx);
        });


    }
}
