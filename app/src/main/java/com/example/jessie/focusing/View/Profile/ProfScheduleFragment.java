package com.example.jessie.focusing.View.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.jessie.focusing.Model.AppInfoManager;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.Model.ProfileManager;
import com.example.jessie.focusing.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author : Yujie Lyu
 * @date : 26-01-2019
 * @time : 21:58
 */
public class ProfScheduleFragment extends Fragment implements View.OnClickListener,
        OnDismissListener, DeleteProfileDialog.OnFragmentInteractionListener {
    private TextView tv_profName, tv_startTime, tv_endTime, tv_alarm, tv_repeat;
    //    private EditText ed_profName;
    private Button btn_delete, btn_save;
    private LinearLayout l_start, l_end, l_alarm, l_repeat;
    private int repeatId;
    private ProfileManager profileManager;
    private AppInfoManager appInfoManager;
    private int profileId;
    private Profile profile;
    private List<String> alarmNumOptions, alarmUnitOptions, repeatOptions;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prof_schedule, container, false);
        l_start = view.findViewById(R.id.layout_starttime);
        l_start.setOnClickListener(this);
        l_end = view.findViewById(R.id.layout_endtime);
        l_end.setOnClickListener(this);
//        l_alarm = view.findViewById(R.id.layout_alarm);
//        l_alarm.setOnClickListener(this);
        l_repeat = view.findViewById(R.id.layout_repeat);
        l_repeat.setOnClickListener(this);
        alarmNumOptions = Arrays.asList("1", "5", "10", "30");
        alarmUnitOptions = Arrays.asList("Minutes", "Hours", "Days");
        repeatOptions = Arrays.asList("Everyday", "Every Monday", "Every Tuesday",
                "Every Wednesday", "Every Thursday", "Every Friday", "Every Saturday",
                "Every Sunday");

        tv_startTime = view.findViewById(R.id.prefer_start_time);
        tv_endTime = view.findViewById(R.id.prefer_end_time);
//        tv_alarm = view.findViewById(R.id.prefer_alarm);
        tv_repeat = view.findViewById(R.id.prefer_repeat);
        profileId = getArguments().getInt("ProfileId", -1);
        btn_save = view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
                Toast.makeText(getContext(), "Profile save successfully:)", Toast.LENGTH_LONG).show();

            }
        });
        //TODO: should remove this button
        btn_delete = view.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               showDeleteProfileDialog();
                profileManager.deleteProfile(profileId);
                appInfoManager.deleteByProfId(profileId);
                Toast.makeText(getContext(), "Delete done:)", Toast.LENGTH_LONG).show();
                getActivity().finish();//todo:标记一下，返回的时候非常有用
//                getActivity().getSupportFragmentManager().popBackStack();//todo:fragment的返回方法
//                Intent intent = new Intent(getActivity(), ProfileList_Activity.class);
//                getActivity().startActivity(intent);
            }

        });
        profileManager = new ProfileManager(getContext());
        appInfoManager = new AppInfoManager(getContext());
        initData();
        tv_startTime.setText(String.format("%02d:%02d", profile.getStartHour(), profile.getStartMin()));
        tv_endTime.setText(String.format("%02d:%02d", profile.getEndHour(), profile.getEndMin()));
        tv_repeat.setText(profile.getRepeat());
//        tv_alarm.setText(profile.getAlarm());
        tv_profName = view.findViewById(R.id.set_prof_name);
        tv_profName.setText(profile.getProfileName());

        return view;
    }

    private void showDeleteProfileDialog() {
        DeleteProfileDialog dialog = new DeleteProfileDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "DeleteProfile");

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onStop() {
        super.onStop();
        updateData();

    }

    private void initData() {
        profile = profileManager.syncProfileDetail(profileId);
    }

    //save changes, update DB
    public void updateData() {
        String[] startTime = tv_startTime.getText().toString().split(":");//todo:把小时数赋给它
        int startHour = Integer.parseInt(startTime[0]);
        int startMin = Integer.parseInt(startTime[1]);//todo:把分钟数赋给它；
        String[] endTime = tv_endTime.getText().toString().split(":");
        int endHour = Integer.parseInt(endTime[0]);//todo：把小时数赋给它
        int endMin = Integer.parseInt(endTime[1]);//todo:分钟数
        profile.setStartHour(startHour);
        profile.setStartMin(startMin);
        profile.setEndHour(endHour);
        profile.setEndMin(endMin);
//        profile.setAlarm(tv_alarm.getText().toString());
        String repeatType=tv_repeat.getText().toString();
        repeatId=getcorrRepeatId(repeatType);
        profile.setRepeat(repeatType);
        profile.setRepeatId(repeatId);//todo:排查repeatId录入情况
        profileManager.updateProfile(profile);
    }


    private int getcorrRepeatId(String s) {
        int repeatId=0;
        switch (s) {
            case "Everyday":
                repeatId = 0;
                break;
            case "Every Monday":
                repeatId = 1;
                break;
            case "Every Tuesday":
                repeatId=2;
                break;
            case "Every Wednesday":
                repeatId=3;
                break;
            case "Every Thursday":
                repeatId=4;
                break;
            case "Every Friday":
                repeatId=5;
                break;
            case "Every Saturday":
                repeatId=6;
                break;
            case  "Every Sunday":
                repeatId=7;
                break;

        }
        return repeatId;
    }

    /**
     * Display a wheel time picker to choose specific time
     *
     * @param tvToShow the {@link TextView} to show the time
     */
    private TimePickerView pickTime(final TextView tvToShow) {
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.getDefault());
                tvToShow.setText(df.format(date));
                // set tag to compare time
                tvToShow.setTag(date);
            }
        }).setOutSideCancelable(false).isCyclic(true).setType(new boolean[]{false, false, false, true, true, false})
                .build();
        Calendar initTime = Calendar.getInstance();
        Log.i("Time Picker", initTime.getTime().toString());
        pvTime.setDate(initTime);
        return pvTime;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_starttime:
                TimePickerView tvStart = pickTime(tv_startTime);
                tvStart.show();
                break;
            case R.id.layout_endtime:
                TimePickerView tvEnd = pickTime(tv_endTime);
                tvEnd.setOnDismissListener(this);
                tvEnd.show();
                break;
//            case R.id.layout_alarm:
//                OptionsPickerView almOpts = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
//
//                    @Override
//                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
//
//                        String str = alarmNumOptions.get(options1)
//                                + " " + alarmUnitOptions.get(options2)
//                                + " early";
//                        tv_alarm.setText(str);
//
//                    }
//                }).build();
//                almOpts.setNPicker(alarmNumOptions, alarmUnitOptions, null);
//                almOpts.show();
//                break;
            case R.id.layout_repeat:
                OptionsPickerView rptOpts = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {

                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        tv_repeat.setText(repeatOptions.get(options1));

                    }
                }).build();
                rptOpts.setNPicker(repeatOptions, null, null);
                rptOpts.show();
                break;
        }
    }

    @Override
    public void onDismiss(Object o) {
        Date start = (Date) tv_startTime.getTag();
        Date end = (Date) tv_endTime.getTag();
        if (start != null && end != null && end.before(start)) {
            Toast.makeText(getContext(), "Invalid Time", Toast.LENGTH_LONG).show();
            tv_endTime.setText("00:00");
        }
    }

    @Override
    public void onFragmentInteraction() {
        profileManager.deleteProfile(profile.getId());
        Intent intent = new Intent(getActivity(), ProfileDetailActivity.class);
        getActivity().startActivity(intent);

    }
}
