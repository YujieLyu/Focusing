package com.example.jessie.focusing.Module;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.utils.LockUtil;

import java.util.Calendar;


/**
 * @author : Yujie Lyu
 * @date : 15-01-2019
 * @time : 11:15
 */
public class Finish_Activity extends AppCompatActivity {

    private long timeSummary;
    private RelativeLayout finishLayout;
    private TextView tv_wellDone, tv_summary, tv_keepUp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        finishLayout = findViewById(R.id.finish_layout);
        tv_wellDone = findViewById(R.id.tv_welldone);
        tv_keepUp = findViewById(R.id.tv_keepup);
        tv_summary = findViewById(R.id.tv_summary);
        initData();

    }

    public void initData() {
        timeSummary = getIntent().getLongExtra("countTime", 0);
        initLayoutBackground();

    }

    private void initLayoutBackground() {

        Calendar convertTime = Calendar.getInstance();
        convertTime.setTimeInMillis(timeSummary);
        int hours = convertTime.get(Calendar.HOUR_OF_DAY);
        int mins = convertTime.get(Calendar.MINUTE);
        tv_summary.setText(String.format("%d hours %d mins", hours, mins));
        Resources resources = this.getResources();
        final Drawable bgPic = resources.getDrawable(R.drawable.bg_pure2);
        finishLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        finishLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        finishLayout.buildDrawingCache();
                        Bitmap bmp = LockUtil.drawableToBitmap(bgPic, finishLayout);
                        LockUtil.blur(Finish_Activity.this, LockUtil.big(bmp), finishLayout);  //高斯模糊
                        return true;
                    }
                });

    }
}
