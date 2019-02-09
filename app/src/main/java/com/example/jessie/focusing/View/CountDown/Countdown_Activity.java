package com.example.jessie.focusing.View.CountDown;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jessie.focusing.Controller.Adapter.AppListAdapter;
import com.example.jessie.focusing.Model.AppInfoManager;
import com.example.jessie.focusing.Model.FocusTimeManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Service.LockService;
import com.example.jessie.focusing.Utils.LockUtil;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.Utils.TimeHelper;
import com.example.jessie.focusing.View.Finish.Finish_Activity;
import com.example.jessie.focusing.View.Main.Main_Activity;

import java.util.Calendar;
import java.util.Date;

import cn.iwgang.countdownview.CountdownView;

import static com.example.jessie.focusing.Utils.AppConstants.ONE_DAY;
import static com.example.jessie.focusing.Utils.RenderScriptBlur.rsBlur;

/**
 * @author : Yujie Lyu
 * @date : 11-01-2019
 * @time : 10:14
 */
public class Countdown_Activity extends AppCompatActivity implements CountdownView.OnCountdownEndListener {

    private long startTime, endTime, countTime;
    private String clickBackFromLock;//按返回键
    private RelativeLayout countLayout;
    private CountdownView cdv_count;
    private TextView tv_suggestInfo;
    private Button btn_stop;
    private Handler handler;
    private AppInfoManager appInfoManager;
    private FocusTimeManager focusTimeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);
        processExtraData();
        cdv_count = findViewById(R.id.cdv_count);
        cdv_count.setOnCountdownEndListener(this);
        tv_suggestInfo = findViewById(R.id.tv_suggestInfo);
        countLayout = findViewById(R.id.cd_main_view);
        btn_stop=findViewById(R.id.btn_cancel_countdown);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(Countdown_Activity.this,Main_Activity.class);
                startActivity(intent);
            }
        });
        appInfoManager = new AppInfoManager(this);
        initData();
        StatusBarUtil.setStatusTransparent(this);
//        initLayoutBackground();
    }

    protected void initData() {

//        clickBackFromLock = getIntent().getStringExtra(AppConstants.PRESS_BACK);
        endTime = getIntent().getLongExtra("endTime", 0);

        long currTimeStart = System.currentTimeMillis();
        countTime = endTime - currTimeStart;
        if (currTimeStart < endTime) {
            countTime = endTime - currTimeStart;
        } else {
            countTime = endTime - currTimeStart + ONE_DAY;
        }

        cdv_count.start(countTime);
        focusTimeManager=new FocusTimeManager();
//

    }


    private void initLayoutBackground() {

        final Resources resources = this.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.bg_snow_mountain);
        Bitmap b = rsBlur(Countdown_Activity.this, bmp, 25);

        countLayout.setBackground(new BitmapDrawable(b));

    }

    @Override
    public void onBackPressed() {
        LockUtil.goHome(this);
    }

    @Override
    public void onEnd(CountdownView cv) {
        cv.stop();
        appInfoManager.reset(-10);
        LockService.StartNow = false;

//        moveTaskToBack(true);
        Intent intent = new Intent(Countdown_Activity.this, Finish_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("countTime", countTime);
        startActivity(intent);
//        finishAndRemoveTask();//Kill the activity
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Calendar today=Calendar.getInstance();
//        today.setTimeInMillis(System.currentTimeMillis());
//        Calendar today=TimeHelper.getCurrCalendar();
        long remainTime=cdv_count.getRemainTime();
        long timeSummary=countTime-remainTime;
        focusTimeManager.saveOrUpdateTime(timeSummary);
        appInfoManager.reset(-10);
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
}
