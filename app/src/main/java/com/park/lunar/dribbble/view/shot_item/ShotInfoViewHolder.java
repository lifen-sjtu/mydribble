package com.park.lunar.dribbble.view.shot_item;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.park.lunar.dribbble.R;
import com.park.lunar.dribbble.view.base.BaseViewHolder;

import butterknife.BindView;


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
    @BindView(R.id.shot_share_action) TextView shotShareBtn;
    public ShotInfoViewHolder(View view) {
        super(view);
    }
}
