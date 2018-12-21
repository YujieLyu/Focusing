package com.example.jessie.focusing_demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 21-12-2018
 * @time : 14:39
 */
public class Lock_App_Activity extends AppCompatActivity {

    private ListView lv_app_list;
    private AppListAdapter mAppListAdpter;
    public Handler mHandler=new Handler();

    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lock_app_main);
            lv_app_list = findViewById(R.id.lv_app_list);
            mAppListAdpter = new AppListAdapter();
            lv_app_list.setAdapter(mAppListAdpter);
            initData();
    }

    private void initData(){
        final Thread thread = new Thread() {
        @Override
            public void run(){
            super.run();
            final List<AppLockInfo> appLockInfos=ScanAppsTool.scanAppsList(Lock_App_Activity.this.getPackageManager());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mAppListAdpter.setData(appLockInfos);
                }
            });
        }
        };
        thread.start();
    }

    class AppListAdapter extends BaseAdapter {

        List<AppLockInfo> appLockInfos = new ArrayList<AppLockInfo>();

        public void setData(List<AppLockInfo> appLockInfos) {
            this.appLockInfos = appLockInfos;
            notifyDataSetChanged();
        }

        public List<AppLockInfo> getData() {
            return appLockInfos;
        }

        @Override
        public int getCount() {
            if (appLockInfos != null && appLockInfos.size() > 0) {
                return appLockInfos.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (appLockInfos != null && appLockInfos.size() > 0) {
                return appLockInfos.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            AppLockInfo appLockInfo = appLockInfos.get(position);
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_lock_list, null);
                mViewHolder.iv_app_icon = (ImageView) convertView.findViewById(R.id.app_icon);
                mViewHolder.tx_app_name = (TextView) convertView.findViewById(R.id.app_name);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.iv_app_icon.setImageDrawable(appLockInfo.getAppImg());
            mViewHolder.tx_app_name.setText(appLockInfo.getAppName());
            return convertView;
        }

        class ViewHolder {

            ImageView iv_app_icon;
            TextView tx_app_name;
        }
    }



}
