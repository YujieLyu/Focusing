package com.example.jessie.focusing.Controller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Model.AppInfoManager;
import com.example.jessie.focusing.Model.AppInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 29-12-2018
 * @time : 19:48
 */
public class AppListAdapter extends BaseAdapter implements View.OnClickListener {


    List<AppInfo> appInfos = new ArrayList<>();
    private final Context context;
    private final AppInfoManager infoManager;

    public AppListAdapter(Context context) {
        this.context = context;
        infoManager = new AppInfoManager(context);

    }
//        private AppInfoManager appLockInfoManager;


    public void setData(List<AppInfo> appInfos) {
//        LitePal.deleteAll(AppInfo.class);//TODO:临时的

        this.appInfos = infoManager.insertAppLockInfo(appInfos);
//        this.appInfos=appInfos;
        notifyDataSetChanged();
    }


    public List<AppInfo> getData() {
        return appInfos;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        AppInfo selectedAppInfo = appInfos.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lock_list, null);
        }

        mViewHolder.ivAppIcon = convertView.findViewById(R.id.app_icon);
        mViewHolder.txAppName = convertView.findViewById(R.id.app_name);
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
