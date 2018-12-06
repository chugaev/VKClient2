package com.anatoly.chugaev.VKAPP.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.anatoly.chugaev.VKAPP.R;

public class LoadingViewHolder extends RecyclerView.ViewHolder{
    public ProgressBar mProgressBar;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        mProgressBar = itemView.findViewById(R.id.progress_bar);
    }

    public void bind() {
        mProgressBar.setIndeterminate(true);
    }
}
