package com.example.jessie.focusing.View.StartNow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Service.LockService;
import com.example.jessie.focusing.Utils.BaseTimeChangeListener;
import com.example.jessie.focusing.Utils.TimeHelper;
import com.example.jessie.focusing.View.Countdown.CountdownActivity;
import com.example.jessie.focusing.widget.CirclePicker;

import static com.example.jessie.focusing.Utils.AppConstants.END_TIME;
import static com.example.jessie.focusing.Utils.AppConstants.START_TIME;
import static com.example.jessie.focusing.Utils.TimeHelper.HOUR_IN_MILLIS;

/**
 * @author : Yujie Lyu
 * @date : 24-01-2019
 * @time : 08:24
 */
public class NowClockFragment extends Fragment {
    private static String TAG = NowClockFragment.class.getSimpleName();
    private TextView tv_startTime, tv_endTime, tv_countTime;
    private TimeReceiver timeReceiver = new TimeReceiver();

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startnow_timepicker, container, false);
        tv_countTime = view.findViewById(R.id.tv_countTime);
        tv_startTime = view.findViewById(R.id.tc_start_time);
        tv_endTime = view.findViewById(R.id.tv_end_time);
        CirclePicker circlePicker = view.findViewById(R.id.timer);
        long now = System.currentTimeMillis();
        updateTime(now, now + HOUR_IN_MILLIS);
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        getContext().registerReceiver(timeReceiver, filter);
        circlePicker.setInitialTime(now, now + HOUR_IN_MILLIS);
        circlePicker.setOnTimerChangeListener(new BaseTimeChangeListener() {
            @Override
            public void onEndTimeChanged(long startTime, long endTime) {
                updateTime(startTime, endTime);
            }
        });
        Button btnStart = view.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> {
            long startTime = (long) tv_startTime.getTag();
            long endTime = (long) tv_endTime.getTag();
            if (endTime <= startTime) {
                Toast toast = Toast.makeText(getContext(),
                        R.string.invalid_time_suggestion, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                Intent intent = new Intent(getActivity(), CountdownActivity.class);
                intent.putExtra(CountdownActivity.IS_START_NOW, true);
                intent.putExtra(END_TIME, endTime);
                intent.putExtra(START_TIME, startTime);
                startActivity(intent);
            }
        });
        return view;

    }

    @Override
    public void onStop() {
        super.onStop();
        long startTime = (long) tv_startTime.getTag();
        long endTime = (long) tv_endTime.getTag();
        LockService.startNow(getContext(), startTime, endTime);
        getContext().unregisterReceiver(timeReceiver);
    }

    private void updateTime(long startTime) {
        String start = TimeHelper.toString(startTime);
        tv_startTime.setText(start);
        tv_startTime.setTag(startTime);
        long endTime = (long) tv_endTime.getTag();
        tv_countTime.setText(TimeHelper.calcDuration(startTime, endTime));
        Log.i(TAG, "Start Time updated: " + start);
    }

    private void updateTime(long startTime, long endTime) {
        tv_startTime.setText(TimeHelper.toString(startTime));
        tv_startTime.setTag(startTime);
        tv_endTime.setText(TimeHelper.toString(endTime));
        tv_endTime.setTag(endTime);
        tv_countTime.setText(TimeHelper.calcDuration(startTime, endTime));
    }

    private class TimeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                updateTime(System.currentTimeMillis());
            }
        }
    }
}
