package com.example.jessie.focusing.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jessie.focusing.R;
import com.github.mikephil.charting.charts.BarChart;

/**
 * @author : Yujie Lyu
 * @date : 08-02-2019
 * @time : 02:41
 */
public class UseTimeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_use_time, container, false);
        BarChart mBarChart = view.findViewById(R.id.use_time_barchart);
        return view;
    }
}
