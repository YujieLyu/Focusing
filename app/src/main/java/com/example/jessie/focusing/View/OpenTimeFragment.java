package com.example.jessie.focusing.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jessie.focusing.Controller.Adapter.UsageListAdapter;
import com.example.jessie.focusing.Model.AppUsage;
import com.example.jessie.focusing.R;
import com.github.mikephil.charting.charts.BarChart;

import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 08-02-2019
 * @time : 02:41
 */
public class OpenTimeFragment extends Fragment {
    private UsageListAdapter usageListAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_open_detail, container, false);
        BarChart mBarChart = view.findViewById(R.id.open_times_barchart);
        return view;
    }

    private void initData() {


    }

}
