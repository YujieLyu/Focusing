package com.example.jessie.focusing.View.Main;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.amitshekhar.DebugDB;
import com.example.jessie.focusing.Model.UsageManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.DataStatisticActivity;
import com.example.jessie.focusing.View.Profile.ProfileList_Activity;
import com.example.jessie.focusing.View.Schedule.Schedule_Activity;
import com.example.jessie.focusing.View.StartNow.StartNow_Activity;

import org.litepal.LitePal;

public class Main_Activity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.initialize(this);
//        LitePal.deleteDatabase("appDB");//todo: be careful
        DebugDB.getAddressLog();
        StatusBarUtil.setStatusTransparent(this);

        TextView tv_start = findViewById(R.id.tv_start_now);
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main_Activity.this,StartNow_Activity.class);
                startActivity(intent);
            }
        });

        TextView tv_profile = findViewById(R.id.tv_profile);
        tv_profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main_Activity.this,ProfileList_Activity.class);
                startActivity(intent);
            }

        });


        TextView tv_schedule = findViewById(R.id.tv_schedule);
        tv_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main_Activity.this,Schedule_Activity.class);
                startActivity(intent);

            }
        });
        TextView tv_statistic=findViewById(R.id.tv_statistic);
        tv_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Activity.this, DataStatisticActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        getIntent(); //will return the old one
        processExtraData();
    }

    private void processExtraData() {
        Intent intent = getIntent();
        //use the data received here
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Cursor c = LitePal.getDatabase().rawQuery("PRAGMA wal_checkpoint;", null);
//        c.close();
    }
}
