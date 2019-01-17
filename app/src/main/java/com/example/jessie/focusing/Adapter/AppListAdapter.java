package com.example.jessie.focusing.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jessie.focusing.Module.Lock_App_Activity;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Controller.AppInfoManager;
import com.example.jessie.focusing.Model.AppInfo;
import com.example.jessie.focusing.utils.SearchFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author : Yujie Lyu
 * @date : 29-12-2018
 * @time : 19:48
 */
public class AppListAdapter extends BaseAdapter implements View.OnClickListener,Filterable {


    List<AppInfo> appInfos,temp;
    private final Context context;
    private final AppInfoManager infoManager;
    private LayoutInflater myLayoutInflater;
    private SearchFilter searchFilter;

    public AppListAdapter(Context context) {
        this.context = context;
//        myLayoutInflater = LayoutInflater.from(context);
//        appInfos = getAppInfos();
//        temp = new ArrayList<>(appInfos);
        infoManager = new AppInfoManager(context);

    }
//        private AppInfoManager appLockInfoManager;



    public void setData(List<AppInfo> appInfos) {
//        LitePal.deleteAll(AppInfo.class);//TODO:临时的
        temp=appInfos;
        this.appInfos = infoManager.insertAppLockInfo(appInfos);
//        this.appInfos=appInfos;
        notifyDataSetChanged();
    }


    public List<AppInfo> getData() {
        return appInfos;
    }

    public void setTemp(List<AppInfo> temp) {
        this.temp = temp;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        AppInfo selectedAppInfo = appInfos.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lock_list, null);
        }

        mViewHolder.ivAppIcon = (ImageView) convertView.findViewById(R.id.app_icon);
        mViewHolder.txAppName = (TextView) convertView.findViewById(R.id.app_name);
        mViewHolder.cbAppIsLocked = convertView.findViewById(R.id.switch_bar);
        mViewHolder.ivAppIcon.setImageDrawable(selectedAppInfo.getAppImg());
        mViewHolder.txAppName.setText(selectedAppInfo.getAppName());
        mViewHolder.cbAppIsLocked.setChecked(selectedAppInfo.isLocked());
        mViewHolder.cbAppIsLocked.setTag(selectedAppInfo);
        mViewHolder.cbAppIsLocked.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        AppInfo appInfo = (AppInfo) v.getTag();
        boolean lockStatus = ((CheckBox) v).isChecked();
        appInfo.setLocked(lockStatus);
    }

    public void saveInfos() {
        infoManager.saveInfos(appInfos);
    }

    @Override
    public Filter getFilter() {
        if (searchFilter == null) {
            searchFilter = new SearchFilter(this);
        }
        return searchFilter;
    }

    class ViewHolder {

        ImageView ivAppIcon;
        TextView txAppName;
        CheckBox cbAppIsLocked;

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

}
