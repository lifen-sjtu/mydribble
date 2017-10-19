package com.example.jlwang.mydribble.shot_list;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.model.Shot;
import com.example.jlwang.mydribble.shot_item.ShotActivity;
import com.example.jlwang.mydribble.shot_item.ShotFragment;
import com.example.jlwang.mydribble.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jlwang on 10/16/17.
 */

public class ShotListAdapter extends RecyclerView.Adapter {
    private List<Shot> shotList;
    private Context context;
    /**
     * This is the default constructor
     * @param shotList the list we pass in to construct the object
     */


    public ShotListAdapter(@NonNull List<Shot> shotList) {
        this.shotList = shotList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shot,parent,false);
        return new ShotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position){
        final Shot shot = shotList.get(position);
        ShotViewHolder shotviewHolder = (ShotViewHolder) holder;
        shotviewHolder.likeCount.setText(String.valueOf(shot.likes_count));
        shotviewHolder.bucketCount.setText(String.valueOf(shot.buckets_count));
        shotviewHolder.viewCount.setText(String.valueOf(shot.views_count));
        shotviewHolder.image.setImageResource(R.drawable.shot_placeholder);

        Picasso.with(context).load(shot.html_url).fit().into(shotviewHolder.image);
        shotviewHolder.shot_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                Intent intent = new Intent(context, ShotActivity.class);
                intent.putExtra(ShotFragment.KEY_SHOT, ModelUtils.toString(shot,new TypeToken<Shot>(){}));
                intent.putExtra(ShotActivity.KEY_SHOT_TITLE,shot.title);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shotList.size();
    }

}
