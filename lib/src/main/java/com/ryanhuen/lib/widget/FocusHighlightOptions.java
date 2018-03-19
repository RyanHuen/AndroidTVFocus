package com.ryanhuen.lib.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

public class FocusHighlightOptions {
    public static final class Builder {
        private boolean needsScale = true;
        private boolean needsBorder = true;
        private boolean needsSpecialBorder = false;
        private boolean needsSpecialBackground = false;
        private boolean needsMoveToBottom = false;
        private View specifiedBorderView;
        private Drawable specifiedBackground;

        /**
         * @param needsScale 是否需要放大
         */
        public Builder needsScale(boolean needsScale) {
            this.needsScale = needsScale;
            return this;
        }

        /**
         * @param needsBorder 是否需要绘制Focus边框
         */
        public Builder needsBorder(boolean needsBorder) {
            this.needsBorder = needsBorder;
            return this;
        }

        /**
         * 指定额外的View进行绘制边框
         *
         * @param view 要绘制边框的View
         */
        public Builder specifiedViewWithBorder(View view) {
            this.needsSpecialBorder = true;
            this.specifiedBorderView = view;
            return this;
        }

        /**
         * 是否指定额外的Focus背景
         *
         * @param specifiedBackground 额外指定的Focus Drawable
         * @param needsMoveToBottom    Focus时是否作为背景图
         */
        public Builder specifiedBackground(Drawable specifiedBackground, boolean needsMoveToBottom) {
            this.needsSpecialBackground = true;
            this.needsMoveToBottom = needsMoveToBottom;
            this.specifiedBackground = specifiedBackground;
            return this;
        }

        public FocusHighlightOptions build() {
            final FocusHighlightOptions handler = new FocusHighlightOptions();
            handler.needsScale = needsScale;
            handler.needsBorder = needsBorder;
            if (needsSpecialBorder) {
                handler.specifiedBorderView = specifiedBorderView;
                handler.needsSpecialBorder = true;
            }
            if (needsSpecialBackground) {
                handler.needsSpecialBackground = true;
                handler.needsMoveToBottom = needsMoveToBottom;
                handler.specifiedBackground = specifiedBackground;
            }
            return handler;
        }
    }

    boolean needsScale = true;
    boolean needsBorder = true;
    boolean needsSpecialBorder = false;
    View specifiedBorderView;
    boolean needsSpecialBackground = false;
    boolean needsMoveToBottom = false;
    Drawable specifiedBackground;
}
