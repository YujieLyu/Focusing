package com.example.jessie.focusing.View.Finish;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jessie.focusing.Model.FocusTimeManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;

import java.util.Calendar;
import java.util.Date;

import static com.example.jessie.focusing.Utils.RenderScriptBlur.rsBlur;


/**
 * @author : Yujie Lyu
 * @date : 15-01-2019
 * @time : 11:15
 */
public class Finish_Activity extends AppCompatActivity {

    private long timeSummary;
    private RelativeLayout finishLayout;
    private TextView tv_wellDone, tv_summary, tv_keepUp;
    private FocusTimeManager focusTimeManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        finishLayout = findViewById(R.id.finish_layout);
        tv_wellDone = findViewById(R.id.tv_welldone);
        tv_keepUp = findViewById(R.id.tv_keepup);
        tv_summary = findViewById(R.id.tv_summary);
        tv_summary.setText(initData());
        StatusBarUtil.setStatusTransparent(this);
        StatusBarUtil.setDarkStatusIcon(this,true);
        initLayoutBackground();

    }

    public String initData() {

        timeSummary = getIntent().getLongExtra("countTime", 0);
        long hours = (timeSummary % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (timeSummary % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (timeSummary % (1000 * 60)) / 1000;
        focusTimeManager=new FocusTimeManager();
//        Calendar today=Calendar.getInstance();
//        today.setTimeInMillis(System.currentTimeMillis());
//        focusTimeManager.saveOrUpdateTime(today,timeSummary);
        if(seconds>30){
            minutes+=1;
        }
        String displayTime;
        if(hours==0){
            displayTime= minutes+" minutes";
        }else {
            displayTime=hours + " hour " + minutes + " minutes";
        }
        return displayTime ;


    }
    private void initLayoutBackground() {

        final Resources resources = this.getResources();
        Bitmap bmp = BitmapFactory.decodeResource(resources, R.drawable.bg_aurora);
        Bitmap b=rsBlur(Finish_Activity.this, bmp, 25);
        finishLayout.setBackground(new BitmapDrawable(b));

    }


//    private void initLayoutBackground() {
//
//        Calendar convertTime = Calendar.getInstance();
//        convertTime.setTimeInMillis(timeSummary);
//        int hours = convertTime.get(Calendar.HOUR_OF_DAY);
//        int mins = convertTime.get(Calendar.MINUTE);
//        tv_summary.setText(String.format("%d hours %d mins", hours, mins));
//        Resources resources = this.getResources();
//        final Drawable bgPic = resources.getDrawable(R.drawable.bg_pure2);
//        finishLayout.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener() {
//                    @Override
//                    public boolean onPreDraw() {
//                        finishLayout.getViewTreeObserver().removeOnPreDrawListener(this);
//                        finishLayout.buildDrawingCache();
//                        Bitmap bmp = LockUtil.drawableToBitmap(bgPic, finishLayout);
//                        LockUtil.blur(Finish_Activity.this, LockUtil.big(bmp), finishLayout);  //高斯模糊
//                        return true;
//                    }
//                });
//
//    }
}
