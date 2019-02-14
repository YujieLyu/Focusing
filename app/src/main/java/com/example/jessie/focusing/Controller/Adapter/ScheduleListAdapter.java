package com.example.jessie.focusing.Controller.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.Model.ProfileManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.View.Profile.ProfileDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.jessie.focusing.Utils.AppConstants.PROFILE_ID;

/**
 * @author : Yujie Lyu
 * @date : 28-01-2019
 * @time : 04:06
 */
public class ScheduleListAdapter extends BaseAdapter {

    private List<Profile> profiles = new ArrayList<>();
    private Context context;

    private ProfileManager profileManager;

    public ScheduleListAdapter(Context context) {
        this.context = context;
        profileManager = new ProfileManager();
    }

    public void setData(int dayOfWeek) {
        profiles = profileManager.syncProfileOnSchedule(dayOfWeek);
        notifyDataSetChanged();
    }

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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScheduleListAdapter.ViewHolder viewHolder = new ScheduleListAdapter.ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_schedule_list, null);
        }
        viewHolder.profileName = convertView.findViewById(R.id.profile_name);
        viewHolder.timeSlot = convertView.findViewById(R.id.timeSlot);
        viewHolder.repeat = convertView.findViewById(R.id.repeat);
        if (profiles.isEmpty()) {
            viewHolder.profileName.setText("Today has no schedule");
        } else {
            Profile selectedInfo = profiles.get(position);
            viewHolder.profileName.setText(selectedInfo.getProfileName());
            String timeSlot = selectedInfo.getTimeSlot();
            viewHolder.timeSlot.setText(timeSlot);
            viewHolder.repeat.setText(selectedInfo.getRepeatString());
            convertView.setTag(selectedInfo);
        }
        //TODO: cost too much resources
        convertView.setOnClickListener(v -> {
            Profile profile = (Profile) v.getTag();
            Intent intent = new Intent(context, ProfileDetailActivity.class);
            intent.putExtra(PROFILE_ID, profile.getId());
            context.startActivity(intent);
        });
        return convertView;
    }

    class ViewHolder {
        TextView profileName, timeSlot, repeat;
    }
}
