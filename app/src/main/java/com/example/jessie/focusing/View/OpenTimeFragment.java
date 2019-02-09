package com.example.jessie.focusing.View;

import com.example.jessie.focusing.Model.AppUsage;
import com.example.jessie.focusing.R;

/**
 * @author : Yujie Lyu
 * @date : 08-02-2019
 * @time : 02:41
 */
public class OpenTimeFragment extends UsageTemplateFragment {
    public OpenTimeFragment() {
        layoutId = R.layout.fragment_app_open_detail;
        chartId = R.id.open_times_barchart;
    }

    @Override
    protected float getOutFocusData(AppUsage appUsage) {
        return appUsage == null ? 0 : appUsage.getOpenOutFocus();
    }

    @Override
    protected float getInFocusData(AppUsage appUsage) {
        return appUsage == null ? 0 : appUsage.getOpenInFocus();
    }


}
