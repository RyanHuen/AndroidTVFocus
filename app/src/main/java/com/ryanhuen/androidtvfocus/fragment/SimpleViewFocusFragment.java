package com.ryanhuen.androidtvfocus.fragment;


import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryanhuen.androidtvfocus.R;
import com.ryanhuen.lib.widget.FocusHighlightHelper;
import com.ryanhuen.lib.widget.FocusHighlightOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleViewFocusFragment extends Fragment {

    private TextView mDefaultConfig;
    private TextView mNoScale;
    private TextView mScaleNoBorder;
    private LinearLayout mAdvanceFocus;
    private ImageView mTargetDrawBorder;
    private TextView mChangeBorder;
    private Button mHitDialog;

    public SimpleViewFocusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_simple_view_focus, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        mDefaultConfig = root.findViewById(R.id.default_config);
        mNoScale = root.findViewById(R.id.no_scale);
        mScaleNoBorder = root.findViewById(R.id.scale_no_border);
        mAdvanceFocus = root.findViewById(R.id.advance_focus);
        mTargetDrawBorder = root.findViewById(R.id.target_draw_border);
        mChangeBorder = root.findViewById(R.id.change_border);
        mHitDialog = (Button) root.findViewById(R.id.hit_dialog);

        //默认效果
        mDefaultConfig.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                FocusHighlightHelper.focusHighlightView(v, hasFocus, new FocusHighlightOptions());
            }
        });

        mNoScale.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //不放大
                FocusHighlightHelper.focusHighlightView(mNoScale, hasFocus, new FocusHighlightOptions
                        .Builder()
                        .needsScale(false)
                        .build());
            }
        });
        mScaleNoBorder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //只放大，不增加边框
                FocusHighlightHelper.focusHighlightView(mScaleNoBorder, hasFocus, new FocusHighlightOptions
                        .Builder()
                        .needsBorder(false)
                        .build());
            }
        });

        mChangeBorder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //修改Focus的边框样式
                FocusHighlightHelper.focusHighlightView(mChangeBorder, hasFocus, new FocusHighlightOptions
                        .Builder()
                        .specifiedBackground(getResources().getDrawable(R.drawable.bg_light_focus), true)
                        .build());
            }
        });


        mAdvanceFocus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //进阶Focus效果，指定ViewGroup中的某个View绘制边框
                FocusHighlightHelper.focusHighlightView(mAdvanceFocus, hasFocus, new FocusHighlightOptions
                        .Builder()
                        .specifiedViewWithBorder(mTargetDrawBorder)
                        .build());
            }
        });

        mHitDialog.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                FocusHighlightHelper.focusHighlightView(mHitDialog, hasFocus, new FocusHighlightOptions());
            }
        });
        mHitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Dialog);
                dialog.setContentView(LayoutInflater.from(getActivity())
                        .inflate(R.layout.dialog_for_focus_demo, null));

                View view = dialog.getWindow().getDecorView();
                final TextView textView = view.findViewById(R.id.text_dialog_focus);
                final ImageView imageView = view.findViewById(R.id.image_dialog_focus);
                textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        FocusHighlightHelper.focusHighlightView(dialog, textView, hasFocus, new FocusHighlightOptions());
                    }
                });
                imageView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        FocusHighlightHelper.focusHighlightView(dialog, imageView, hasFocus, new FocusHighlightOptions());
                    }
                });
                dialog.show();
            }
        });
    }

}
