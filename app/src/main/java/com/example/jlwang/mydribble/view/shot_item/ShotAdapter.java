package com.example.jlwang.mydribble.view.shot_item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.model.Shot;
import com.example.jlwang.mydribble.view.ChooseBucketActivity;
import com.example.jlwang.mydribble.view.buck_list.BucketListFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by jlwang on 10/17/17.
 */

public class ShotAdapter extends RecyclerView.Adapter{
    private static final int VIEW_TYPE_SHOT_IMAGE = 0;
    private static final int VIEW_TYPE_SHOT_INFO = 1;

    private ShotFragment shotFragment;
    private final Shot shot;

    private ArrayList<String> collectedBucketIds;
    public ShotAdapter(@NonNull ShotFragment shotFragment,@NonNull Shot shot) {
        this.shotFragment = shotFragment;
        this.shot = shot;
        this.collectedBucketIds = null;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Context context = holder.itemView.getContext();
        switch (viewType){
            case VIEW_TYPE_SHOT_IMAGE:
                Glide.with(holder.itemView.getContext())
                        .load(shot.getImageUrl())
                        .into(((ShotImageViewHolder) holder).shotImage);
                break;
            case VIEW_TYPE_SHOT_INFO:
                ShotInfoViewHolder shotInfoViewHolder = (ShotInfoViewHolder) holder;
                shotInfoViewHolder.shotTitle.setText(shot.title);
                shotInfoViewHolder.shotAuthorName.setText(shot.user.name);
                shotInfoViewHolder.shotDiscription.setText(Html.fromHtml(shot.description));

                shotInfoViewHolder.shotBucketCount.setText(String.valueOf(shot.buckets_count));
                shotInfoViewHolder.shotLikeCount.setText(String.valueOf(shot.likes_count));
                shotInfoViewHolder.shotViewCount.setText(String.valueOf(shot.views_count));

                shotInfoViewHolder.shotBucketBtn.setImageDrawable(
                        shot.bucketed
                                ? ContextCompat.getDrawable(context, R.drawable.ic_inbox_dribbble_18dp)
                                : ContextCompat.getDrawable(context, R.drawable.ic_inbox_black_18dp));

                shotInfoViewHolder.shotShareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        share(v.getContext());
                    }
                });

                shotInfoViewHolder.shotBucketBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bucket(v.getContext());
                    }
                });
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
    public List<String> getReadOnlyCollectedBucketIds() {
        return Collections.unmodifiableList(collectedBucketIds);
    }

    private void share(Context context) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shot.title + " " + shot.html_url);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_shot)));
    }


    private void bucket(Context context) {
        if (collectedBucketIds != null) {
            // collectedBucketIds == null means we're still loading
            Intent intent = new Intent(context, ChooseBucketActivity.class);
            intent.putStringArrayListExtra(BucketListFragment.KEY_CHOSEN_BUCKET_IDS,
                    collectedBucketIds);
            shotFragment.startActivityForResult(intent, ShotFragment.REQ_CODE_BUCKET);
        }
    }

    public void updateCollectedBucketIds(List<String> bucketList) {
        if (collectedBucketIds == null) {
            collectedBucketIds = new ArrayList<>();
        }
        collectedBucketIds.clear();
        collectedBucketIds.addAll(bucketList);

        shot.bucketed = !bucketList.isEmpty();
        notifyDataSetChanged();
    }

    public void updateCollectedBucketIds(@NonNull List<String> addedIds,
                                         @NonNull List<String> removedIds) {
        if (collectedBucketIds == null) {
            collectedBucketIds = new ArrayList<>();
        }

        collectedBucketIds.addAll(addedIds);
        collectedBucketIds.removeAll(removedIds);

        shot.bucketed = !collectedBucketIds.isEmpty();
        shot.buckets_count += addedIds.size() - removedIds.size();
        notifyDataSetChanged();
    }
}
