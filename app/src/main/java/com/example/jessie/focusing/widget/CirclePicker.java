package com.example.jessie.focusing.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.jessie.focusing.Interface.OnTimeChangeListener;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.TimeHelper;

import static android.os.Build.VERSION_CODES.M;
import static com.example.jessie.focusing.Utils.TimeHelper.DAY_IN_MILLIS;
import static com.example.jessie.focusing.Utils.TimeHelper.HOUR_IN_MILLIS;

/**
 * @author : Yujie Lyu
 * @date : 22-01-2019
 * @time : 21:04
 */
public class CirclePicker extends View {
    public static final int DEF_MAX_ANGLE = 720;
    private final Context context;
    private float mStartDegree; //the angle of start button
    private float mEndDegree; //the angle of end button
    private int mRingDefaultColor;
    private int mBtnSize;
    private float mStartBtnAngle;
    private float mEndBtnAngle;
    private int mBtnImgSize;
    private float mStartBtnCurX, mStartBtnCurY;
    private float mEndBtnCurX, mEndBtnCurY;
    private Paint mCirclePaint;
    private Paint mProgressPaint;
    private Bitmap mClockBg;
    private int maxAngle;
    private Bitmap mStartBtnBg;
    private Bitmap mEndBtnBg;
    private int mMinViewSize;
    private int mClockSize;
    private Paint mDefaultPaint;
    private int mCenterY;
    private int mCenterX;
    private int mWheelRadius;
    private int mMoveFlag;//1 for start btn , 2 for end btn
    private OnTimeChangeListener mOnTimeChangeListener;
    private int mStartBtnColor;
    private int mEndBtnColor;
    private Paint mStartBtnPaint;
    private Paint mEndBtnPaint;
    private float mLastEventX;
    private float mLastEventY;

    public CirclePicker(Context context) {
        this(context, null);
    }

    public CirclePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttrs(attrs, defStyleAttr);
        initPaints();
        initValue();
    }

    public int getMaxAngle() {
        return maxAngle;
    }

    private void initValue() {
        mMoveFlag = -1;
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePicker, defStyle, 0);
        maxAngle = typedArray.getInt(R.styleable.CirclePicker_max_angle, DEF_MAX_ANGLE);
        mStartDegree = typedArray.getFloat(R.styleable.CirclePicker_Start_Degree, 0);
        mEndDegree = typedArray.getFloat(R.styleable.CirclePicker_End_Degree, 45);

        if (mStartDegree > maxAngle)
            mStartDegree = mStartDegree % maxAngle;

        if (mEndDegree > maxAngle)
            mEndDegree = mEndDegree % maxAngle;

        int startBtnBgId = typedArray.getResourceId(R.styleable.CirclePicker_Start_Btn_Bg, R.mipmap.icon_circle_picker_start_btn);
        mStartBtnBg = BitmapFactory.decodeResource(getResources(), startBtnBgId);

        int endBtnBgId = typedArray.getResourceId(R.styleable.CirclePicker_End_Btn_Bg, R.mipmap.icon_circle_picker_end_btn);
        mEndBtnBg = BitmapFactory.decodeResource(getResources(), endBtnBgId);
        mBtnImgSize = Math.max(Math.max(mStartBtnBg.getWidth(), mStartBtnBg.getHeight()), Math.max(mEndBtnBg.getWidth(), mEndBtnBg.getHeight()));
        int mBtnOffset = typedArray.getInt(R.styleable.CirclePicker_Btn_Offset_Size, 8);
        mBtnSize = typedArray.getInt(R.styleable.CirclePicker_Btn_Width, mBtnImgSize + mBtnOffset);
        int clockBgId = typedArray.getResourceId(R.styleable.CirclePicker_Clock_Bg, R.mipmap.setalarm_colock_bg);
        mClockBg = BitmapFactory.decodeResource(getResources(), clockBgId);
        mClockSize = Math.max(mClockBg.getWidth(), mClockBg.getHeight());
        mMinViewSize = mBtnSize * 2 + mClockSize;
        mRingDefaultColor = typedArray.getColor(R.styleable.CirclePicker_Ring_Default_Color, Color.parseColor("#70c6c6c6"));
        mStartBtnColor = typedArray.getColor(R.styleable.CirclePicker_Start_Btn_Color, Color.parseColor("#7c85e6"));
        mEndBtnColor = typedArray.getColor(R.styleable.CirclePicker_End_Btn_Color, Color.parseColor("#bf7be0"));

        typedArray.recycle();
    }

    private void initPaints() {

        mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultPaint.setDither(false);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(false);
        mCirclePaint.setColor(mRingDefaultColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setStrokeWidth(mBtnSize);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setDither(false);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mBtnSize);


        mStartBtnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStartBtnPaint.setDither(false);
        mStartBtnPaint.setColor(mStartBtnColor);
        mStartBtnPaint.setStyle(Paint.Style.FILL);

        mEndBtnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEndBtnPaint.setDither(false);
        mEndBtnPaint.setColor(mEndBtnColor);
        mEndBtnPaint.setStyle(Paint.Style.FILL);
    }

    @TargetApi(M)
    private int getColor(int colorId) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return getContext().getColor(colorId);
        } else {
            return ContextCompat.getColor(getContext(), colorId);
        }
    }

    private void refreshBtnPosition() {
        refreshStartBtnPosition();
        refreshEndBtnPosition();
    }

    public void refreshStartBtnPosition() {
        //Transfer to 360
        mStartBtnAngle = mStartDegree % 360;
        double startCos = Math.cos(Math.toRadians(mStartBtnAngle));
        MakeCurPosition(startCos);
    }

    public void refreshEndBtnPosition() {
        mEndBtnAngle = mEndDegree % 360;
        double endCos = Math.cos(Math.toRadians(mEndBtnAngle));
        MakeCurPosition2(endCos);
    }

    private void MakeCurPosition(double cos) {
        mStartBtnCurX = calcXLocationInWheel(mStartBtnAngle, cos);
        mStartBtnCurY = calcYLocationInWheel(cos);
    }

    private void MakeCurPosition2(double cos2) {
        mEndBtnCurX = calcXLocationInWheel(mEndBtnAngle, cos2);
        mEndBtnCurY = calcYLocationInWheel(cos2);
    }

    private float calcXLocationInWheel(double angle, double cos) {
        if (angle < 180) {
            return (float) (getMeasuredWidth() / 2 + Math.sqrt(1 - cos * cos) * (mMinViewSize - mBtnSize) / 2);
        } else {
            return (float) (getMeasuredWidth() / 2 - Math.sqrt(1 - cos * cos) * (mMinViewSize - mBtnSize) / 2);
        }
    }

    private float calcYLocationInWheel(double cos) {
        return (float) (getMeasuredHeight() / 2 - cos * (mMinViewSize - mBtnSize) / 2);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (getParent() != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (getDistance(event.getX(), event.getY(), mCenterX, mCenterY) > mMinViewSize / 2 + mBtnSize) {
            return super.onTouchEvent(event);
        }

        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//
                if (isMoveEndBtn(eventX, eventY)) {
                    mMoveFlag = 2;
                }
//
                mLastEventX = eventX;
                mLastEventY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:

//                A = Y2 - Y1
//                B = X1 - X2
//                C = X2*Y1 - X1*Y2


                if (mMoveFlag == 2) {

                    float a1 = mCenterY - mEndBtnCurY;
                    float b1 = mEndBtnCurX - mCenterX;
                    float c1 = mEndBtnCurY * mCenterX - mCenterY * mEndBtnCurX;
                    double d1 = (a1 * eventX + b1 * eventY + c1) / (Math.sqrt(a1 * a1 + b1 * b1));

                    float b2 = -a1;
                    float c2 = -b1 * mCenterX - b2 * mCenterY;
                    double d2 = (b1 * eventX + b2 * eventY + c2) / (Math.sqrt(b1 * b1 + b2 * b2));
                    double moveDegree = Math.toDegrees(Math.atan2(d1, d2));
                    mEndDegree = (float) (mEndDegree + Math.floor(moveDegree));
                    mEndDegree = (mEndDegree < 0) ? mEndDegree + maxAngle : mEndDegree % maxAngle;
                    if (mOnTimeChangeListener != null) {
                        long startTime = toMillis(mStartDegree) + TimeHelper.getStartOfToday();
                        long endTime = toMillis(mEndDegree) + TimeHelper.getStartOfToday();
                        mOnTimeChangeListener.onEndTimeChanged(startTime, endTime);
                    }
                    refreshEndBtnPosition();
                    Log.d("Test", "mEndDegree==" + mEndDegree);
                    Log.d("Test", "d1==" + d1 + "\n" +
                            "d2==" + d2 + "\n" +
                            "moveDegree==" + moveDegree + "\n" +
                            "mEndBtnAngle==" + mEndBtnAngle + "\n" +
                            "mEndBtnCurX==" + mEndBtnCurX + "\n" +
                            "/mEndBtnCurY==" + mEndBtnCurY);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mMoveFlag = -1;
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;

        mWheelRadius = (mMinViewSize - mBtnSize) / 2;
        canvas.drawCircle(mCenterX, mCenterY, mWheelRadius, mCirclePaint);
        canvas.drawBitmap(mClockBg, mCenterX - mClockSize / 2F, mCenterY - mClockSize / 2F, mDefaultPaint);

        float begin = 0;
        float sweep = 0;
        if (mStartBtnAngle > 180 && mStartBtnAngle > mEndBtnAngle) {   //180  -- 360
            begin = -Math.abs(mStartBtnAngle - 360) - 90;
            sweep = Math.abs(Math.abs(mStartBtnAngle - 360) + mEndBtnAngle);
        } else if (mStartBtnAngle > mEndBtnAngle) {
            begin = mStartBtnAngle - 90;
            sweep = 360 - (mStartBtnAngle - mEndBtnAngle);
        } else {
            begin = mStartBtnAngle - 90;
            sweep = Math.abs(mStartBtnAngle - mEndBtnAngle);
        }
        mProgressPaint.setShader(new LinearGradient(mStartBtnCurX, mStartBtnCurY, mEndBtnCurX, mEndBtnCurY, mStartBtnColor, mEndBtnColor, Shader.TileMode.CLAMP));
        canvas.drawArc(new RectF(mCenterX - mWheelRadius, mCenterY - mWheelRadius, mCenterX + mWheelRadius, mCenterY + mWheelRadius), begin, sweep, false, mProgressPaint);
        canvas.drawCircle(mEndBtnCurX, mEndBtnCurY, mBtnSize / 2F, mEndBtnPaint);
        canvas.drawBitmap(mEndBtnBg, mEndBtnCurX - mBtnImgSize / 2F, mEndBtnCurY - mBtnImgSize / 2F, mDefaultPaint);
//        canvas.drawCircle(mStartBtnCurX, mStartBtnCurY, mBtnSize / 2, mStartBtnPaint);
//        canvas.drawBitmap(mStartBtnBg, mStartBtnCurX - mBtnImgSize / 2, mStartBtnCurY - mBtnImgSize / 2, mDefaultPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = Math.max(mMinViewSize, heightSize);
        } else {
            width = Math.max(mMinViewSize, widthSize);
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            height = Math.max(mMinViewSize, widthSize);
        } else {
            height = Math.max(mMinViewSize, heightSize);
        }
        setMeasuredDimension(width, height);
        refreshBtnPosition();
    }

    private boolean isMoveEndBtn(float x, float y) {
        float dx = Math.abs(mEndBtnCurX - x);
        float dy = Math.abs(mEndBtnCurY - y);
        return dx < mBtnSize / 2 && dy < mBtnSize / 2;
    }

    public void setOnTimerChangeListener(OnTimeChangeListener listener) {
        if (mOnTimeChangeListener == null) {
            this.mOnTimeChangeListener = listener;
        }
    }

    public void setInitialTime(long startTime, long endTime) {
        long midnight = TimeHelper.getStartOfToday();
        mStartDegree = toAngle(startTime - midnight);
        mEndDegree = toAngle(endTime - midnight);
        if (mOnTimeChangeListener != null) {
            mOnTimeChangeListener.onInitTime(startTime, endTime);
        }
        refreshBtnPosition();
    }

    private long toMillis(float angle) {
        return (long) (angle / maxAngle * getTotalHours() * HOUR_IN_MILLIS);
    }

    private float toAngle(long millis) {
        if (millis < 0) {
            millis += DAY_IN_MILLIS;
        }
        return maxAngle * millis * 1F / (HOUR_IN_MILLIS * getTotalHours());
    }

    public float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    private int getTotalHours() {
        return maxAngle == DEF_MAX_ANGLE ? 24 : 12;
    }

}
