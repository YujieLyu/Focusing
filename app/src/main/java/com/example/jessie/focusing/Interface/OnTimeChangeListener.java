package com.example.jessie.focusing.Interface;

/**
 * @author : Yujie Lyu
 * @date : 22-01-2019
 * @time : 21:47
 */
public interface OnTimeChangeListener {
    /**
     * Executed when start time changing
     * @param startDegree The angle of start time
     * @param endDegree  The angle of end time
     */
    void onStartTimeChanged(float startDegree, float endDegree);

    /**
     * Executed when end time changing
     * @param startDegree The angle of start time
     * @param endDegree  The angle of end time
     */
    void onEndTimeChanged(float startDegree, float endDegree);

    /**
     * Initialize the start time and end time
     * @param startDegree The angle of start time
     * @param endDegree  The angle of end time
     */
    void onInitTime(float startDegree, float endDegree);


    /**
     * Executed when all time changing
     * @param startDegree The angle of start time
     * @param endDegree  The angle of end time
     */
    void onAllTimeChanged(float startDegree, float endDegree);
}

