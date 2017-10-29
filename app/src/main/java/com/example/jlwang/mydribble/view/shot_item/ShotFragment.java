package com.example.jlwang.mydribble.view.shot_item;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.dribbble.Dribbble;
import com.example.jlwang.mydribble.dribbble.auth.Auth;
import com.example.jlwang.mydribble.model.Bucket;
import com.example.jlwang.mydribble.model.Shot;
import com.example.jlwang.mydribble.utils.ModelUtils;
import com.example.jlwang.mydribble.view.buck_list.BucketListFragment;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Response;

/**
 * Created by jlwang on 10/17/17.
 */

public class ShotFragment extends Fragment{

    public static final String KEY_SHOT = "shot";
    public static final int REQ_CODE_BUCKET = 100;

    private RecyclerView recyclerView;
    private ShotAdapter adapter;
    private Shot shot;


    public static ShotFragment newInstance(@NonNull Bundle args) {
        ShotFragment fragment = new ShotFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        shot = ModelUtils.toObject(getArguments().getString(KEY_SHOT),
                                                    new TypeToken<Shot>(){});
        adapter = new ShotAdapter(this,shot);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        AsyncTaskCompat.executeParallel(new LoadCollectedBucketList());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("fen onActivityResult","success");
        if(requestCode == REQ_CODE_BUCKET && resultCode == Activity.RESULT_OK) {
            List<String> chosenBucketIds = data.getStringArrayListExtra(BucketListFragment.KEY_CHOSEN_BUCKET_IDS);
            List<String> addBucketIds = new ArrayList<>();
            List<String> removeBucketIds = new ArrayList<>();
            List<String> collectedBucktedIds = adapter.getReadOnlyCollectedBucketIds();
            Log.i("fen chosenBucket",chosenBucketIds.toString());
            Log.i("fen collectedBucket",collectedBucktedIds.toString());

            for(String chosenBucketId : chosenBucketIds) {
                if(!collectedBucktedIds.contains(chosenBucketId)) {
                    addBucketIds.add(chosenBucketId);
                }
            }

            for(String collectedBucketId : collectedBucktedIds) {
                if(!chosenBucketIds.contains(collectedBucketId)) {
                    removeBucketIds.add(collectedBucketId);
                }
            }
            Log.i("fen added",addBucketIds.toString());
            Log.i("fen removed",removeBucketIds.toString());
            AsyncTaskCompat.executeParallel(new UpdateBucketTask(addBucketIds, removeBucketIds));

        }
    }

    private class LoadCollectedBucketList extends AsyncTask<Void,Void,List<String>> {
        String shotBucketUrl;
        String userBucketUrl;

        LoadCollectedBucketList() {
            this.shotBucketUrl = Dribbble.SHOT_END_POINT + "/"+ shot.id + "/buckets";
            this.userBucketUrl = Dribbble.USER_END_POINT + "/buckets";
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                Response response = Dribbble.makeGetRequest(shotBucketUrl);
                Response response1 = Dribbble.makeGetRequest(userBucketUrl);
                List<Bucket> shotBucketList = Dribbble.parseResponse(response,new TypeToken<List<Bucket>>(){});
                List<Bucket> userBucketList = Dribbble.parseResponse(response1,new TypeToken<List<Bucket>>(){});

                Set<String> userBucketIds = new HashSet<>();
                for (Bucket userBucket : userBucketList) {
                    userBucketIds.add(userBucket.id);
                }
                List<String> collectedBucketIds = new ArrayList<>();
                for (Bucket shotBucket : shotBucketList) {
                    if (userBucketIds.contains(shotBucket.id)) {
                        collectedBucketIds.add(shotBucket.id);
                    }
                }
                return collectedBucketIds;

            } catch (IOException|JsonSyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> buckets) {
            adapter.updateCollectedBucketIds(buckets);
        }
    }

    private class UpdateBucketTask extends AsyncTask<Void,Void,Void> {
        private List<String> added;
        private List<String> removed;
        private Exception e;
        public UpdateBucketTask(List<String> added, List<String> removed) {
            this.added = added;
            this.removed = removed;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for(String addedId : added) {
                    String url = Dribbble.BUCKETS_END_POINT + "/" + addedId + "/shots";
                    FormBody formBody = new FormBody.Builder()
                            .add(Auth.KEY_SHOT_ID, shot.id)
                            .build();
                    Response response = Dribbble.makePutRequest(url, formBody);
                    Dribbble.checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT);
                }

                for(String removedId : removed) {
                    String url = Dribbble.BUCKETS_END_POINT + "/" + removedId + "/shots";
                    FormBody formBody = new FormBody.Builder()
                            .add(Auth.KEY_SHOT_ID, shot.id)
                            .build();
                    Response response = Dribbble.makeDeleteRequest(url, formBody);
                    Dribbble.checkStatusCode(response, HttpURLConnection.HTTP_NO_CONTENT);
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.e = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(e == null) {
                adapter.updateCollectedBucketIds(added,removed);
            }else {
                Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
