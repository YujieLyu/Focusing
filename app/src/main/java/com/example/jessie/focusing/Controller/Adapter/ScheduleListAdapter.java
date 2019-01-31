package com.example.jessie.focusing.Controller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.Model.ProfileManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.View.Profile.ProfileDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 28-01-2019
 * @time : 04:06
 */
public class ScheduleListAdapter extends BaseAdapter {

    private List<Profile> profiles = new ArrayList<>();
    private Context context;

    private ProfileManager profileManager;

    public ScheduleListAdapter(Context context) {
        this.context = context;
        profileManager = new ProfileManager(context);
    }

    public void setData(int today) {
//        Profile profile1=new Profile("Meeting");
        profiles=profileManager.syncProfileOnSchedule(today);

        notifyDataSetChanged();
    }

    public void addSchedule(Profile profile){
        //todo:调用Profdetail设置页面，将结果返回给schedule
//        profileManager.updateProfile(profile);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScheduleListAdapter.ViewHolder viewHolder = new ScheduleListAdapter.ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_schedule_list, null);
        }
        viewHolder.profileName = convertView.findViewById(R.id.profile_name);
        viewHolder.timeSlot = convertView.findViewById(R.id.timeSlot);
        viewHolder.repeat = convertView.findViewById(R.id.repeat);
        if (profiles.size()==0){
            viewHolder.profileName.setText("Today has no schedule");
        }else {
            Profile selectedInfo = profiles.get(position);


            viewHolder.profileName.setText(selectedInfo.getProfileName());
            String timeSlot = selectedInfo.getStartHour() + ":" + selectedInfo.getStartMin()
                    + "~" + selectedInfo.getEndHour() + ":" + selectedInfo.getEndMin();
            viewHolder.timeSlot.setText(timeSlot);
            viewHolder.repeat.setText(selectedInfo.getRepeat());
            convertView.setTag(selectedInfo);
        }
//        viewHolder.tv_ProfName.setOnClickListener(this);

        convertView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Profile profile=(Profile)v.getTag(); //将被点击的item转化为Profile instance,需要在view处setTAG
                Intent intent = new Intent(context, ProfileDetailActivity.class);
                intent.putExtra("ProfileId", profile.getId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    

    //todo:class和Class区别？
    class ViewHolder {

        TextView profileName,timeSlot,repeat;



    }
//    public void syncData() {
//        profileManager.syncData(appInfos);
//    }

    @Override
    public int getCount() {
        if (profiles != null && profiles.size() > 0) {
            return profiles.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (profiles != null && profiles.size() > 0) {
            return profiles.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
