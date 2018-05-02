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
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ryanhuen.lib.R;


public class FocusHighlightHelper {
    public static final String TAG = FocusHighlightHelper.class.getName();
    public static final boolean DEBUG = false;
    public static boolean hideMetroCursorView = false;  //是否隐藏metrocursorView

    public static final int VIEW_SCALE_PIVOT_X_LEFT = 0;
    public static final int VIEW_SCALE_PIVOT_X_RIGHT = 1;
    public static final int VIEW_SCALE_PIVOT_Y_TOP = 0;
    public static final int VIEW_SCALE_PIVOT_Y_BOTTOM = 1;

    private static Dialog sDialog;

    public static void focusHighlightView(View view, boolean hasFocus, FocusHighlightOptions options) {
        if (view.getContext() instanceof Activity) {
            final RHFocusCursorView cursorView = getFocusCursorView(view);
            if (hasFocus) {
                cursorView.setFocusView(view, options);
            } else {
                cursorView.setUnFocusView(view);
            }
        }
    }

    public static void focusHighlightView(Dialog dialog, View view,
                                          boolean hasFocus, FocusHighlightOptions options) {
        sDialog = dialog;
        final RHFocusCursorView cursorView = getFocusCursorViewFromDialogWindow(dialog);
        if (hasFocus) {
            cursorView.setFocusView(view, options);
        } else {
            cursorView.setUnFocusView(view);
        }
    }

    private static RHFocusCursorView getFocusCursorView(View view) {
        ViewGroup decorView;
        if (view.getContext() instanceof Activity) {
            decorView = (ViewGroup) ((Activity) view.getContext()).getWindow().getDecorView();
        } else {
            return getFocusCursorViewFromDialogWindow(sDialog);
        }
        RHFocusCursorView metroCursorView;
        metroCursorView = decorView.findViewById(R.id.ryan_focus_cursor_view);
        if (null == metroCursorView) {
            if (DEBUG) {
                Log.e(TAG, "didn't find MetroCursorView,we will create one");
            }
            metroCursorView = new RHFocusCursorView(view.getContext());
            metroCursorView.setId(R.id.ryan_focus_cursor_view);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            decorView.addView(metroCursorView, layoutParams);
        }
        return metroCursorView;
    }

    private static RHFocusCursorView getFocusCursorViewFromDialogWindow(Dialog dialog) {
        if (dialog.getWindow() != null) {
            ViewGroup decorView = (ViewGroup) dialog.getWindow().getDecorView();
            RHFocusCursorView metroCursorView;
            metroCursorView = decorView.findViewById(R.id.ryan_focus_cursor_view);
            if (null == metroCursorView) {
                if (DEBUG) {
                    Log.e(TAG, "didn't find MetroCursorView,we will create one");
                }
                metroCursorView = new RHFocusCursorView(dialog.getContext());
                metroCursorView.setId(R.id.ryan_focus_cursor_view);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                decorView.addView(metroCursorView, layoutParams);
            }
            return metroCursorView;
        } else {
            throw new RuntimeException("The Dialog which want to add CZFocusCursorView didn't get its host Window object");
        }
    }

    public static void setMetroClipView(View v) {
        getFocusCursorView(v).setClipView(v);
    }

    public static void clearMetroClipView(View v) {
        getFocusCursorView(v).setClipView(null);
        getFocusCursorView(v).invalidate();
    }

    public static void invalidateFocusView(View view) {
        RHFocusCursorView czFocusCursorView = getFocusCursorView(view);
        czFocusCursorView.invalidate();
    }
}
