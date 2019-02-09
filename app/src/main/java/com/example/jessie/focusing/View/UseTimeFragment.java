package com.example.jessie.focusing.View;

import com.example.jessie.focusing.Model.AppUsage;
import com.example.jessie.focusing.R;

import java.util.Locale;

/**
 * @author : Yujie Lyu
 * @date : 08-02-2019
 * @time : 02:41
 */
public class UseTimeFragment extends UsageTemplateFragment {

    public UseTimeFragment() {
        layoutId = R.layout.fragment_app_use_time;
        chartId = R.id.use_time_barchart;
        dataFormatter = (value, entry, dataSetIndex, viewPortHandler) -> String.format(Locale.getDefault(), "%.2f mins", value);
    }

    @Override
    protected float getOutFocusData(AppUsage appUsage) {
        return appUsage == null ? 0 : (float) (appUsage.getUsedOutFocus() / 1000 / 60.0);
    }

    @Override
    protected float getInFocusData(AppUsage appUsage) {
        return appUsage == null ? 0 : (float) (appUsage.getUsedInFocus() / 1000 / 60.0);
    }
}
