package com.park.lunar.dribbble.view.shot_item;

import android.view.View;
import android.widget.ImageView;

import com.park.lunar.dribbble.R;
import com.park.lunar.dribbble.view.base.BaseViewHolder;

import butterknife.BindView;

class ShotImageViewHolder extends BaseViewHolder {
    @BindView(R.id.shot_image) ImageView shotImage;
    public ShotImageViewHolder(View view) {
        super(view);
    }
}
