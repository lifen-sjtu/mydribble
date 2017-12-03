package com.park.lunar.dribbble.view.buck_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.park.lunar.dribbble.R;
import com.park.lunar.dribbble.view.base.BaseViewHolder;

import butterknife.BindView;

class BucketViewHolder extends BaseViewHolder {
    @BindView(R.id.bucket_name) TextView bucket_name;
    @BindView(R.id.bucket_shot_count) TextView bucket_shot_count;
    @BindView(R.id.bucket_chosen_icon) ImageView bucketChosen;
    @BindView(R.id.bucket_layout) View bucketLayout;
    public BucketViewHolder(View view) {
        super(view);
    }
}
