package com.example.jlwang.mydribble.view.shot_list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.dribbble.Dribbble;
import com.example.jlwang.mydribble.view.MainActivity;
import com.example.jlwang.mydribble.view.base.SpaceItemDecoration;
import com.example.jlwang.mydribble.model.Shot;
import com.example.jlwang.mydribble.model.User;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by jlwang on 10/16/17.
 */

public class ShotListFragment extends Fragment {
    public static final String KEY_SHOT_TITLE = "shot_title";
    private static final int COUNT_PER_PAGE = 12;

    private RecyclerView recyclerView;
    private ShotListAdapter adapter;
    private List<Shot> shotList;
    private OkHttpClient client = new OkHttpClient();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));

        adapter = new ShotListAdapter(new ArrayList<Shot>(), new ShotListAdapter.LoadMoreListner() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(new LoadShotList(Dribbble.GET_SHOT_LIST_URL,adapter.getDataCount() / COUNT_PER_PAGE + 1));
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private class LoadShotList extends AsyncTask<Void, Void, List<Shot>> {

        String imageUrl;

        LoadShotList(String imageUrl, int page) {
            this.imageUrl = imageUrl + "?page=" + page;
        }

        @Override
        protected List<Shot> doInBackground(Void... voids) {
            try {
                Response response = Dribbble.makeGetRequest(imageUrl);
                return Dribbble.parseResponse(response,new TypeToken<List<Shot>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Shot> shots) {
            if(shots != null) {
                adapter.append(shots);
                adapter.setShowLoading(shots.size() == COUNT_PER_PAGE);
            }

        }
    }

}
