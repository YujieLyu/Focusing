package com.example.jessie.focusing.Service;

import com.example.jessie.focusing.Model.UsageManager;

import java.util.Date;

/**
 * @author : Yujie Lyu
 * @date : 07-02-2019
 * @time : 10:25
 * 功能是持续监听screen 活动，用于收集各APP在focustime之外的使用时间和次数
 */
public class DataCollectService {

    private String packageName;
    private UsageManager usageManager;
    private long startTime,endTime;
    private long timeSummary;

    private void updateData(){
        //如果栈顶packageName发生变化，记录当前时间作为变化后packageName1的startTime；
        startTime=System.currentTimeMillis();
        //.
        //.
        //.
        //一段时间后，packageName再次变化，记录当前时间作为packageName1的endTime；
        //计算timesummary，调用usageManager.saveOrUpdateData,完成一次记录
        endTime=System.currentTimeMillis();
        timeSummary=endTime-startTime;
//        usageManager=new UsageManager(this);
//        usageManager.saveOrUpdateData(timeSummary,packageName);
        //将变化后的packageName2赋值给packageName，endtime赋给startTime，然后循环下一次记录

    }





}
