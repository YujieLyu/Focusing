package com.example.jessie.focusing.View.Finish;

//import android.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.Shared.BaseSingleTaskActivity;

import static com.example.jessie.focusing.Utils.RenderScriptBlur.rsBlur;


/**
 * @author : Yujie Lyu
 * @date : 15-01-2019
 * @time : 11:15
 */
public class FinishActivity extends BaseSingleTaskActivity {

    private RelativeLayout finishLayout;
    private TextView tv_wellDone, tv_summary, tv_keepUp;


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

        long timeSummary = getIntent().getLongExtra("countTime", 0);
        long hours = (timeSummary % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (timeSummary % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (timeSummary % (1000 * 60)) / 1000;
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
        Bitmap b = rsBlur(FinishActivity.this, bmp, 25);
        finishLayout.setBackground(new BitmapDrawable(b));

    }

}
