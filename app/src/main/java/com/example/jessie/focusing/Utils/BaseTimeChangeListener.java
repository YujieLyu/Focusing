package com.example.jessie.focusing.Utils;


import com.example.jessie.focusing.Interface.OnTimeChangeListener;

/**
 * A wrapper class for convenience use of {@link OnTimeChangeListener}
 *
 * @author : Yujie Lyu
 * @date : 22-01-2019
 * @time : 21:48
 */
public abstract class BaseTimeChangeListener implements OnTimeChangeListener {

    @Override
    public void onStartTimeChanged(float startDegree, float endDegree) {

    }


    @Override
    public void onEndTimeChanged(long startTime, long endTime) {

    }

    @Override
    public void onInitTime(long startTime, long endTime) {

    }


    @Override
    public void onAllTimeChanged(float startDegree, float endDegree) {

    }
}
