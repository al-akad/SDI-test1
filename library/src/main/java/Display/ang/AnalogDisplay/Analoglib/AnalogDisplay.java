/*******************************************************************************
 * Copyright (c) 2016 Thuat Nguyen
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package Display.ang.AnalogDisplay.Analoglib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class AnalogDisplay extends View {

    public static final int Maxim = 300;
    public static final float Topline = 0.0f;
    public static final float LEFT = 0.0f;
    public static final float RIGHT = 1.0f;
    public static final float BOTTOM = 1.0f;
    public static final float CENTER = 0.5f;

    public static final boolean AussereSchatten = true;
    public static final boolean ShowOuterBorder = true;
    public static final boolean ShowOuterRim = true;
    public static final boolean ShowInerRim = true;
    public static final boolean ShowNedel = true;
    public static final boolean ShowScale = false;
    public static final boolean ShowRanges = true;
    public static final boolean ShowText = false;
    public static final boolean StandarsShowRangeValues = false;

    public static final float OuterShadomWidth = 0.03f;
    public static final float OuterBorderWdith = 0.04f;
    public static final float OuterRimWidth = 0.05f;
    public static final float InnerRimWidth = 0.06f;
    public static final float InnerRimBorderWidth = 0.005f;

    public static final float NedelWidth = 0.025f;
    public static final float NedleHeigh = 0.32f;
    public static final int InnercircelColor = Color.rgb(1, 1, 226);
    public static final int OuterCircleColor = Color.rgb(205, 231, 132);


    public static final float ScalePosition = 0.015f;
    public static final float ScaleStartValue = 0.0f;
    public static final float ScaleEndValue = 100.0f;
    public static final float ScaleStartAngle = 60.0f;
    public static final int ScaleDivision = 10;
    public static final int ScaleSubdivision = 5;

    public static final int[] OUTER_SHADOW_COLORS = {Color.argb(40, 255, 254, 187), Color.argb(20, 255, 247, 219),
            Color.argb(5, 255, 255, 255)};
    public static final float[] OUTER_SHADOW_POS = {0.90f, 0.95f, 0.99f};

    public static final float[] RANGE_VALUES = {5000.0f};
    public static final int[] RangeColors = {Color.WHITE};

    public static final int TextShadowColor = Color.argb(100, 0, 0, 0);
    public static final int TextValueColor = Color.WHITE;
    public static final int TextUnitColor = Color.WHITE;
    public static final float TextValueSize = 0.3f;
    public static final float TextUnitSitze = 0.1f;
    private static final float[] StandardRangeValue = new float[]{0.0f, 100.0f, 200.0f, 500.0f, 1000.0f, 3000.0f, 5000.0f};
    private static final int StandardColor = Color.parseColor("#3498db");

    private static final int StandardFaceColor = Color.parseColor("#3F1F76");


    // *--------------------------------------------------------------------- *//
    // Customizable properties
    // *--------------------------------------------------------------------- *//

    private boolean mShowOuterShadow;
    private boolean mShowOuterBorder;
    private boolean mShowOuterRim;
    private boolean mShowInnerRim;
    private boolean mShowScale;
    private boolean mShowRanges;
    private boolean mShowNeedle;
    private boolean mShowText;



    private float mOuterShadowWidth;
    private float mOuterBorderWidth;
    private float mOuterRimWidth;
    private float mInnerRimWidth;
    private float mInnerRimBorderWidth;
    private float mNeedleWidth;
    private float mNeedleHeight;
    private int mInnerCircleColor;
    private int mOuterCircleColor;

    private float mScalePosition;
    private float mScaleStartValue;
    private float mScaleEndValue;
    private float mScaleStartAngle;
    private float[] mRangeValues;

    private int[] mRangeColors;
    private int mDivisions;
    private int mSubdivisions;

    private RectF mOuterShadowRect;
    private RectF mOuterBorderRect;
    private RectF mOuterRimRect;
    private RectF mInnerRimRect;
    private RectF mInnerRimBorderRect;
    private RectF mFaceRect;
    private RectF mScaleRect;

    private Bitmap mBackground;
    private Paint mBackgroundPaint;
    private Paint mOuterShadowPaint;
    private Paint mOuterBorderPaint;
    private Paint mOuterRimPaint;
    private Paint mInnerRimPaint;
    private Paint mInnerRimBorderLightPaint;
    private Paint mInnerRimBorderDarkPaint;
    private Paint mFacePaint;
    private Paint mFaceBorderPaint;
    private Paint mFaceShadowPaint;
    private Paint[] mRangePaints;
    private Paint mNeedleRightPaint;
    private Paint mNeedleLeftPaint;
    private Paint mNeedleScrewPaint;
    private Paint mNeedleScrewBorderPaint;
    private Paint mTextValuePaint;
    private Paint mTextUnitPaint;




    private String mTextValue;
    private String mTextUnit;
    private int mTextValueColor;
    private int mTextUnitColor;
    private int mTextShadowColor;
    private float mTextValueSize;
    private float mTextUnitSize;

    private Path mNeedleRightPath;
    private Path mNeedleLeftPath;

    // *--------------------------------------------------------------------- *//

    private float mScaleRotation;
    private float mDivisionValue;
    private float mSubdivisionValue;
    private float mSubdivisionAngle;

    private float mTargetValue;
    private float mCurrentValue;

    private float mNeedleVelocity;
    private float mNeedleAcceleration;
    private long mNeedleLastMoved = -1;
    private boolean mNeedleInitialized;
    private RectF mDynamicBorderRect;
    private boolean mShowRangeValues;

    public AnalogDisplay(final Context context, final AttributeSet attributeSet, final int DefStyle) {
        super(context, attributeSet, DefStyle);
        ReadAttributeSet(context, attributeSet, DefStyle);
        init();
    }

    public AnalogDisplay(final Context context, final AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AnalogDisplay(final Context context) {
        this(context, null, 0);
    }

    private void ReadAttributeSet(final Context context, final AttributeSet attributeSet, final int DefStyle) {
        final TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.AnalogDisplay, DefStyle, 0);
        mShowOuterShadow = a.getBoolean(R.styleable.AnalogDisplay_showOuterShadow, AussereSchatten);
        mShowOuterBorder = a.getBoolean(R.styleable.AnalogDisplay_showOuterBorder, ShowOuterBorder);
        mShowOuterRim = a.getBoolean(R.styleable.AnalogDisplay_showOuterRim, ShowOuterRim);
        mShowInnerRim = a.getBoolean(R.styleable.AnalogDisplay_showInnerRim, ShowInerRim);
        mShowNeedle = a.getBoolean(R.styleable.AnalogDisplay_showNeedle, ShowNedel);
        mShowScale = a.getBoolean(R.styleable.AnalogDisplay_showScale, ShowScale);
        mShowRanges = a.getBoolean(R.styleable.AnalogDisplay_showRanges, ShowRanges);
        mShowText = a.getBoolean(R.styleable.AnalogDisplay_showRangeText, StandarsShowRangeValues);

        mOuterShadowWidth = mShowOuterShadow ? a.getFloat(R.styleable.AnalogDisplay_outerShadowWidth, OuterShadomWidth) : 0.0f;
        mOuterBorderWidth = mShowOuterBorder ? a.getFloat(R.styleable.AnalogDisplay_outerBorderWidth, OuterBorderWdith) : 0.0f;
        mOuterRimWidth = mShowOuterRim ? a.getFloat(R.styleable.AnalogDisplay_outerRimWidth, OuterRimWidth) : 0.0f;
        mInnerRimWidth = mShowInnerRim ? a.getFloat(R.styleable.AnalogDisplay_innerRimWidth, InnerRimWidth) : 0.0f;
        mInnerRimBorderWidth = mShowInnerRim ? a.getFloat(R.styleable.AnalogDisplay_innerRimBorderWidth, InnerRimBorderWidth) : 0.0f;

        mNeedleWidth = a.getFloat(R.styleable.AnalogDisplay_needleWidth, NedelWidth);
        mNeedleHeight = a.getFloat(R.styleable.AnalogDisplay_needleHeight, NedleHeigh);
        mInnerCircleColor = a.getColor(R.styleable.AnalogDisplay_innerCircleColor, InnercircelColor);
        mOuterCircleColor = a.getColor(R.styleable.AnalogDisplay_outerCircleColor, OuterCircleColor);


        mScalePosition = (mShowScale || mShowRanges) ? a.getFloat(R.styleable.AnalogDisplay_scalePosition, ScalePosition) : 0.0f;
        mScaleStartValue = a.getFloat(R.styleable.AnalogDisplay_scaleStartValue, ScaleStartValue);
        mScaleEndValue = a.getFloat(R.styleable.AnalogDisplay_scaleEndValue, ScaleEndValue);
        mScaleStartAngle = a.getFloat(R.styleable.AnalogDisplay_scaleStartAngle, ScaleStartAngle);

        mDivisions = a.getInteger(R.styleable.AnalogDisplay_divisions, ScaleDivision);
        mSubdivisions = a.getInteger(R.styleable.AnalogDisplay_subdivisions, ScaleSubdivision);

        if (mShowRanges) {
            mTextShadowColor = a.getColor(R.styleable.AnalogDisplay_textShadowColor, TextShadowColor);

            final CharSequence[] rangeValues = a.getTextArray(R.styleable.AnalogDisplay_rangeValues);
            final CharSequence[] rangeColors = a.getTextArray(R.styleable.AnalogDisplay_rangeColors);
            ReadRanges(rangeValues, rangeColors);
            mShowRangeValues = StandarsShowRangeValues;
        }

        if (mShowText) {
            final int textValueId = a.getResourceId(R.styleable.AnalogDisplay_textValue, 0);
            final String textValue = a.getString(R.styleable.AnalogDisplay_textValue);
            mTextValue = (0 < textValueId) ? context.getString(textValueId) : (null != textValue) ? textValue : "";

            final int textUnitId = a.getResourceId(R.styleable.AnalogDisplay_Unit, 0);
            final String textUnit = a.getString(R.styleable.AnalogDisplay_Unit);
            mTextUnit = (0 < textUnitId) ? context.getString(textUnitId) : (null != textUnit) ? textUnit : "";
            mTextValueColor = a.getColor(R.styleable.AnalogDisplay_textValueColor, TextValueColor);
            mTextUnitColor = a.getColor(R.styleable.AnalogDisplay_UnitColor, TextUnitColor);
            mTextShadowColor = a.getColor(R.styleable.AnalogDisplay_textShadowColor, TextShadowColor);

            mTextValueSize = a.getFloat(R.styleable.AnalogDisplay_textValueSize, TextValueSize);
            mTextUnitSize = a.getFloat(R.styleable.AnalogDisplay_UnitSize, TextUnitSitze);
        }

        a.recycle();
    }

    private void ReadRanges(final CharSequence[] rangeValues, final CharSequence[] rangeColors) {

        int RangeValuesLength;
        if (rangeValues == null) {
            RangeValuesLength = StandardRangeValue.length;
        } else {
            RangeValuesLength = rangeValues.length;
        }
        final int length = RangeValuesLength;
        if (rangeValues != null) {
            mRangeValues = new float[length];
            for (int i = 0; i < length; i++) {
                mRangeValues[i] = Float.parseFloat(rangeValues[i].toString());
            }
        } else {
            mRangeValues = StandardRangeValue;
        }

        if (rangeColors != null) {
            mRangeColors = new int[length];
            for (int i = 0; i < length; i++) {
                mRangeColors[i] = Color.parseColor(rangeColors[i].toString());
            }
        } else {
            mRangeColors = RangeColors;
        }
    }

    @TargetApi(11)
    private void init()
    {
        // TODO Why isn't this working with HA layer?
        // The needle is not displayed although the onDraw() is being triggered by invalidate()
        // calls.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        initialDrawingRects();
        initialDrawingTools();

        // Compute the scale properties
        if (mShowRanges) {
            initialScale();
        }
    }

    public void initialDrawingRects() {
        // The drawing area is a rectangle of width 1 and height 1,
        // where (0,0) is the top left corner of the canvas.
        // Note that on Canvas X axis points to right, while the Y axis points downwards.
        mOuterShadowRect = new RectF(LEFT, Topline, RIGHT, BOTTOM);

        final float add = 0.015f;
        mOuterBorderRect = new RectF(mOuterShadowRect.left + mOuterShadowWidth + add, mOuterShadowRect.top + mOuterShadowWidth + add,
                mOuterShadowRect.right - mOuterShadowWidth - add, mOuterShadowRect.bottom - mOuterShadowWidth - add);

        mDynamicBorderRect = new RectF(mOuterBorderRect.left + 0.018f, mOuterBorderRect.top + 0.018f, mOuterBorderRect.right - 0.018f, mOuterBorderRect.bottom - 0.018f);

        mOuterRimRect = new RectF(mOuterBorderRect.left + mOuterBorderWidth, mOuterBorderRect.top + mOuterBorderWidth,
                mOuterBorderRect.right - mOuterBorderWidth, mOuterBorderRect.bottom - mOuterBorderWidth);

        mInnerRimRect = new RectF(mOuterRimRect.left + mOuterRimWidth, mOuterRimRect.top + mOuterRimWidth, mOuterRimRect.right
                - mOuterRimWidth, mOuterRimRect.bottom - mOuterRimWidth);

        mInnerRimBorderRect = new RectF(mInnerRimRect.left + mInnerRimBorderWidth, mInnerRimRect.top + mInnerRimBorderWidth,
                mInnerRimRect.right - mInnerRimBorderWidth, mInnerRimRect.bottom - mInnerRimBorderWidth);

        mFaceRect = new RectF(mInnerRimRect.left + mInnerRimWidth - add, mInnerRimRect.top + mInnerRimWidth - add,
                mInnerRimRect.right - mInnerRimWidth + add, mInnerRimRect.bottom - mInnerRimWidth + add);

        mScaleRect = new RectF(mFaceRect.left + mScalePosition, mFaceRect.top + mScalePosition, mFaceRect.right - mScalePosition,
                mFaceRect.bottom - mScalePosition);
    }

    private void initialDrawingTools() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setFilterBitmap(true);

        if (mShowOuterShadow) {
            mOuterShadowPaint = getDefaultOuterShadowPaint();
        }
        mOuterBorderPaint = getDefaultOuterBorderPaint();

        if (mShowOuterRim) {
            mOuterRimPaint = getDefaultOuterRimPaint();
        }
        if (mShowInnerRim) {
            mInnerRimPaint = getDefaultInnerRimPaint();
            mInnerRimBorderLightPaint = getDefaultInnerRimBorderLightPaint();
            mInnerRimBorderDarkPaint = getDefaultInnerRimBorderDarkPaint();
        }
        if (mShowRanges) {
            setDefaultScaleRangePaints();
        }
        if (mShowNeedle) {
            setDefaultNeedlePaths();
            mNeedleLeftPaint = getDefaultNeedleLeftPaint();
            mNeedleRightPaint = getDefaultNeedleRightPaint();
            mNeedleScrewPaint = getDefaultNeedleScrewPaint();
            mNeedleScrewBorderPaint = getDefaultNeedleScrewBorderPaint();
        }
        if (mShowText) {
            mTextValuePaint = getDefaultTextValuePaint();
            mTextUnitPaint = getDefaultTextUnitPaint();
        }

        mFacePaint = getDefaultFacePaint();
        mFaceBorderPaint = getDefaultFaceBorderPaint();
        mFaceShadowPaint = getDefaultFaceShadowPaint();
    }

    public Paint getDefaultOuterShadowPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(StandardColor);
        paint.setStrokeWidth(1f);
//		paint.setShader(new RadialGradient(CENTER, CENTER, mOuterShadowRect.width() / 2.0f, OUTER_SHADOW_COLORS, OUTER_SHADOW_POS,
//				TileMode.MIRROR));
        return paint;
    }

    private Paint getDefaultOuterBorderPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        return paint;
    }

    public Paint getDefaultOuterRimPaint() {
        // Use a linear gradient to create the 3D effect
//        final LinearGradient verticalGradient = new LinearGradient(mOuterRimRect.left, mOuterRimRect.top, mOuterRimRect.left,
//                mOuterRimRect.bottom, Color.rgb(255, 255, 255), Color.rgb(84, 90, 100), TileMode.REPEAT);

        // Use a Bitmap shader for the metallic style
//        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brushed_alu);
//        final BitmapShader aluminiumTile = new BitmapShader(bitmap, TileMode.REPEAT, TileMode.REPEAT);
//        final Matrix matrix = new Matrix();
//        matrix.setScale(1.0f / bitmap.getWidth(), 1.0f / bitmap.getHeight());
//        aluminiumTile.setLocalMatrix(matrix);

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setShader(new ComposeShader(verticalGradient, aluminiumTile, PorterDuff.Mode.MULTIPLY));
        paint.setFilterBitmap(true);
        return paint;
    }

    private Paint getDefaultInnerRimPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(StandardColor);
        paint.setStrokeWidth(1f);

//		paint.setShader(new LinearGradient(mInnerRimRect.left, mInnerRimRect.top, mInnerRimRect.left, mInnerRimRect.bottom, new int[]{
//				Color.argb(255, 68, 73, 80), Color.argb(255, 91, 97, 105), Color.argb(255, 178, 180, 183), Color.argb(255, 188, 188, 190),
//				Color.argb(255, 84, 90, 100), Color.argb(255, 137, 137, 137)}, new float[]{0, 0.1f, 0.2f, 0.4f, 0.8f, 1},
//				TileMode.CLAMP));
        return paint;
    }

    private Paint getDefaultInnerRimBorderLightPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(StandardColor);
        paint.setStrokeWidth(1f);
        return paint;
    }

    private Paint getDefaultInnerRimBorderDarkPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(StandardColor);
        paint.setStrokeWidth(1f);
        return paint;
    }

    public Paint getDefaultFacePaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(StandardFaceColor);
        return paint;
    }

    public Paint getDefaultFaceBorderPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(StandardColor);
        paint.setStrokeWidth(0.02f);
        return paint;
    }

    public Paint getDefaultFaceShadowPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(StandardColor);
        paint.setStrokeWidth(0.03f);

//		paint.setShader(new RadialGradient(0.5f, 0.5f, mFaceRect.width() / 2.0f, new int[]{Color.argb(60, 40, 96, 170),
//				Color.argb(80, 15, 34, 98), Color.argb(120, 0, 0, 0), Color.argb(140, 0, 0, 0)},
//				new float[]{0.60f, 0.85f, 0.96f, 0.99f}, TileMode.MIRROR));
        return paint;
    }

    public void setDefaultNeedlePaths() {
        final float x = 0.5f, y = 0.5f;
        mNeedleLeftPath = new Path();
        mNeedleLeftPath.moveTo(x, y);
        mNeedleLeftPath.lineTo(x - mNeedleWidth, y);
        mNeedleLeftPath.lineTo(x, y - mNeedleHeight);
        mNeedleLeftPath.lineTo(x, y);
        mNeedleLeftPath.lineTo(x - mNeedleWidth, y);

        mNeedleRightPath = new Path();
        mNeedleRightPath.moveTo(x, y);
        mNeedleRightPath.lineTo(x + mNeedleWidth, y);
        mNeedleRightPath.lineTo(x, y - mNeedleHeight);
        mNeedleRightPath.lineTo(x, y);
        mNeedleRightPath.lineTo(x + mNeedleWidth, y);
    }

    public Paint getDefaultNeedleLeftPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        return paint;
    }

    public Paint getDefaultNeedleRightPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        //paint.setShadowLayer(0.01f, 0.005f, -0.005f, Color.argb(127, 0, 0, 0));
        return paint;
    }

    public Paint getDefaultNeedleScrewPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //paint.setShader(new RadialGradient(0.5f, 0.5f, 0.07f, new int[]{Color.rgb(171, 171, 171), Color.WHITE}, new float[]{0.05f,
        //        0.9f}, TileMode.MIRROR));
        paint.setColor(Color.RED);
        return paint;
    }

    public Paint getDefaultNeedleScrewBorderPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(0.005f);
        return paint;
    }

    public void setDefaultRanges() {
        mRangeValues = new float[]{16, 25, 40, 100};
        mRangeColors = new int[]{Color.rgb(231, 32, 43), Color.rgb(232, 111, 33), Color.rgb(232, 231, 33), Color.rgb(27, 202, 33)};
    }

    public void setDefaultScaleRangePaints() {
        final int length = mRangeColors.length;
        mRangePaints = new Paint[length];
        for (int i = 0; i < length; i++) {
            mRangePaints[i] = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            mRangePaints[i].setColor(mRangeColors[i]);
            mRangePaints[i].setStyle(Paint.Style.STROKE);
            mRangePaints[i].setStrokeWidth(0.005f);
            mRangePaints[i].setTextSize(0.05f);
            mRangePaints[i].setTypeface(Typeface.SANS_SERIF);
            mRangePaints[i].setTextAlign(Align.CENTER);
            mRangePaints[i].setShadowLayer(0.005f, 0.002f, 0.002f, mTextShadowColor);
        }
    }

    public Paint getDefaultTextValuePaint() {
        final Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mTextValueColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(0.005f);
        paint.setTextSize(mTextValueSize);
        paint.setTextAlign(Align.CENTER);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setShadowLayer(0.01f, 0.002f, 0.002f, mTextShadowColor);
        return paint;
    }

    public Paint getDefaultTextUnitPaint() {
        final Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mTextUnitColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(0.005f);
        paint.setTextSize(mTextUnitSize);
        paint.setTextAlign(Align.CENTER);
        paint.setTypeface(Typeface.SANS_SERIF);
        paint.setShadowLayer(0.01f, 0.002f, 0.002f, mTextShadowColor);
        return paint;
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        final Bundle bundle = (Bundle) state;
        final Parcelable superState = bundle.getParcelable("superState");
        super.onRestoreInstanceState(superState);

        mNeedleInitialized = bundle.getBoolean("needleInitialized");
        mNeedleVelocity = bundle.getFloat("needleVelocity");
        mNeedleAcceleration = bundle.getFloat("needleAcceleration");
        mNeedleLastMoved = bundle.getLong("needleLastMoved");
        mCurrentValue = bundle.getFloat("currentValue");
        mTargetValue = bundle.getFloat("targetValue");
    }

    private void initialScale() {
        mScaleRotation = (mScaleStartAngle + 180) % 360;
        mDivisionValue = (mScaleEndValue - mScaleStartValue) / mDivisions;
        Log.d("mDivisionValue:", String.valueOf(mDivisionValue));
        mSubdivisionValue = mDivisionValue / mSubdivisions;
        mSubdivisionAngle = (360 - 2 * mScaleStartAngle) / (mDivisions * mSubdivisions);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();

        final Bundle state = new Bundle();
        state.putParcelable("superState", superState);
        state.putBoolean("needleInitialized", mNeedleInitialized);
        state.putFloat("needleVelocity", mNeedleVelocity);
        state.putFloat("needleAcceleration", mNeedleAcceleration);
        state.putLong("needleLastMoved", mNeedleLastMoved);
        state.putFloat("currentValue", mCurrentValue);
        state.putFloat("targetValue", mTargetValue);
        return state;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        // Loggable.log.debug(String.format("widthMeasureSpec=%s, heightMeasureSpec=%s",
        // View.MeasureSpec.toString(widthMeasureSpec),
        // View.MeasureSpec.toString(heightMeasureSpec)));

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int chosenWidth = chooseDimension(widthMode, widthSize);
        final int chosenHeight = chooseDimension(heightMode, heightSize);
        setMeasuredDimension(chosenWidth, chosenHeight);
    }

    private int chooseDimension(final int mode, final int size) {
        switch (mode) {
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.EXACTLY:
                return size;
            case View.MeasureSpec.UNSPECIFIED:
            default:
                return getDefaultDimension();
        }
    }

    private int getDefaultDimension() {
        return Maxim;
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        drawGauge();
    }

    private void drawGauge() {
        if (null != mBackground) {
            // Let go of the old background
            mBackground.recycle();
        }
        // Create a new background according to the new width and height
        mBackground = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(mBackground);
        final float scale = Math.min(getWidth(), getHeight());
        canvas.scale(scale, scale);
        canvas.translate((scale == getHeight()) ? ((getWidth() - scale) / 2) / scale : 0
                , (scale == getWidth()) ? ((getHeight() - scale) / 2) / scale : 0);

        drawRim(canvas);
        drawFace(canvas);

        if (mShowRanges) {
            drawScale(canvas);
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        drawBackground(canvas);

        final float scale = Math.min(getWidth(), getHeight());
        canvas.scale(scale, scale);
        canvas.translate((scale == getHeight()) ? ((getWidth() - scale) / 2) / scale : 0
                , (scale == getWidth()) ? ((getHeight() - scale) / 2) / scale : 0);
        if (mShowRanges) {
            if (mShowNeedle) {
                drawNeedle(canvas);
            }

            if (mShowText) {
                drawText(canvas);
            }

            computeCurrentValue();
        }
    }

    private void drawBackground(final Canvas canvas) {
        if (null != mBackground) {
            canvas.drawBitmap(mBackground, 0, 0, mBackgroundPaint);
        }
    }

    private void drawRim(final Canvas canvas) {
//        if (mShowOuterShadow) {
//            canvas.drawArc(mOuterShadowRect, 150, 240, true, mOuterShadowPaint);
//        }
        canvas.drawArc(mOuterBorderRect, 148, 244, true, mOuterBorderPaint);
//        if (mShowOuterRim) {
//            canvas.drawArc(mOuterRimRect, 150, 240, true, mOuterRimPaint);
//        }
//        if (mShowInnerRim) {
//            canvas.drawArc(mInnerRimRect, 150, 240, true, mInnerRimPaint);
//            canvas.drawArc(mInnerRimRect, 150, 240, true, mInnerRimBorderLightPaint);
//            canvas.drawArc(mInnerRimBorderRect, 150, 240, true, mInnerRimBorderDarkPaint);
//        }
    }

    private void drawFace(final Canvas canvas) {
        // Draw the face gradient
        canvas.drawArc(mFaceRect, 148, 244, false, mFacePaint);
        // Draw the face border
        //canvas.drawOval(mFaceRect, mFaceBorderPaint);
        // Draw the inner face shadow
        //canvas.drawOval(mFaceRect, mFaceShadowPaint);
    }

    private void drawText(final Canvas canvas) {
        final String textValue = !TextUtils.isEmpty(mTextValue) ? mTextValue : valueString(mCurrentValue);
        final float textValueWidth = mTextValuePaint.measureText(textValue);
        final float textUnitWidth = !TextUtils.isEmpty(mTextUnit) ? mTextUnitPaint.measureText(mTextUnit) : 0;

        final float startX = CENTER;
        final float startY = CENTER + 0.2f;

        drawText(canvas, -1, textValue, startX, startY, mTextValuePaint);

        if (!TextUtils.isEmpty(mTextUnit)) {
            drawText(canvas, -1, mTextUnit, CENTER, CENTER + 0.3f, mTextUnitPaint);
        }
    }

    private void drawScale(final Canvas canvas) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        // On canvas, North is 0 degrees, East is 90 degrees, South is 180 etc.
        // We start the scale somewhere South-West so we need to first rotate the canvas.
        canvas.rotate(mScaleRotation, 0.5f, 0.5f);
        Log.d("mScaleRotation: ", String.valueOf(mScaleRotation));

        final int totalTicks = mDivisions * mSubdivisions + 1;
        for (int i = 0; i < totalTicks; i++) {
            final float y1 = mScaleRect.top;
            Log.d("mScaleRect.top: ", String.valueOf(mScaleRect.top));

            final float y2 = y1 + 0.045f; // height of division
            final float y3 = y1 + 0.090f; // height of subdivision

            final float value = getValueForTick(i);
            final Paint paint = getRangePaint(value);

            float div = mScaleEndValue / (float) mDivisions;
            float mod = value % div;
//            if ((Math.abs(mod - 0) < 0.001) || (Math.abs(mod - div) < 0.001)) {
            // Draw a division tick
            paint.setStrokeWidth(0.01f);
            paint.setColor(Color.WHITE);
            canvas.drawLine(0.5f, y1 - 0.015f, 0.5f, y3 - 0.07f, paint);
            // Draw the text 0.15 away from the division tick
            //paint.setTextSize(1F);
            paint.setStyle(Paint.Style.FILL);
            //canvas.drawText(valueString(value), 0.49f, y3 + 0.05f, paint);
            if (mShowRangeValues) {
                drawText(canvas, i, valueString(value), 0.5f, y3, paint);
                Log.d("TEXT:", valueString(value));
            }
//            } else {
//                // Draw a subdivision tick
//                paint.setStrokeWidth(0.002f);
//                paint.setColor(Color.WHITE);
//                canvas.drawLine(0.5f, y1, 0.5f, y2, paint);
//            }
            canvas.rotate(mSubdivisionAngle, 0.5f, 0.5f);
            Log.d("mSubdivisionAngle: ", String.valueOf(mSubdivisionAngle));
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas, int tick, String value, float x, float y, Paint paint) {
        //Save original font size
        float originalTextSize = paint.getTextSize();

        // set a magnification factor
        final float magnifier = 100f;

        // Scale the canvas
        canvas.save();
        canvas.scale(1f / magnifier, 1f / magnifier);
        float textWidth = 0;
        float textHeight = 0;
        if (tick != -1) {
//            textWidth = paint.measureText(value);
            final int middleValue = mRangeValues.length / 2;
            if (tick == middleValue) {
                textHeight = -1;
                textWidth = 0;
            } else {
                textHeight = 1.5f;
                textWidth = -1;
            }
            canvas.rotate(120 - tick * mSubdivisionAngle, x * magnifier, y * magnifier);
        }
        // increase the font size
        paint.setTextSize(originalTextSize * magnifier);

        canvas.drawText(value, x * magnifier + textWidth, y * magnifier + textHeight, paint);

        //canvas.drawTextOnPath(value, textPath, 0.0f, 0.0f, paint);

        // bring everything back to normal
        canvas.restore();
        paint.setTextSize(originalTextSize);
    }

    /**
     * Draws a text in the canvas with spacing between each letter.
     * Basically what this method does is it split's the given text into individual letters
     * and draws each letter independently using Canvas.drawText with a separation of
     * {@code spacingX} between each letter.
     *
     * @param canvas    the canvas where the text will be drawn
     * @param text      the text what will be drawn
     * @param left      the left position of the text
     * @param top       the top position of the text
     * @param paint     holds styling information for the text
     * @param spacingPx the number of pixels between each letter that will be drawn
     */
    public static void drawSpacedText(Canvas canvas, String text, float left, float top, Paint paint, float spacingPx) {

        float currentLeft = left;

        for (int i = 0; i < text.length(); i++) {
            String c = text.charAt(i) + "";
            canvas.drawText(c, currentLeft, top, paint);
            currentLeft += spacingPx;
            currentLeft += paint.measureText(c);
        }
    }

    /**
     * returns the width of a text drawn by drawSpacedText
     */
    public static float getSpacedTextWidth(Paint paint, String text, float spacingX) {
        return paint.measureText(text) + spacingX * (text.length() - 1);
    }

    private String valueString(final float value) {
        return String.format("%d", (int) value);
    }

    private float getValueForTick(final int tick) {
        return mRangeValues[tick];
//        return tick * (mDivisionValue / mSubdivisions);
    }

    private Paint getRangePaint(final float value) {
        return mRangePaints[0];
//        final int length = mRangeValues.length;
//        for (int i = 0; i < length - 1; i++) {
//            if (value < mRangeValues[i]) return mRangePaints[i];
//        }
//        if (value <= mRangeValues[length - 1]) return mRangePaints[length - 1];
//        throw new IllegalArgumentException("Value " + value + " out of range!");
    }

    private void drawNeedle(final Canvas canvas) {
        if (mNeedleInitialized) {
            final float angle = getAngleForValue(mCurrentValue);
            Log.d("drawNeedle", String.format("value=%f -> angle=%f", mCurrentValue, angle));
            final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor("#3498db"));
            paint.setStrokeWidth(0.03f);
//            paint.setStrokeWidth(0.03f);
            float sweepAngle;
            if (angle != 0 && angle != 240) {
                if (angle < 240) {
                    sweepAngle = angle + 120;
                } else {
                    sweepAngle = angle - 240;
                }
                canvas.drawArc(mDynamicBorderRect, 148, sweepAngle, false, paint);
//            canvas.drawArc(mFaceRect, 150, angle, true, mFacePaint);
            }
            canvas.save(Canvas.MATRIX_SAVE_FLAG);
            canvas.rotate(angle, 0.5f, 0.5f);

//            Paint outerCircle = new Paint();
//            outerCircle.setColor(mOuterCircleColor);
//            outerCircle.setStyle(Paint.Style.FILL);
//            canvas.drawCircle(0.5f, 0.5f, 0.23f, outerCircle);
//
//            Paint innerCircle = new Paint();
//            innerCircle.setColor(mInnerCircleColor);
//            canvas.drawCircle(0.5f, 0.5f, 0.1f, innerCircle);

            setNeedleShadowPosition(angle);
            canvas.drawPath(mNeedleLeftPath, mNeedleLeftPaint);
            canvas.drawPath(mNeedleRightPath, mNeedleRightPaint);

            canvas.restore();

            // Draw the needle screw and its border


            canvas.drawCircle(0.5f, 0.5f, 0.04f, mNeedleScrewPaint);

            Log.d("NEEDLE", "Needle drawn :/");
            //canvas.drawCircle(0.5f, 0.5f, 0.04f, mNeedleScrewBorderPaint);
        }
    }

    private void setNeedleShadowPosition(final float angle) {
        if (angle > 180 && angle < 360) {
            // Move shadow from right to left
            mNeedleRightPaint.setShadowLayer(0, 0, 0, Color.BLACK);
            mNeedleLeftPaint.setShadowLayer(0.01f, -0.005f, 0.005f, Color.argb(127, 0, 0, 0));
        } else {
            // Move shadow from left to right
            mNeedleLeftPaint.setShadowLayer(0, 0, 0, Color.BLACK);
            mNeedleRightPaint.setShadowLayer(0.01f, 0.005f, -0.005f, Color.argb(127, 0, 0, 0));
        }
    }

    private float getAngleForValue(final float value) {
        if (!mShowRanges)
            return -1;
        int range = -1;
        for (int i = 0; i < mRangeValues.length - 1; ++i) {
            range++;
            if (value == mRangeValues[i]) {
                range = i;
                break;
            }
            if (value == mRangeValues[i + 1]) {
                range = i + 1;
                break;
            }
            if (value > mRangeValues[i] && value < mRangeValues[i + 1]) {
                break;
            }
        }

        if (range == -1)
            return 0;

        float angle = range * mSubdivisionAngle + (value - mRangeValues[range]) * mSubdivisionAngle / (mRangeValues[range + 1] - mRangeValues[range]);

        return (mScaleRotation + angle) % 360;
    }

    private void computeCurrentValue() {
        // Logger.log.warn(String.format("velocity=%f, acceleration=%f", mNeedleVelocity,
        // mNeedleAcceleration));

        if (!(Math.abs(mCurrentValue - mTargetValue) > 0.01f)) {
            return;
        }

        if (-1 != mNeedleLastMoved) {
            final float time = (System.currentTimeMillis() - mNeedleLastMoved) / 1000.0f;
            final float direction = Math.signum(mNeedleVelocity);
            if (Math.abs(mNeedleVelocity) < 90.0f) {
                mNeedleAcceleration = 5.0f * (mTargetValue - mCurrentValue);
            } else {
                mNeedleAcceleration = 0.0f;
            }

            mNeedleAcceleration = 5.0f * (mTargetValue - mCurrentValue);
            mCurrentValue += mNeedleVelocity * time;
            mNeedleVelocity += mNeedleAcceleration * time;

            if ((mTargetValue - mCurrentValue) * direction < 0.01f * direction) {
                mCurrentValue = mTargetValue;
                mNeedleVelocity = 0.0f;
                mNeedleAcceleration = 0.0f;
                mNeedleLastMoved = -1L;
            } else {
                mNeedleLastMoved = System.currentTimeMillis();
            }

            invalidate();

        } else {
            mNeedleLastMoved = System.currentTimeMillis();
            computeCurrentValue();
        }
    }

    public void setTargetValue(final float value) {
        if (mShowScale || mShowRanges) {
            if (value < mScaleStartValue) {
                mTargetValue = mScaleStartValue;
            } else if (value > mScaleEndValue) {
                mTargetValue = mScaleEndValue;
            } else {
                mTargetValue = value;
            }
        } else {
            mTargetValue = value;
        }
        mNeedleInitialized = true;
        invalidate();
    }

    public void setShowRangeValues(boolean mShowRangeValues) {
        this.mShowRangeValues = mShowRangeValues;
        mNeedleInitialized = true;
        invalidate();
    }
}
