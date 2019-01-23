package com.example.jessie.focusing.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.jessie.focusing.R;

public class Main_Activity extends AppCompatActivity {


    private TextView tv_start,tv_profile,tv_schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusTransparent();
//        setDarkStatusIcon(true);

        tv_start=findViewById(R.id.tv_start_now);
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main_Activity.this,StartNow_Activity.class);
                startActivity(intent);
            }
        });
        tv_start.setBackgroundColor(Color.argb(145, 255, 255, 255)); //背景透明度

        tv_profile=findViewById(R.id.tv_profile);
        tv_profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main_Activity.this,Profile_Activity.class);
                startActivity(intent);
            }

        });
        tv_profile.setBackgroundColor(Color.argb(145, 255, 255, 255)); //背景透明度


        tv_schedule=findViewById(R.id.tv_schedule);
        tv_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main_Activity.this,Schedule_Activity.class);
                startActivity(intent);

            }
        });
        tv_schedule.setBackgroundColor(Color.argb(145, 255, 255, 255)); //背景透明度


    }

    protected void setStatusTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0+ 实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4 实现
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void setDarkStatusIcon(boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView == null) return;

            int vis = decorView.getSystemUiVisibility();
            if (dark) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        }
    }





}
