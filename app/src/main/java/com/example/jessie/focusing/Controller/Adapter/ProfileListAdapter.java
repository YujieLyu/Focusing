package com.example.jessie.focusing.Controller.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.Model.ProfileManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.View.Profile.ProfileAppListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 25-01-2019
 * @time : 08:17
 */
public class ProfileListAdapter extends BaseAdapter implements View.OnClickListener {

    private List<Profile> profiles = new ArrayList<>();
    private Context context;
    private ProfileManager profileManager;
    private FragmentManager fragmentManager;

    public ProfileListAdapter(Context context) {
        this.context = context;
        profileManager = new ProfileManager(context);
    }

    public void setData(Profile profile) {

//        this.profiles = profileManager.insertProfileInfos(profile);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        Profile selectedInfo = profiles.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_profile_list, null);
        }
        viewHolder.tv_ProfName = convertView.findViewById(R.id.prof_name);

        viewHolder.tv_ProfName.setText(selectedInfo.getProfileName());
        return convertView;
    }

    @Override
    public void onClick(View v) {
        ProfileAppListFragment fragment = new ProfileAppListFragment();
        Bundle args = new Bundle();
        Profile p = (Profile) v.getTag();
        args.putString("Profile", p.getProfileName());//TODO：传值
        fragment.setArguments(args);
//        todo:跳转

//
//        c.beginTransaction()
//                .replace(R.id.fg_display, fragment)
//                .commit();

    }

    //todo:class和Class区别？
    class ViewHolder {

        TextView tv_ProfName;


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
