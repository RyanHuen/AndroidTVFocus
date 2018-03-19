/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ryanhuen.lib.widget;

import android.app.Activity;
import android.view.View;

import com.ryanhuen.lib.R;

/**
 * Sets up the highlighting behavior when an item gains focus.
 */
public class FocusHighlightHelper {
    public static final int VIEW_SCALE_PIVOT_X_LEFT = 0;
    public static final int VIEW_SCALE_PIVOT_X_RIGHT = 1;
    public static final int VIEW_SCALE_PIVOT_Y_TOP = 0;
    public static final int VIEW_SCALE_PIVOT_Y_BOTTOM = 1;

    /**
     * @param view 对传入的View设置OnFocusChangeListener，适用于不需要自行管理Focus的情况
     */
    public static void focusHighlightViewDefault(View view) {
        if (view.getContext() instanceof Activity) {
            final ShadowFocusView cursorView = getShadowFocusView(view);
            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        cursorView.setFocusView(v, new FocusHighlightOptions());
                    } else {
                        cursorView.setUnFocusView(v);
                    }
                }
            });
        }
    }

    public static void focusHighlightView(View view, final FocusHighlightOptions options) {
        if (view.getContext() instanceof Activity) {
            final ShadowFocusView cursorView = getShadowFocusView(view);
            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        cursorView.setFocusView(v, options);
                    } else {
                        cursorView.setUnFocusView(v);
                    }
                }
            });
        }
    }

    /**
     * @param view     需要绘制动画的View
     * @param hasFocus 传入的view是否处于Focus状态,true时绘制Focus动画，false时绘制清楚Focus动画
     * @param options  Focus的选项
     */
    public static void focusHighlightView(View view, boolean hasFocus, FocusHighlightOptions options) {
        if (view.getContext() instanceof Activity) {
            Activity activity = (Activity) view.getContext();
            final ShadowFocusView cursorView = activity.findViewById(R.id.shadow_focus_view);
            if (cursorView == null) {
                throw new RuntimeException("Your View's host Activity Layout didn't found MetroCursorView to Draw Focus : " + activity);
            }
            if (hasFocus) {
                cursorView.setFocusView(view, options);
            } else {
                cursorView.setUnFocusView(view);
            }
        }
    }

    private static ShadowFocusView getShadowFocusView(View view) {
        Activity activity = (Activity) view.getContext();
        final ShadowFocusView cursorView = activity.findViewById(R.id.shadow_focus_view);
        if (cursorView == null) {
            throw new RuntimeException("Your View's host Activity Layout didn't found MetroCursorView to Draw Focus" + activity);
        }
        return cursorView;
    }

    public static void setMetroClipView(View v) {
        getShadowFocusView(v).setClipView(v);
    }

    public static void clearMetroClipView(View v) {
        getShadowFocusView(v).setClipView(null);
        getShadowFocusView(v).invalidate();
    }
}
