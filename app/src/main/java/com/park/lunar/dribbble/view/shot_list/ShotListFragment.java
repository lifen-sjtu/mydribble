package com.park.lunar.dribbble.view.shot_list;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.park.lunar.dribbble.R;
import com.park.lunar.dribbble.Dribbble;
import com.park.lunar.dribbble.model.Like;
import com.park.lunar.dribbble.view.base.SpaceItemDecoration;
import com.park.lunar.dribbble.model.Shot;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;


public class ShotListFragment extends Fragment {
    public static final String KEY_SHOT_TITLE = "shot_title";
    private static final int COUNT_PER_PAGE = 12;
    public static final String KEY_SHOW_MODE ="show_mode";

    private RecyclerView recyclerView;
    private ShotListAdapter adapter;
    private List<Shot> shotList;
    private OkHttpClient client = new OkHttpClient();

    private boolean isHomePage;

    public static ShotListFragment newInstance(boolean isHomePage) {
        ShotListFragment shotListFragment = new ShotListFragment();
        Bundle args = new Bundle();
        args.putBoolean(KEY_SHOW_MODE,isHomePage);
        shotListFragment.setArguments(args);
        return shotListFragment;
    }


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
        isHomePage = getArguments().getBoolean(KEY_SHOW_MODE);
        adapter = new ShotListAdapter(new ArrayList<Shot>(), new ShotListAdapter.LoadMoreListner() {
            @Override
            public void onLoadMore() {
                if (isHomePage) {
                    AsyncTaskCompat.executeParallel(new LoadShotList(Dribbble.SHOT_END_POINT,adapter.getDataCount() / COUNT_PER_PAGE + 1));
                } else {
                    AsyncTaskCompat.executeParallel(new LoadLikeShotList(Dribbble.USER_END_POINT,adapter.getDataCount() / COUNT_PER_PAGE + 1));
                }

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
            }else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }

        }
    }

    private class LoadLikeShotList extends AsyncTask<Void, Void, List<Shot>> {

        String userLikesUrl;

        LoadLikeShotList(String imageUrl, int page) {
            this.userLikesUrl = imageUrl + "/likes?page=" + page;
        }

        @Override
        protected List<Shot> doInBackground(Void... voids) {
            try {
                Response response = Dribbble.makeGetRequest(userLikesUrl);
                List<Like> likeList = Dribbble.parseResponse(response,new TypeToken<List<Like>>(){});
                List<Shot> likeShotList = new ArrayList<>();
                for(Like like : likeList) {
                    likeShotList.add(like.shot);
                }
                Log.i("fen likeList",likeList.toString());
                Log.i("fen likeShotList",likeShotList.toString());
                return likeShotList;
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
            }else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }

        }
    }

}
