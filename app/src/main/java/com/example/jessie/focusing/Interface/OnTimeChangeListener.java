package com.example.jessie.focusing.Interface;

/**
 * @author : Yujie Lyu
 * @date : 22-01-2019
 * @time : 21:47
 */
public interface OnTimeChangeListener {
    /**
     * 开始时间发生变化
     * @param startDegree 开始时间代表的角度
     * @param endDegree  结束时间代表的角度
     */
    void startTimeChanged(float startDegree, float endDegree);

    /**
     * 结束时间发生变化
     * @param startDegree 开始时间代表的角度
     * @param endDegree  结束时间代表的角度
     */
    void endTimeChanged(float startDegree, float endDegree);

    /**
     * 初始化开始时间和结束时间
     * @param startDegree 开始时间代表的角度
     * @param endDegree  结束时间代表的角度
     */
    void initTime(float startDegree, float endDegree);


    /**
     * 开始,结束时间都发生变化
     * @param startDegree 开始时间代表的角度
     * @param endDegree  结束时间代表的角度
     */
    void onAllTimeChanaged(float startDegree, float endDegree);
}

