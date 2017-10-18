package com.example.jlwang.mydribble.shot_item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by jlwang on 10/17/17.
 */

class ShotImageViewHolder extends BaseViewHolder {
    @BindView(R.id.shot_image) ImageView shotImage;
    public ShotImageViewHolder(View view) {
        super(view);
    }
}
