package com.example.jessie.focusing.Controller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jessie.focusing.Model.AppInfo;
import com.example.jessie.focusing.Model.AppUsage;
import com.example.jessie.focusing.Model.UsageManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.TimeHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 05-02-2019
 * @time : 23:44
 */
public class UsageListAdapter extends BaseAdapter {
    private final Context context;
    private List<AppUsage> appUsages=new ArrayList<>();
    private UsageManager usageManager;
    private long maxTime;

    public UsageListAdapter(Context context) {
        this.context = context;
        usageManager=new UsageManager(context);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      UsageListAdapter.ViewHolder viewHolder = new UsageListAdapter.ViewHolder();
        AppUsage selectedApp = appUsages.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lock_list, null);
        }

        viewHolder.ivAppIcon = convertView.findViewById(R.id.app_icon);
        viewHolder.txAppName = convertView.findViewById(R.id.app_name);
        viewHolder.ivAppIcon.setImageDrawable(selectedApp.getAppImg());
        viewHolder.txAppName.setText(selectedApp.getAppName());

        return convertView;
    }

    class ViewHolder {

        ImageView ivAppIcon;
        TextView txAppName;
        ImageButton imageButton;
    }

    public void setData(List<AppInfo> appInfos) {
        usageManager=new UsageManager(context);
        Calendar today=TimeHelper.getCurrCalendar();
        appUsages=usageManager.syncData(appInfos);


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
