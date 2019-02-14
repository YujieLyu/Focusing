package com.example.jessie.focusing.View.Stats;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jessie.focusing.Model.AppUsage;
import com.example.jessie.focusing.Model.UsageManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.ChartUtils;
import com.example.jessie.focusing.Utils.PackageUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.jessie.focusing.Utils.AppConstants.STATS_DAYS;
import static com.example.jessie.focusing.View.Stats.LogDetailActivity.KEY;

/**
 * @author : Yujie Lyu
 * @date : 08-02-2019
 * @time : 02:41
 */
public abstract class UsageTemplateFragment extends Fragment {
    protected int layoutId;
    protected int chartId;
    protected boolean isMultiBar;
    protected IValueFormatter dataFormatter = new DefaultValueFormatter(0);
    protected List<BarEntry> openTimes;
    protected List<BarEntry> focusTime;
    private UsageManager usageManager;
    private String packageName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        Bundle args = getArguments();
        if (args != null) {
            packageName = args.getString(KEY);
            PackageUtils packageUtils = new PackageUtils(getActivity().getPackageManager());
            String appName = packageUtils.getAppName(packageName);
            if (appName != null) {
                TextView tv_title = view.findViewById(R.id.title);
                tv_title.setText(appName);
            }
        }
        usageManager = new UsageManager(Objects.requireNonNull(getContext()));
        BarChart mBarChart = view.findViewById(chartId);
        ChartUtils.initBarChart(mBarChart, isMultiBar);
        initData();
        setData(mBarChart);
        return view;
    }

    protected abstract float getOutFocusData(AppUsage appUsage);

    protected abstract float getInFocusData(AppUsage appUsage);

    private void initData() {
        openTimes = new ArrayList<>();
        focusTime = new ArrayList<>();
        for (int i = 0; i < STATS_DAYS; i++) {
            AppUsage appUsage = usageManager.getAppUsageOfDay(packageName, STATS_DAYS - 1 - i);
            float out = getOutFocusData(appUsage);
            float in = getInFocusData(appUsage);
            openTimes.add(new BarEntry(i, out));
            focusTime.add(new BarEntry(i, in));
        }
    }

    protected void setData(BarChart chart) {

        BarDataSet set1, set2;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            set1.setValues(focusTime);
            set2.setValues(openTimes);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(focusTime, "In Focus"); // TODO: change name
            set1.setDrawIcons(false);
            int[] colors = new int[]{
                    Color.rgb(255, 102, 0),
                    Color.rgb(104, 241, 175),
            };
            set1.setColors(colors[0]);

            set2 = new BarDataSet(openTimes, "Out Focus");
            set2.setDrawIcons(false);
            set2.setColor(colors[1]);

            BarData barData = new BarData(set1, set2);
            barData.setValueFormatter(dataFormatter);
            barData.setValueTextColor(Color.GRAY);
            barData.setValueTextSize(11);
            chart.setData(barData);
        }
        float groupSpace = 0.08f;
        float barSpace = 0.06f; // x2 DataSet
        float barWidth = 0.4f; // x2 DataSet
        // (0.4 + 0.06) * 4 + 0.08 = 1.00 -> interval per "group"
        chart.getXAxis().setAxisMinimum(0);
        chart.getXAxis().setAxisMaximum(STATS_DAYS);
        chart.getBarData().setBarWidth(barWidth);
        chart.groupBars(0, groupSpace, barSpace);

        chart.setFitBars(true);
        chart.invalidate();
    }
}
