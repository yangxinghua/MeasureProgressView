package com.ivan.measureprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by ivan on 10/27/16.
 */

public class MeasureProgressView extends View {

    private static final int DEFAULT_PROGRESS_COLOR = Color.GREEN;
    private static final int DEFAULT_STEP_TEXT_COLOR = Color.GREEN;
    private static final int DEFAULT_STEP_SIZE = 5;
    private static final int DEFAULT_STROKE_WIDTH = 10;

    private static final int HINT_GAP_WITH_LINE = 40;

    private static final int DEFAULT_TEXT_SIZE = 14;

    private static final String TAG = "MeasuremProgressView";

    private int mProgressOriginColor = Color.BLACK;

    private int mStepSize;
    private int mProgressColor;
    private int mStepTextColor;

    private String[] mHintArray = {"step1", "step2", "step3", "step4", "step5"};

    private float mUsedWidth;

    private int mCurrentStep;

    private int radius = DEFAULT_STROKE_WIDTH + 5;

    private float mStepTextSize;

    private Paint mProgressLinePaint;
    private Paint mCirclePaint;
    private Paint mHintPaint;

    public MeasureProgressView(Context context) {
        this(context, null);
    }

    public MeasureProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasureProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.MeasureProgressView);

        mStepSize = t.getInteger(R.styleable.MeasureProgressView_stepSize, DEFAULT_STEP_SIZE);
        mStepTextColor = t.getColor(R.styleable.MeasureProgressView_stepTextColor,
                DEFAULT_STEP_TEXT_COLOR);
        mProgressColor = t.getColor(R.styleable.MeasureProgressView_progressColor,
                DEFAULT_PROGRESS_COLOR);
        mStepTextSize = t.getInteger(R.styleable.MeasureProgressView_stepTextSize, DEFAULT_TEXT_SIZE);

        mProgressLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressLinePaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);

        mHintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHintPaint.setTextAlign(Paint.Align.CENTER);

        mStepTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, mStepTextSize, getResources().getDisplayMetrics());
        mHintPaint.setTextSize(mStepTextSize);

        t.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        mUsedWidth = 0.0f;
        float perCircleWidth = radius * 2;
        float perLineWidth = (width - mStepSize * perCircleWidth) / (mStepSize + 1);
        for (int i = 0; i < mStepSize; i++) {
            if (i < mCurrentStep) {
                mProgressLinePaint.setColor(mProgressColor);
                mCirclePaint.setColor(mProgressColor);
            } else {
                mProgressLinePaint.setColor(mProgressOriginColor);
                mCirclePaint.setColor(mProgressOriginColor);
            }

            canvas.drawLine(mUsedWidth, height / 2.0f,
                    mUsedWidth + perLineWidth, height / 2.0f, mProgressLinePaint);
            mUsedWidth += perLineWidth;

            if (i % 2 == 0) {
                canvas.drawText(mHintArray[i], mUsedWidth + radius, height / 2.0f - radius -
                        HINT_GAP_WITH_LINE, mHintPaint);
            } else {
                canvas.drawText(mHintArray[i], mUsedWidth + radius, height / 2.0f + radius +
                        DEFAULT_STROKE_WIDTH + HINT_GAP_WITH_LINE, mHintPaint);
            }

            canvas.drawCircle(mUsedWidth + DEFAULT_STROKE_WIDTH, height / 2.0f, radius, mCirclePaint);
            mUsedWidth += perCircleWidth;

        }

        canvas.drawLine(mUsedWidth, height / 2.0f, mUsedWidth + perLineWidth, height / 2.0f,
                mProgressLinePaint);

    }

    public void setStepTextArray(String[] array) {
        if (mStepSize <= 0) {
            Log.e(TAG, "please set step size before set hint array");
            return;
        }

        if (array.length < mStepSize) {
            Log.e(TAG, "the hint array length can't be less than step size");
            return;
        }

        mHintArray = array;
    }

    public void setCurrentStep(int currentStep) {
        mCurrentStep = currentStep;
        invalidate();
    }
}
