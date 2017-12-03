package com.park.lunar.dribbble.view.shot_list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.park.lunar.dribbble.R;
import com.park.lunar.dribbble.model.Shot;
import com.park.lunar.dribbble.view.ShotActivity;
import com.park.lunar.dribbble.view.shot_item.ShotFragment;
import com.park.lunar.dribbble.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ShotListAdapter extends RecyclerView.Adapter {
    private List<Shot> shotList;
    private static final int VIEWTYPE_SHOT = 0;
    private static final int VIEWTYPE_LOADING = 1;

    private LoadMoreListner loadMoreListner;
    private boolean showLoading;
    /**
     * This is the default constructor
     * @param shotList the list we pass in to construct the object
     */


    public ShotListAdapter(@NonNull List<Shot> shotList,LoadMoreListner loadMoreListner) {
        this.shotList = shotList;
        this.loadMoreListner = loadMoreListner;
        this.showLoading = true;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch(viewType){
            case VIEWTYPE_SHOT:
               view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shot,parent,false);
                return new ShotViewHolder(view);
            case VIEWTYPE_LOADING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
                return new RecyclerView.ViewHolder(view){};
            default:return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        if (viewType == VIEWTYPE_LOADING) {
            loadMoreListner.onLoadMore();
        } else {
            final Shot shot = shotList.get(position);
            ShotViewHolder shotviewHolder = (ShotViewHolder) holder;
            shotviewHolder.likeCount.setText(String.valueOf(shot.likes_count));
            shotviewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
            shotviewHolder.viewCount.setText(String.valueOf(shot.views_count));
            shotviewHolder.image.setImageResource(R.drawable.shot_placeholder);

            Glide.with(holder.itemView.getContext())
                    .load(shot.getImageUrl())
                    .placeholder(R.drawable.shot_placeholder)
                    .into(shotviewHolder.image);

            shotviewHolder.shot_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = holder.itemView.getContext();
                    Intent intent = new Intent(context, ShotActivity.class);
                    intent.putExtra(ShotFragment.KEY_SHOT, ModelUtils.toString(shot, new TypeToken<Shot>() {
                    }));
                    intent.putExtra(ShotActivity.KEY_SHOT_TITLE, shot.title);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return showLoading ? shotList.size() + 1 : shotList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position < shotList.size()) {
            return VIEWTYPE_SHOT;
        }else{
            return VIEWTYPE_LOADING;
        }
    }

    public int getDataCount() {
        return shotList.size();
    }

    public void append(List<Shot> moreData) {
        shotList.addAll(moreData);
        notifyDataSetChanged();
    }

    public void setShowLoading(boolean showLoading){
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public interface LoadMoreListner {
        void onLoadMore();
    }
}
