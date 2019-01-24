package com.example.jessie.focusing.Controller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jessie.focusing.Model.ProfileInfo;
import com.example.jessie.focusing.Model.ProfileManager;
import com.example.jessie.focusing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 25-01-2019
 * @time : 08:17
 */
public class ProfileListAdapter extends BaseAdapter implements View.OnClickListener{

    private List<ProfileInfo> profileInfos=new ArrayList<>();
    private Context context;
    private ProfileManager profileManager;

    public ProfileListAdapter(Context context){
        this.context=context;
        profileManager = new ProfileManager(context);
    }

    public void setData(ProfileInfo profileInfo){
        this.profileInfos=profileManager.insertProfileInfos(profileInfo);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        ProfileInfo profileInfo=profileInfos.get(position);
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.item_profile_list,null);
        }
        viewHolder.tv_ProfName=convertView.findViewById(R.id.prof_name);
        viewHolder.tv_ProfName.setText(profileInfo.getProfileName());
        return convertView;
    }

    @Override
    public void onClick(View v) {

    }

    //todo:class和Class区别？
    class ViewHolder {

        TextView tv_ProfName;


    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }




}
