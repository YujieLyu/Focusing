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

import com.example.jessie.focusing.Model.AppInfoManager;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.Model.ProfileManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.View.Profile.ProfileDetailActivity;
import com.example.jessie.focusing.widget.BaseOnSwipeStatusListener;
import com.example.jessie.focusing.widget.SwipeItemLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Yujie Lyu
 * @date : 25-01-2019
 * @time : 08:17
 */
public class ProfileListAdapter extends BaseAdapter {

    private List<Profile> profiles = new ArrayList<>();
    private Context context;

    private ProfileManager profileManager;
    private AppInfoManager appInfoManager;
    private FragmentManager fragmentManager;
    private final Set<SwipeItemLayout> tempItemSet;

    public ProfileListAdapter(Context context) {
        this.context = context;
        profileManager = new ProfileManager(context);
        appInfoManager = new AppInfoManager(context);
        tempItemSet = new HashSet<>();
    }

    public void setData() {
//        Profile profile1=new Profile("Meeting");
        profiles = profileManager.syncProfile();
        notifyDataSetChanged();
    }

    public void addProfile(Profile profile) {
        profileManager.updateProfile(profile);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        final Profile selectedInfo = profiles.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.slip_list_item, null);
            convertView.setBackgroundColor(Color.argb(10, 255, 255, 255));
        }
        final SwipeItemLayout sll_main = convertView.findViewById(R.id.sll_main);
        sll_main.setOnSwipeStatusListener(new BaseOnSwipeStatusListener(sll_main, tempItemSet)); //TODO: refine constructor
        viewHolder.tv_ProfName = convertView.findViewById(R.id.prof_name);
//        viewHolder.checkBox = convertView.findViewById(R.id.cb_ischecked);

        viewHolder.tv_ProfName.setText(selectedInfo.getProfileName());
        viewHolder.tv_ProfName.setTag(selectedInfo);
        viewHolder.tv_ProfName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sll_main.getCurrentStatus() == SwipeItemLayout.Status.Open) {
                    sll_main.setStatus(SwipeItemLayout.Status.Close, true);
                    return;
                }
                Profile profile = (Profile) v.getTag(); //将被点击的item转化为Profile instance,需要在view处setTAG
                Intent intent = new Intent(context, ProfileDetailActivity.class);
                intent.putExtra("ProfileId", profile.getId());
                context.startActivity(intent);
            }
        });
        viewHolder.tv_delete = convertView.findViewById(R.id.tv_delete);
        viewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sll_main.setStatus(SwipeItemLayout.Status.Close, true);
                profileManager.deleteProfile(selectedInfo.getId());
                appInfoManager.deleteByProfId(selectedInfo.getId());
                profiles.remove(position);
                notifyDataSetChanged();
            }
        });

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
        TextView tv_top;
        TextView tv_delete;


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
