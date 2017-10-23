package com.example.jlwang.mydribble.view.buck_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.model.Bucket;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by jlwang on 10/17/17.
 */

class BucketListAdapter extends RecyclerView.Adapter {
    private List<Bucket> bucketList;

    public BucketListAdapter(@NonNull List<Bucket> bucketList) {
        this.bucketList = bucketList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bucket,parent,false);
        return new BucketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Bucket bucket = bucketList.get(position);

        String bucketShotCountString = MessageFormat.format(
                holder.itemView.getContext().getResources().getString(R.string.shot_count),
                bucket.shots_count);

        BucketViewHolder bucketViewHolder = (BucketViewHolder) holder;
        bucketViewHolder.bucket_name.setText(bucket.name);
        bucketViewHolder.bucket_shot_count.setText(bucketShotCountString);

    }

    @Override
    public int getItemCount() {
        return bucketList.size();
    }
}
