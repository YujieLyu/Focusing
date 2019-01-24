package com.example.jessie.focusing.View.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.AppConstants;
import com.example.jessie.focusing.Utils.LockUtil;
import com.example.jessie.focusing.View.Main_Activity;

/**
 * @author : Yujie Lyu
 * @date : 24-01-2019
 * @time : 17:44
 */
public class ProfileDisplayFragment extends Fragment implements View.OnClickListener {
    private TextView tv_suggestion, tv_p1, btn_new;
//    private Button btn_new;

    public static ProfileDisplayFragment newInstance() {
        return new ProfileDisplayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_display, container, false);
        tv_suggestion = view.findViewById(R.id.tv_suggest_profile);
        tv_p1 = view.findViewById(R.id.tv_p1);
        tv_p1.setOnClickListener(this);
        btn_new = view.findViewById(R.id.btn_new);
        tv_p1.setBackgroundColor(Color.argb(150, 255, 255, 255)); //背景透明度
        btn_new.setBackgroundColor(Color.argb(90, 255, 255, 255));
        return view;

    }

    public void onBackPressed() {
        Intent intent = new Intent(getActivity(), Main_Activity.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        AppListProfileFragment fragment = new AppListProfileFragment();
        Bundle args = new Bundle();
        args.putInt("Profile", v.getId());
        fragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.fg_display, fragment)
                .commit();
    }
}
