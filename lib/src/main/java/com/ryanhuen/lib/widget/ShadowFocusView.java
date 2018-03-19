package com.ryanhuen.lib.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.ryanhuen.lib.R;


public class ShadowFocusView extends View {
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

    ObjectAnimator animScaleUp = ObjectAnimator.ofFloat(this, "ScaleUp",
            new float[]{1.0F, DEFAULT_VIEW_SCALE}).setDuration(getResources().getInteger(R.integer.scale_up_duration));

    public ShadowFocusView(Context context) {
        super(context);
        init();
    }

    public ShadowFocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        mDrawablePaint = getResources().getDrawable(R.drawable.focus_highlight);
        mDrawableDefault = getResources().getDrawable(R.drawable.focus_highlight);
        mPaint.setColor(0xff000000);
        animScaleUp.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFocusView(canvas, mFocusView, mScaleNeeded ? mScaleUp : 1.0f, mBorderNeeded);
    }

    View mClipView = null;
    private int[] mClipViewLocation = new int[2];

    public void setClipView(View v) {
        mClipView = v;
    }

    public void drawFocusView(Canvas canvas, View view, float scale, boolean needBorder) {
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

            clipCanvalRect(canvas, offsetW, offsetH);

            Rect padding = new Rect();
            mDrawablePaint.getPadding(padding);

            int left = mFocusLocation[0] - mLocation[0] - offsetW;
            int top = mFocusLocation[1] - mLocation[1] - offsetH;

            if (null == mSpecifiedBorderView) {
                canvas.save();
                if (mMoveHighLightToBottom) {
                    //边框在View底部
                    caculateDrawConfig(canvas, view, scale, padding, left, top);
                    if (mBorderNeeded) {
                        drawWholeViewBorder(canvas, width, height, padding);
                    }
                    view.draw(canvas);
                } else {
                    caculateDrawConfig(canvas, view, scale, padding, left, top);
                    view.draw(canvas);
                    if (mBorderNeeded) {
                        drawWholeViewBorder(canvas, width, height, padding);
                    }
                }
                canvas.restore();
            } else {
                //边框在子View上
                if (mMoveHighLightToBottom) {
                    canvas.save();
                    drawSpecifiedDrawable(canvas, scale, padding, left, top);
                    canvas.restore();

                    canvas.save();
                    caculateDrawConfig(canvas, view, scale, padding, left, top);
                    view.draw(canvas);
                    canvas.restore();
                } else {
                    canvas.save();
                    caculateDrawConfig(canvas, view, scale, padding, left, top);
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

    private void caculateDrawConfig(Canvas canvas, View view, float scale, Rect padding, int left, int top) {
        canvas.translate(left, top);
        float canvasPivotX = whetherViewSetPivot(view, R.id.ryan_focus_item_pivot_x, padding, scale);
        float canvasPivotY = whetherViewSetPivot(view, R.id.ryan_focus_item_pivot_y, padding, scale);
        canvas.translate(canvasPivotX, canvasPivotY);
        canvas.scale(scale, scale);
    }

    /**
     * 判断是否需要裁剪画布（canvas的每次restore，都会重设画布大小，需要重新裁剪）
     *
     * @param canvas  画布
     * @param offsetW 横向缩放偏移
     * @param offsetH 纵向缩放偏移
     */
    private void clipCanvalRect(Canvas canvas, int offsetW, int offsetH) {
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
        float scalePivotX = whetherViewSetPivot(mSpecifiedBorderView, R.id.ryan_focus_item_pivot_x, padding, scale);
        float scalePivotY = whetherViewSetPivot(mSpecifiedBorderView, R.id.ryan_focus_item_pivot_y, padding, scale);
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
            //需要给特定子View绘制边框
            //此动作表示一定需要边框，因此mBorderNeeded保留默认为true
            mSpecifiedBorderView = focusHighlightOptions.specifiedBorderView;
        } else {
            //是否需要边框
            mBorderNeeded = focusHighlightOptions.needsBorder;
        }
        if (focusHighlightOptions.needsSpecialBackground) {
            //是否需要特殊边框（即修改边框样式）
            mDrawablePaint = focusHighlightOptions.specifiedBackground;
            //边框遮盖View还是View遮盖边框
            mMoveHighLightToBottom = focusHighlightOptions.needsMoveToBottom;
            //只要设置特殊背景就表示设置边框
            mBorderNeeded = true;
        }
        mScaleNeeded = focusHighlightOptions.needsScale;
        view.setAlpha(0f);
        if (mFocusView != view) {
            mFocusView = view;
            mScaleUp = 1.0f;
            animScaleUp.start();
        }
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

    /**
     * 该方法不能被混淆
     *
     * @param scale
     */
    public void setScaleUp(float scale) {
        mScaleUp = scale;
        invalidate();
    }

}
