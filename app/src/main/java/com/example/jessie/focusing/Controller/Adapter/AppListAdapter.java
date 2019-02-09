package com.example.jessie.focusing.Controller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jessie.focusing.Model.AppInfo;
import com.example.jessie.focusing.Model.AppInfoManager;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.Model.ProfileManager;
import com.example.jessie.focusing.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 29-12-2018
 * @time : 19:48
 */
public class AppListAdapter extends BaseAdapter implements View.OnClickListener {


    private final Context context;
    private final AppInfoManager infoManager;
    private final ProfileManager profileManager;
    List<AppInfo> appInfos = new ArrayList<>();
    private int profId;

    public AppListAdapter(Context context) {
        this.context = context;
        infoManager = new AppInfoManager();
        profileManager = new ProfileManager();
    }


    public void setData(List<AppInfo> appInfos, int profId) {
        this.profId = profId;
        for (AppInfo appInfo : appInfos) {
            appInfo.setProfId(profId);//set appinfo list里面所有数据的profileid为同一数字

        }
        this.appInfos = infoManager.syncData(appInfos);


        notifyDataSetChanged();
    }


    public List<AppInfo> getData() {
        return appInfos;
    }

    @Override
    public void onClick(View v) {
        AppInfo appInfo = (AppInfo) v.getTag();
        boolean lockStatus = ((CheckBox) v).isChecked();
        appInfo.setLocked(lockStatus);
    }

    public void saveSettings() {
        if (profId != Profile.START_NOW_PROFILE_ID) { //TODO: how to deal with "start now"
            Profile profile = profileManager.getProfile(profId);
            if (profile == null) {
                infoManager.deleteByProfId(profId);
                return;
            }
        }
        infoManager.saveOrUpdateInfos(appInfos);
    }

    @Override
    public int getCount() {
        if (appInfos != null && appInfos.size() > 0) {
            return appInfos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (appInfos != null && appInfos.size() > 0) {
            return appInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        AppInfo selectedAppInfo = appInfos.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lock_list, null);
        }

        viewHolder.ivAppIcon = convertView.findViewById(R.id.app_icon);
        viewHolder.txAppName = convertView.findViewById(R.id.app_name);
        viewHolder.cbAppIsLocked = convertView.findViewById(R.id.switch_bar);
        viewHolder.ivAppIcon.setImageDrawable(selectedAppInfo.getAppImg());
        viewHolder.txAppName.setText(selectedAppInfo.getAppName());
        viewHolder.cbAppIsLocked.setChecked(selectedAppInfo.isLocked());
        viewHolder.cbAppIsLocked.setTag(selectedAppInfo);
        viewHolder.cbAppIsLocked.setOnClickListener(this);

        return convertView;
    }

    class ViewHolder {

        ImageView ivAppIcon;
        TextView txAppName;
        CheckBox cbAppIsLocked;
    }

}
