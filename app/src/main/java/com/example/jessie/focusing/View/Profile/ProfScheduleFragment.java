package com.example.jessie.focusing.View.Profile;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessie.focusing.Interface.TimeCallBack;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.Model.WeekDays;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.widget.TimePickerFragment;

import static com.example.jessie.focusing.Utils.AppConstants.PROFILE_ID;

/**
 * @author : Yujie Lyu
 * @date : 26-01-2019
 * @time : 21:58
 */
public class ProfScheduleFragment extends Fragment implements View.OnClickListener, TimeCallBack {
    private TimePickerFragment tpStart, tpEnd;
    private TextView tv_profName;
    private TextView tv_startTime;
    private TextView tv_endTime;
    private TextView tv_repeat;
    private Profile profile;
    private boolean[] choices;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prof_schedule, container, false);
        Bundle args = getArguments();
        if (args != null) {
            int profileId = args.getInt(PROFILE_ID, -1);
            profile = Profile.findById(profileId);
        }
        if (profile == null) {
            throw new IllegalArgumentException("profileId is invalid.");
        }
        tv_profName = view.findViewById(R.id.tv_title);
        LinearLayout l_start = view.findViewById(R.id.layout_starttime);
        l_start.setOnClickListener(this);
        LinearLayout l_end = view.findViewById(R.id.layout_endtime);
        l_end.setOnClickListener(this);
        LinearLayout l_repeat = view.findViewById(R.id.layout_repeat);
        l_repeat.setOnClickListener(this);
        tv_startTime = view.findViewById(R.id.prefer_start_time);
        tv_endTime = view.findViewById(R.id.prefer_end_time);
        tv_repeat = view.findViewById(R.id.prefer_repeat);

        Button btn_save = view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Profile save successfully:)", Toast.LENGTH_LONG).show();
            getActivity().onBackPressed();
        });
        initView();
        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
        updateData();
    }

    private void initView() {
        tv_startTime.setText(profile.getStartTimeStr());
        tv_endTime.setText(profile.getEndTimeStr());
        tv_repeat.setText(WeekDays.toString(profile.getRepeatId()));
        tv_profName.setText(profile.getProfileName());
        choices = WeekDays.toChoices(profile.getRepeatId());
    }


    /**
     * Save changes, update DB
     */
    public void updateData() {
        String[] startTime = tv_startTime.getText().toString().split(":");
        int startHour = Integer.parseInt(startTime[0]);
        int startMin = Integer.parseInt(startTime[1]);
        String[] endTime = tv_endTime.getText().toString().split(":");
        int endHour = Integer.parseInt(endTime[0]);
        int endMin = Integer.parseInt(endTime[1]);
        profile.setStartHour(startHour);
        profile.setStartMin(startMin);
        profile.setEndHour(endHour);
        profile.setEndMin(endMin);
        profile.setRepeatId(WeekDays.toValue(choices));
        profile.saveOrUpdate();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_starttime:
                tpStart = new TimePickerFragment();
                tpStart.setCallBackListener(this);
                tpStart.show(getActivity().getSupportFragmentManager(), "");
                break;
            case R.id.layout_endtime:
                tpEnd = new TimePickerFragment();
                tpEnd.setCallBackListener(this);
                tpEnd.show(getActivity().getSupportFragmentManager(), "");
                break;

            case R.id.layout_repeat:
                showSingleChoiceDialog();

                break;
        }
    }

    @Override
    public void onTimeSet(TimePickerFragment tp, String displayTime) {
        if (tp == tpStart) {
            tv_startTime.setText(displayTime);
        }
        if (tp == tpEnd) {
            tv_endTime.setText(displayTime);
        }
    }

    private void showSingleChoiceDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Repeat")
                .setMultiChoiceItems(WeekDays.names(), choices, (dialog, which, isChecked) -> {
                    choices[which] = isChecked;
                }).setPositiveButton("OK", (dialog, which) -> {
            tv_repeat.setText(WeekDays.toString(choices));
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
