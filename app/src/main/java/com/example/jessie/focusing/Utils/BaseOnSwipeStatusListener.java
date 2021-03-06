package com.example.jessie.focusing.Utils;

import com.example.jessie.focusing.widget.SwipeItemLayout;

import java.util.Set;

/**
 * @author : Yujie Lyu
 * @date : 01-02-2019
 * @time : 01:39
 */
public class BaseOnSwipeStatusListener implements SwipeItemLayout.OnSwipeStatusListener {

    private final SwipeItemLayout currItem;
    private final Set<SwipeItemLayout> tempSet;

    public BaseOnSwipeStatusListener(SwipeItemLayout currSwipeItem, Set<SwipeItemLayout> tempSet) {
        currItem = currSwipeItem;
        this.tempSet = tempSet;
    }

    @Override
    public void onStatusChanged(SwipeItemLayout.Status status) {
        if (status == SwipeItemLayout.Status.Open) {
            //remove other layouts with Open status
            if (tempSet.size() > 0) {
                for (SwipeItemLayout s : tempSet) {
                    s.setStatus(SwipeItemLayout.Status.Close, true);
                    tempSet.remove(s);
                }
            }
            tempSet.add(currItem);
        } else {
            tempSet.remove(currItem);
        }
    }

    @Override
    public void onStartCloseAnimation() {

    }

    @Override
    public void onStartOpenAnimation() {

    }
}
