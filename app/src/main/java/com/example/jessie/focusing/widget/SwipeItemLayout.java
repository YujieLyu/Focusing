package com.example.jessie.focusing.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 侧滑Layout
 */
public class SwipeItemLayout extends FrameLayout {

    public static final String TAG = "SlipListLayout";
    private View hiddenView;
    private View itemView;
    private int hiddenViewWidth;
    private ViewDragHelper mDragHelper;
    private int hiddenViewHeight;
    private int itemWidth;
    private int itemHeight;
    private OnSwipeStatusListener listener;
    private Status status = Status.Close;
    private boolean smooth = true;
    private Status preStatus = Status.Close;

    Callback callback = new Callback() {

        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            if (itemView == changedView) {
                hiddenView.offsetLeftAndRight(dx);
            }
            invalidate();
        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == itemView) {
                if (xvel == 0
                        && Math.abs(itemView.getLeft()) > hiddenViewWidth / 2.0f) {
                    open(smooth);
                } else if (xvel < 0) {
                    // to left
                    open(smooth);
                } else {
                    // to right
                    close(smooth);
                }
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return hiddenViewWidth;
        }

        @Override
        public boolean tryCaptureView(View view, int arg1) {
            return view == itemView;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == itemView) {
                if (left > 0) {
                    return 0;
                } else {
                    left = Math.max(left, -hiddenViewWidth);
                    return left;
                }
            }
            return 0;
        }

    };

    public SwipeItemLayout(Context context) {
        this(context, null);
    }

    public SwipeItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, callback);
    }

    public Status getCurrentStatus() {
        return status;
    }

    /**
     * Sets the status of layout
     *
     * @param status Open or Close
     * @param smooth animation
     */
    public void setStatus(Status status, boolean smooth) {
        this.status = status;
        if (status == Status.Open) {
            open(smooth);
        } else {
            close(smooth);
        }
    }

    public void setOnSwipeStatusListener(OnSwipeStatusListener listener) {
        this.listener = listener;
    }


    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }


    private void close(boolean smooth) {
        preStatus = status;
        status = Status.Close;
        if (smooth) {
            if (mDragHelper.smoothSlideViewTo(itemView, 0, 0)) {
                if (listener != null) {
                    Log.i(TAG, "start close animation");
                    listener.onStartCloseAnimation();
                }
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layout(status);
        }
        if (listener != null && preStatus == Status.Open) {
            Log.i(TAG, "close");
            listener.onStatusChanged(status);
        }
    }


    private void layout(Status status) {
        if (status == Status.Close) {
            hiddenView.layout(itemWidth, 0, itemWidth + hiddenViewWidth,
                    itemHeight);
            itemView.layout(0, 0, itemWidth, itemHeight);
        } else {
            hiddenView.layout(itemWidth - hiddenViewWidth, 0, itemWidth,
                    itemHeight);
            itemView.layout(-hiddenViewWidth, 0, itemWidth - hiddenViewWidth,
                    itemHeight);
        }
    }


    private void open(boolean smooth) {
        preStatus = status;
        status = Status.Open;
        if (smooth) {
            if (mDragHelper.smoothSlideViewTo(itemView, -hiddenViewWidth, 0)) {
                if (listener != null) {
                    Log.i(TAG, "start open animation");
                    listener.onStartOpenAnimation();
                }
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            layout(status);
        }
        if (listener != null && preStatus == Status.Close) {
            Log.i(TAG, "open");
            listener.onStatusChanged(status);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if (action == MotionEvent.ACTION_CANCEL) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        itemWidth = itemView.getMeasuredWidth();
        itemHeight = itemView.getMeasuredHeight();
        hiddenViewWidth = hiddenView.getMeasuredWidth();
        hiddenViewHeight = hiddenView.getMeasuredHeight();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // animation start
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        hiddenView = getChildAt(0);
        itemView = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        layout(Status.Close);
    }

    public enum Status {
        Open, Close
    }

    public interface OnSwipeStatusListener {

        void onStatusChanged(Status status);

        /**
         * when Close animation start
         */
        void onStartCloseAnimation();

        /**
         * when Open animation start
         */
        void onStartOpenAnimation();

    }

}
