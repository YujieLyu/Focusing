package com.example.jessie.focusing.View.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jessie.focusing.Controller.Adapter.ProfileListAdapter;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;

import static com.example.jessie.focusing.Utils.AppConstants.PROFILE_ID;

/**
 * @author : Yujie Lyu
 * @date : 19-01-2019
 * @time : 01:02
 */
public class ProfileListActivity extends AppCompatActivity
        implements AddNewProfileDialog.OnFragmentInteractionListener {
    private ProfileListAdapter profileListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
        RelativeLayout profileList = findViewById(R.id.prof_list);
        profileList.setBackgroundResource(R.drawable.island);
        ImageButton btn_add = findViewById(R.id.ibtn_add);
        btn_add.setOnClickListener(v -> showAddProfileDialog());
        ListView lv_profile = findViewById(R.id.lv_prof_list);
        profileListAdapter = new ProfileListAdapter(this);
        initData();
        lv_profile.setAdapter(profileListAdapter);
        StatusBarUtil.setStatusTransparent(this);
        StatusBarUtil.setDarkStatusIcon(this, true);


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void showAddProfileDialog() {
        AddNewProfileDialog dialog = AddNewProfileDialog.newInstance(null);
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager, "AddProfile");

    }

    private void initData() {
        profileListAdapter.fetchData();
    }

    @Override
    public void onFragmentInteraction(Profile profile) {
        boolean res = profile.saveOrUpdate();
        if (res) {
            Intent intent = new Intent(this, ProfileDetailActivity.class);
            intent.putExtra(PROFILE_ID, profile.getId());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Cannot save such Profile", Toast.LENGTH_LONG).show();
        }
    }
}
