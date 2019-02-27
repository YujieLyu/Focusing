package com.example.jessie.focusing.Controller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.BaseOnSwipeStatusListener;
import com.example.jessie.focusing.View.Profile.ProfileDetailActivity;
import com.example.jessie.focusing.widget.SwipeItemLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.jessie.focusing.Utils.AppConstants.PROFILE_ID;

/**
 * @author : Yujie Lyu
 * @date : 25-01-2019
 * @time : 08:17
 */
public class ProfileListAdapter extends BaseAdapter {

    private final Set<SwipeItemLayout> tempItemSet;
    private List<Profile> profiles = new ArrayList<>();
    private Context context;

    public ProfileListAdapter(Context context) {
        this.context = context;
        tempItemSet = new HashSet<>();
    }

    public void fetchData() {
        profiles = Profile.findAll();
        notifyDataSetChanged();
    }

    public void addProfile(Profile profile) {
        profile.saveOrUpdate();
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        final Profile selectedProf = profiles.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.slip_list_item, null);
            convertView.setBackgroundColor(Color.argb(10, 255, 255, 255));
        }
        final SwipeItemLayout sll_main = convertView.findViewById(R.id.sll_main);
        sll_main.setOnSwipeStatusListener(new BaseOnSwipeStatusListener(sll_main, tempItemSet));
        viewHolder.tv_ProfName = convertView.findViewById(R.id.prof_name);

        viewHolder.tv_ProfName.setText(selectedProf.getProfileName());
        viewHolder.tv_ProfName.setTag(selectedProf);
        viewHolder.tv_ProfName.setOnClickListener(v -> {
            if (sll_main.getCurrentStatus() == SwipeItemLayout.Status.Open) {
                sll_main.setStatus(SwipeItemLayout.Status.Close, true);
                return;
            }
            Profile profile = (Profile) v.getTag();
            Intent intent = new Intent(context, ProfileDetailActivity.class);
            intent.putExtra(PROFILE_ID, profile.getId());
            context.startActivity(intent);
        });
        viewHolder.tv_delete = convertView.findViewById(R.id.tv_delete);
        viewHolder.tv_delete.setOnClickListener(v -> {
            sll_main.setStatus(SwipeItemLayout.Status.Close, true);
            selectedProf.deleteCascadeAsync();
            profiles.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }

    class ViewHolder {

        TextView tv_ProfName;
        TextView tv_delete;
    }


}
