package com.ryanhuen.lib.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.ryanhuen.lib.R;


public class ShadowFocusView extends View implements ValueAnimator.AnimatorUpdateListener {
    private View mFocusView;
    private View mUnFocusView;
    private int[] mFocusLocation = new int[2];
    private int[] mRealDrawBorderFocusLocation = new int[2];
    private int[] mLocation = new int[2];
    private Drawable mDrawablePaint;
    private Drawable mDrawableDefault;
    private float mScaleUp = 1.0f;
    private Paint mPaint = new Paint();
    private boolean mBorderNeeded = true;
    private boolean mScaleNeeded = true;
    private boolean mMoveHighLightToBottom = false;
    private View mSpecifiedBorderView;

    public static final float DEFAULT_VIEW_SCALE = 1.1f;

    ValueAnimator animScaleUp = ValueAnimator.ofFloat(1.0F, DEFAULT_VIEW_SCALE)
            .setDuration(getResources().getInteger(R.integer.scale_up_duration));

    public ShadowFocusView(Context context) {
        super(context);
        init();
    }

    public ShadowFocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShadowFocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        mDrawablePaint = getResources().getDrawable(R.drawable.focus_highlight);
        mDrawableDefault = getResources().getDrawable(R.drawable.focus_highlight);
        mPaint.setColor(0xff000000);
        animScaleUp.addUpdateListener(this);
        animScaleUp.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawOnShadowView(canvas, mFocusView, mScaleNeeded ? mScaleUp : 1.0f);
    }

    View mClipView = null;
    private int[] mClipViewLocation = new int[2];

    public void setClipView(View v) {
        mClipView = v;
    }

    private void drawOnShadowView(Canvas canvas, View view, float scale) {
        if (view != null) {

            if (null == mLocation) {
                mLocation = new int[2];
            }
            if (null == mFocusLocation) {
                mFocusLocation = new int[2];
            }
            if (null == mRealDrawBorderFocusLocation) {
                mRealDrawBorderFocusLocation = new int[2];
            }
            getLocationInWindow(mLocation);
            view.getLocationInWindow(mFocusLocation);

            int width = view.getWidth();
            int height = view.getHeight();

            int offsetW = (int) (width * (scale - 1) / 2);
            int offsetH = (int) (height * (scale - 1) / 2);

            clipCanvasRect(canvas, offsetW, offsetH);

            Rect padding = new Rect();
            mDrawablePaint.getPadding(padding);

            int left = mFocusLocation[0] - mLocation[0] - offsetW;
            int top = mFocusLocation[1] - mLocation[1] - offsetH;

            if (null == mSpecifiedBorderView) {
                canvas.save();
                if (mMoveHighLightToBottom) {
                    //border be background
                    caculateDrawConfig(canvas, view, scale, offsetW, offsetH, padding, left, top);
                    if (mBorderNeeded) {
                        drawWholeViewBorder(canvas, width, height, padding);
                    }
                    view.draw(canvas);
                } else {
                    caculateDrawConfig(canvas, view, scale, offsetW, offsetH, padding, left, top);
                    view.draw(canvas);
                    if (mBorderNeeded) {
                        drawWholeViewBorder(canvas, width, height, padding);
                    }
                }
                canvas.restore();
            } else {
                //border be foreground
                if (mMoveHighLightToBottom) {
                    canvas.save();
                    drawSpecifiedDrawable(canvas, scale, padding, left, top);
                    canvas.restore();

                    canvas.save();
                    caculateDrawConfig(canvas, view, scale, offsetW, offsetH, padding, left, top);
                    view.draw(canvas);
                    canvas.restore();
                } else {
                    canvas.save();
                    caculateDrawConfig(canvas, view, scale, offsetW, offsetH, padding, left, top);
                    view.draw(canvas);
                    canvas.restore();

                    canvas.save();
                    drawSpecifiedDrawable(canvas, scale, padding, left, top);
                    canvas.restore();
                }
            }
        }
    }

    private void drawWholeViewBorder(Canvas canvas, int width, int height, Rect padding) {
        mDrawablePaint.setBounds(-padding.left, -padding.top,
                width + padding.right,
                height + padding.bottom);
        mDrawablePaint.draw(canvas);
    }

    private void caculateDrawConfig(Canvas canvas, View view, float scale, int offsetW, int offsetH, Rect padding, int left, int top) {
        canvas.translate(left, top);
        float canvasPivotX = whetherViewSetPivot(view, R.id.shadow_focus_item_pivot_horizontal, padding, scale);
        float canvasPivotY = whetherViewSetPivot(view, R.id.shadow_focus_item_pivot_vertical, padding, scale);
        canvas.translate(canvasPivotX, canvasPivotY);
        canvas.scale(scale, scale);
    }

    /**
     * whether should clip canvas（every time restore the canvas,the clip params will be reset）
     *
     * @param canvas  canvas
     * @param offsetW horizontal scale offset
     * @param offsetH vertical scale offset
     */
    private void clipCanvasRect(Canvas canvas, int offsetW, int offsetH) {
        if (mClipView != null) {
            mClipView.getLocationInWindow(mClipViewLocation);
            canvas.clipRect(mClipViewLocation[0] - offsetW, mClipViewLocation[1] - offsetH,
                    mClipViewLocation[0] + mClipView.getWidth() + offsetW,
                    mClipViewLocation[1] + mClipView.getHeight() + offsetH);
        }
    }

    private void drawSpecifiedDrawable(Canvas canvas, float scale, Rect padding, int left, int top) {
        mSpecifiedBorderView.getLocationInWindow(mRealDrawBorderFocusLocation);
        int toParentOffsetLeft = (int) ((mRealDrawBorderFocusLocation[0] - mFocusLocation[0]) * scale);
        int toParentOffsetTop = (int) ((mRealDrawBorderFocusLocation[1] - mFocusLocation[1]) * scale);
        int borderLeft = left + toParentOffsetLeft;
        int borderTop = top + toParentOffsetTop;
        canvas.translate(borderLeft, borderTop);
        mDrawablePaint.setBounds(-padding.left, -padding.top,
                mSpecifiedBorderView.getWidth() + padding.right,
                mSpecifiedBorderView.getHeight() + padding.bottom);
        float scalePivotX = whetherViewSetPivot(mSpecifiedBorderView, R.id.shadow_focus_item_pivot_horizontal, padding, scale);
        float scalePivotY = whetherViewSetPivot(mSpecifiedBorderView, R.id.shadow_focus_item_pivot_vertical, padding, scale);
        canvas.scale(scale, scale, scalePivotX, scalePivotY);
        mDrawablePaint.draw(canvas);
    }

    private float whetherViewSetPivot(View view, int key, Rect padding, float scale) {
        if (null == view.getTag(key)) {
            return 0f;
        }
        if (FocusHighlightHelper.VIEW_SCALE_PIVOT_X_LEFT == (int) view.getTag(key)) {
            return (view.getWidth()) * (scale - 1) / 2 + padding.left * scale;
        } else if (FocusHighlightHelper.VIEW_SCALE_PIVOT_X_RIGHT == (int) view.getTag(key)) {
            return -((view.getWidth()) * (scale - 1) / 2 + padding.right * scale);
        } else if (FocusHighlightHelper.VIEW_SCALE_PIVOT_Y_TOP == (int) view.getTag(key)) {
            return -(view.getHeight() * (scale - 1) / 2 + padding.top * scale);
        } else if (FocusHighlightHelper.VIEW_SCALE_PIVOT_Y_BOTTOM == (int) view.getTag(key)) {
            return view.getHeight() * (scale - 1) / 2 + padding.bottom * scale;
        } else {
            return 0f;
        }
    }

    public void setFocusView(View view, FocusHighlightOptions focusHighlightOptions) {
        if (focusHighlightOptions.needsSpecialBorder) {
            mSpecifiedBorderView = focusHighlightOptions.specifiedBorderView;
        } else {
            mBorderNeeded = focusHighlightOptions.needsBorder;
        }
        if (focusHighlightOptions.needsSpecialBackground) {
            mDrawablePaint = focusHighlightOptions.specifiedBackground;
            mMoveHighLightToBottom = focusHighlightOptions.needsMoveToBottom;
            mBorderNeeded = true;
        }
        mScaleNeeded = focusHighlightOptions.needsScale;
        view.setAlpha(0f);
        if (mFocusView != view) {
            mFocusView = view;
            mScaleUp = 1.0f;
            animScaleUp.start();
        }
        invalidate();
    }

    public void setUnFocusView(final View view) {
        mFocusView = null;
        view.setAlpha(1f);
        mBorderNeeded = true;
        mScaleNeeded = true;
        mMoveHighLightToBottom = false;
        mSpecifiedBorderView = null;
        mDrawablePaint = mDrawableDefault;
        if (mUnFocusView != view) {
            mUnFocusView = view;
        }
        invalidate();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mScaleUp = (float) animation.getAnimatedValue();
        invalidate();
    }
}
