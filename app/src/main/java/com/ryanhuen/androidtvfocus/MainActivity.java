package com.ryanhuen.androidtvfocus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryanhuen.androidtvfocus.fragment.ScrollableViewFocusFragment;
import com.ryanhuen.androidtvfocus.fragment.SimpleViewFocusFragment;
import com.ryanhuen.lib.widget.FocusHighlightHelper;
import com.ryanhuen.lib.widget.FocusHighlightOptions;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private LinearLayout mSwitchMenu;
    private TextView mSimpleViewFocus;
    private TextView mScrollableViewFocus;
    private FrameLayout mMainContent;

    private ScrollableViewFocusFragment mScrollableViewFocusFragment;
    private SimpleViewFocusFragment mSimpleViewFocusFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initFragments();
    }

    private void initFragments() {
        mScrollableViewFocusFragment = new ScrollableViewFocusFragment();
        mSimpleViewFocusFragment = new SimpleViewFocusFragment();
    }

    private void initView() {
        mSwitchMenu = findViewById(R.id.switch_menu);
        mSimpleViewFocus = findViewById(R.id.simple_view_focus);
        mScrollableViewFocus = findViewById(R.id.scrollable_view_focus);
        mMainContent = findViewById(R.id.main_content);

        mSimpleViewFocus.setOnFocusChangeListener(this);
        mScrollableViewFocus.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        FocusHighlightHelper.focusHighlightView(v, hasFocus, new FocusHighlightOptions());
        if (v.getId() == R.id.simple_view_focus) {
            //simple view focus
            getFragmentManager().beginTransaction().replace(R.id.main_content
                    , mSimpleViewFocusFragment).commit();
        } else if (v.getId() == R.id.scrollable_view_focus) {
            //scrollable view focus
            getFragmentManager().beginTransaction().replace(R.id.main_content
                    , mScrollableViewFocusFragment).commit();
        }
    }
}
