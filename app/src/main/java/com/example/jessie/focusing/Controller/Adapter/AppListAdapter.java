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
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.AppInfoUtils;

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
    private final int profId;
    private List<AppInfo> appInfos = new ArrayList<>();

    public AppListAdapter(Context context, int profId) {
        this.profId = profId;
        this.context = context;
        infoManager = new AppInfoManager();
    }


    /**
     * Used to update apps information stored in DB.
     * By getting the currently installed apps info in the phone,
     * updating apps data stored in DB. And then adding newly installed
     * apps to DB and delete uninstalled ones
     */
    public void syncData() {
        AppInfoUtils appInfoUtils = new AppInfoUtils(context);
        final List<AppInfo> installedApps = appInfoUtils.getInstalledApps();
        //update DB
        appInfos = AppInfoManager.syncData(installedApps, profId);
        // sort the apps by their lock status
        appInfos.sort(AppInfo::compareTo);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        //listen the checkbox's change
        AppInfo appInfo = (AppInfo) v.getTag();
        boolean lockStatus = ((CheckBox) v).isChecked();
        appInfo.setLocked(lockStatus);
    }

    public void saveSettings() {
        if (profId != Profile.START_NOW_PROFILE_ID && Profile.count(profId) <= 0) {
            infoManager.deleteByProfId(profId);
            return;
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
