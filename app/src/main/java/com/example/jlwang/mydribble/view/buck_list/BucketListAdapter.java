package com.example.jlwang.mydribble.view.buck_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.model.Bucket;
import com.example.jlwang.mydribble.model.Shot;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by jlwang on 10/17/17.
 */

class BucketListAdapter extends RecyclerView.Adapter {
    private final static int VIEW_TYPE_BUCKET = 0;
    private final static int VIEW_TYPR_LOADING = 1;
    private List<Bucket> bucketList;
    private boolean showLoading;
    private LoadMoreListner loadMoreListner;
    private boolean isChoosing;

    public BucketListAdapter(@NonNull List<Bucket> bucketList, LoadMoreListner loadMoreListner,boolean isChoosing) {
        this.bucketList = bucketList;
        this.loadMoreListner = loadMoreListner;
        this.isChoosing = isChoosing;
        showLoading = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_BUCKET:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bucket,parent,false);
                return new BucketViewHolder(view);
            case VIEW_TYPR_LOADING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
                return new RecyclerView.ViewHolder(view){};
            default:return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int viewType = getItemViewType(position);
        if(viewType == VIEW_TYPR_LOADING) {
            loadMoreListner.onLoadMore();
        } else {
            Bucket bucket = bucketList.get(position);

            String bucketShotCountString = MessageFormat.format(
                    holder.itemView.getContext().getResources().getString(R.string.shot_count),
                    bucket.shots_count);

            BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
            bucketViewHolder.bucket_name.setText(bucket.name);
            bucketViewHolder.bucket_shot_count.setText(bucketShotCountString);

            if(isChoosing) {
                bucketViewHolder.bucketChosen.setVisibility(View.VISIBLE);
            } else {
                bucketViewHolder.bucketChosen.setVisibility(View.GONE);
            }


        }
    }

    @Override
    public int getItemCount() {
        return showLoading ? bucketList.size() + 1 : bucketList.size();
    }


    @Override
    public int getItemViewType(int position) {
        if(position < bucketList.size()) {
            return VIEW_TYPE_BUCKET;
        }else{
            return VIEW_TYPR_LOADING;
        }
    }

    public int getDataCount() {
        return bucketList.size();
    }

    public void append(List<Bucket> moreData) {
        bucketList.addAll(moreData);
        notifyDataSetChanged();
    }

    public void setShowLoading(boolean showLoading){
        this.showLoading = showLoading;
        notifyDataSetChanged();
    }

    public void prepend(List<Bucket> buckets) {
        this.bucketList.addAll(0, buckets);
        notifyDataSetChanged();
    }

    public interface LoadMoreListner {
        void onLoadMore();
    }
}
