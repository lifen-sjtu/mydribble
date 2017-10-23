package com.example.jlwang.mydribble.view.shot_item;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.view.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by jlwang on 10/17/17.
 */

class ShotInfoViewHolder extends BaseViewHolder {
    @BindView(R.id.shot_author_name) TextView shotAuthorName;
    @BindView(R.id.shot_author_pic) ImageView shotAuthorPic;
    @BindView(R.id.shot_title) TextView shotTitle;
    @BindView(R.id.shot_view_count) TextView shotViewCount;
    @BindView(R.id.shot_like_btn) ImageButton shotLikeBtn;
    @BindView(R.id.shot_like_count) TextView shotLikeCount;
    @BindView(R.id.shot_bucket_btn) ImageButton shotBucketBtn;
    @BindView(R.id.shot_bucket_count) TextView shotBucketCount;
    @BindView(R.id.shot_description) TextView shotDiscription;
    public ShotInfoViewHolder(View view) {
        super(view);
    }
}
