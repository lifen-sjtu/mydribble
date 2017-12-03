package com.park.lunar.dribbble.view.shot_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.park.lunar.dribbble.R;
import com.park.lunar.dribbble.view.base.BaseViewHolder;

import butterknife.BindView;

public class ShotViewHolder extends BaseViewHolder {
    @BindView(R.id.shot_image) ImageView image;
    @BindView(R.id.shot_clickable_cover) View shot_cover;
    @BindView(R.id.shot_view_count) TextView viewCount;
    @BindView(R.id.shot_like_count) TextView likeCount;
    @BindView(R.id.shot_bucket_count) TextView bucketCount;

    public ShotViewHolder(View view) {
        super(view);
    }
}
