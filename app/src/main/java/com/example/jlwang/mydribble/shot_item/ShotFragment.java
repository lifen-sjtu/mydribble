package com.example.jlwang.mydribble.shot_item;

import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.model.Shot;
import com.example.jlwang.mydribble.model.User;
import com.example.jlwang.mydribble.utils.ModelUtils;
import com.google.gson.reflect.TypeToken;

/**
 * Created by jlwang on 10/17/17.
 */

public class ShotFragment extends Fragment{

    public static final String KEY_SHOT = "shot";

    RecyclerView recyclerView;


    public static ShotFragment newInstance(@NonNull Bundle args) {
        Log.i("fen", "ShotFragment newInstance ");
        ShotFragment fragment = new ShotFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment,container,false);
        Log.i("fen", "ShotFragment onCreateView ");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i("fen", "ShotFragment onViewCreated ");
        Shot shot = ModelUtils.toObject(getArguments().getString(KEY_SHOT),
                                                    new TypeToken<Shot>(){});
//        Shot shot = new Shot();
//        shot.title = "shot" + 1;
//        shot.buckets_count = 1000;
//        shot.likes_count = 89;
//        shot.views_count=123;
//        shot.user = new User();
//        shot.user.name = shot.title + " author";

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ShotAdapter(shot));
    }
}
