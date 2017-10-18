package com.example.jlwang.mydribble.shot_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by jlwang on 10/16/17.
 */

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
