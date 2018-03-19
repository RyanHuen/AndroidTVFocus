package com.ryanhuen.androidtvfocus.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanhuen.androidtvfocus.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScrollableViewFocusFragment extends Fragment {


    public ScrollableViewFocusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scrollable_view_focus, container, false);
    }

}
