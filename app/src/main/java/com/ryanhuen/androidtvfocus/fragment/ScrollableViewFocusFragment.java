package com.ryanhuen.androidtvfocus.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanhuen.androidtvfocus.R;
import com.ryanhuen.androidtvfocus.adapter.SampleAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScrollableViewFocusFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public ScrollableViewFocusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scrollable_view_focus, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(
                new GridLayoutManager(getActivity(), 4,
                        GridLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new SampleAdapter(getActivity()));

        return view;
    }

}
