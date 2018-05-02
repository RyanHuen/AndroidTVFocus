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
         * @param needsScale whether scale view
         */
        public Builder needsScale(boolean needsScale) {
            this.needsScale = needsScale;
            return this;
        }

        /**
         * @param needsBorder whether draw border
         */
        public Builder needsBorder(boolean needsBorder) {
            this.needsBorder = needsBorder;
            return this;
        }

        /**
         * specified an extra child view to draw border
         *
         * @param view the child view whom be draw border
         */
        public Builder specifiedViewWithBorder(View view) {
            this.needsSpecialBorder = true;
            this.specifiedBorderView = view;
            return this;
        }

        /**
         * whether specified an extra background drawable to draw
         *
         * @param specifiedBackground extra background drawable
         * @param needsMoveToBottom   foreground or background
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
