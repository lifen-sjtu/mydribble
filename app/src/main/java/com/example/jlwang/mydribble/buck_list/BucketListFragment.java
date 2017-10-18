package com.example.jlwang.mydribble.buck_list;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.base.SpaceItemDecoration;
import com.example.jlwang.mydribble.model.Bucket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jlwang on 10/17/17.
 */

public class BucketListFragment extends Fragment{
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_floating,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));
        recyclerView.setAdapter(new BucketListAdapter(mockData()));
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Try replacing the root layout of R.layout.fragment_fab_recycler_view with
                // FragmentLayout to see what Snackbar looks like
                Snackbar.make(v, "Fab clicked", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private List<Bucket> mockData() {
        List<Bucket> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            Bucket bucket = new Bucket();
            bucket.name = "Bucket" + i;
            bucket.shots_count = random.nextInt(10);
            list.add(bucket);

        }
        return list;
    }
}
