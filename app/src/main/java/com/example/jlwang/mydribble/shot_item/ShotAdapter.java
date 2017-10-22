package com.example.jlwang.mydribble.shot_item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.model.Shot;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by jlwang on 10/17/17.
 */

public class ShotAdapter extends RecyclerView.Adapter{
    private static final int VIEW_TYPE_SHOT_IMAGE = 0;
    private static final int VIEW_TYPE_SHOT_INFO = 1;

    private final Shot shot;
    public ShotAdapter(@NonNull Shot shot) {
        this.shot = shot;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case VIEW_TYPE_SHOT_IMAGE:
                view = LayoutInflater.from(parent.getContext())
                                     .inflate(R.layout.shot_item_image,parent,false);
                return new ShotImageViewHolder(view);
            case VIEW_TYPE_SHOT_INFO:
                view = LayoutInflater.from(parent.getContext())
                                     .inflate(R.layout.shot_item_info,parent,false);
                return new ShotInfoViewHolder(view);
            default:return null;
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case VIEW_TYPE_SHOT_IMAGE:
                Picasso.with(holder.itemView.getContext())
                        .load(shot.html_url)
                        .placeholder(R.drawable.shot_placeholder)
                        .into(((ShotImageViewHolder) holder).shotImage);
                break;
            case VIEW_TYPE_SHOT_INFO:
                ShotInfoViewHolder shotInfoViewHolder = (ShotInfoViewHolder) holder;
                shotInfoViewHolder.shotTitle.setText(shot.title);
                shotInfoViewHolder.shotAuthorName.setText(shot.user.name);
                shotInfoViewHolder.shotDiscription.setText(shot.description);

                shotInfoViewHolder.shotBucketCount.setText(String.valueOf(shot.buckets_count));
                shotInfoViewHolder.shotLikeCount.setText(String.valueOf(shot.likes_count));
                shotInfoViewHolder.shotViewCount.setText(String.valueOf(shot.views_count));
                break;
            default:break;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return VIEW_TYPE_SHOT_IMAGE;
        }else{
            return VIEW_TYPE_SHOT_INFO;
        }
    }
}
