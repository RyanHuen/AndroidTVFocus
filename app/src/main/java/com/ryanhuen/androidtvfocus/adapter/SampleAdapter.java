package com.ryanhuen.androidtvfocus.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryanhuen.androidtvfocus.R;
import com.ryanhuen.lib.widget.FocusHighlightHelper;
import com.ryanhuen.lib.widget.FocusHighlightOptions;

/**
 * Created by ryanhuen on 18-3-19.
 */

public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.SampleViewHolder> {

    private Context mContext;

    public SampleAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public SampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SampleViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SampleViewHolder holder, int position) {
        if (position % 2 == 0) {
            FocusHighlightHelper.focusHighlightView(holder.itemView, new FocusHighlightOptions());
        } else {
            FocusHighlightHelper.focusHighlightView(holder.itemView, new FocusHighlightOptions.Builder()
                    .specifiedViewWithBorder(holder.mIcon).build());
        }
    }

    @Override
    public int getItemCount() {
        return 500;
    }

    class SampleViewHolder extends RecyclerView.ViewHolder {
        ImageView mIcon;
        TextView mTitle;

        public SampleViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.icon);
            mTitle = itemView.findViewById(R.id.title);
        }
    }
}
