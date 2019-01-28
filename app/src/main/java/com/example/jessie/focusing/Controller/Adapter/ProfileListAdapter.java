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
 * @date : 25-01-2019
 * @time : 08:17
 */
public class ProfileListAdapter extends BaseAdapter {

    private List<Profile> profiles = new ArrayList<>();
    private Context context;

    private ProfileManager profileManager;
    private FragmentManager fragmentManager;

    public ProfileListAdapter(Context context) {
        this.context = context;
        profileManager = new ProfileManager(context);
    }

    public void setData() {
//        Profile profile1=new Profile("Meeting");
        profiles=profileManager.syncProfile();
        notifyDataSetChanged();
    }

    public void addProfile(Profile profile){
        profileManager.updateProfile(profile);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        Profile selectedInfo = profiles.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_profile_list, null);
            convertView.setBackgroundColor(Color.argb(10,255,255,255));
        }
        viewHolder.tv_ProfName = convertView.findViewById(R.id.prof_name);
        viewHolder.checkBox = convertView.findViewById(R.id.cb_ischecked);

        viewHolder.tv_ProfName.setText(selectedInfo.getProfileName());
        viewHolder.tv_ProfName.setTag(selectedInfo);
        viewHolder.tv_ProfName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile profile=(Profile)v.getTag(); //将被点击的item转化为Profile instance,需要在view处setTAG
                Intent intent=new Intent(context,ProfileDetailActivity.class);
                intent.putExtra("ProfileId",profile.getId());
                context.startActivity(intent);
            }
        });
        viewHolder.checkBox.setTag(selectedInfo);
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Profile profile=(Profile)v.getTag(); //将被点击的item转化为Profile instance,需要在view处setTAG
                boolean lockStatus = ((CheckBox) v).isChecked();
                profile.setOn(lockStatus);
            }
        });
//        viewHolder.tv_ProfName.setOnClickListener(this);
        return convertView;
    }
    public void saveSettings() {
        profileManager.updateProfiles(profiles);
    }



//    @Override
//    public void onClick(View v) {
//        Profile profile=(Profile)v.getTag(); //将被点击的item转化为Profile instance,需要在view处setTAG
//        Intent intent=new Intent(context,ProfileDetailActivity.class);
//        intent.putExtra("ProfileId",profile.getId());
//        context.startActivity(intent);
//
//    }

    //todo:class和Class区别？
    class ViewHolder {

        TextView tv_ProfName;
        private CheckBox checkBox;


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
