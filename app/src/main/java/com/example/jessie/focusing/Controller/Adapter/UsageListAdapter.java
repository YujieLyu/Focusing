package com.example.jessie.focusing.Controller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jessie.focusing.Model.AppUsage;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.View.Stats.LogDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 05-02-2019
 * @time : 23:44
 */
public class UsageListAdapter extends BaseAdapter implements View.OnClickListener {
    private final Context context;
    private List<AppUsage> appUsages = new ArrayList<>();

    public UsageListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<AppUsage> appUsages) {
        this.appUsages = appUsages;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (appUsages == null) {
            return 0;
        }
        return appUsages.size();
    }

    @Override
    public Object getItem(int position) {
        return appUsages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_stats_list, null);
        }
        ViewHolder vh = new ViewHolder(convertView);
        AppUsage appUsage = appUsages.get(position);
        vh.setValue(appUsage);
        convertView.setTag(appUsage);
        convertView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        AppUsage appUsage = (AppUsage) v.getTag();
        if (appUsage == null) {
            Toast.makeText(context, "Cannot find App", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(context, LogDetailActivity.class);
        intent.putExtra(LogDetailActivity.KEY, appUsage.getPackageName());
        context.startActivity(intent);
    }

    class ViewHolder {

        ImageView ivAppIcon;
        TextView txAppName;
        ImageButton imageButton;

        ViewHolder(View view) {
            ivAppIcon = view.findViewById(R.id.app_icon);
            txAppName = view.findViewById(R.id.app_name);
            imageButton = view.findViewById(R.id.click_arrow);
        }

        void setValue(AppUsage appUsage) {
            txAppName.setText(appUsage.getAppName());
            ivAppIcon.setImageDrawable(appUsage.getAppImg());
        }
    }


}
