package com.park.lunar.dribbble.view.buck_list;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.park.lunar.dribbble.R;
import com.park.lunar.dribbble.Dribbble;
import com.park.lunar.dribbble.view.base.SpaceItemDecoration;
import com.park.lunar.dribbble.model.Bucket;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class BucketListFragment extends Fragment{
    private static final int COUNT_PER_PAGE = 12;
    public static final int REQ_CODE_NEW_BUCKET = 101;
    public static final String KEY_CHOOSING_MODE = "choose_mode";
    public static final String KEY_CHOSEN_BUCKET_IDS = "chosen_bucket_ids";
    private RecyclerView recyclerView;
    private BucketListAdapter adapter;
    private OkHttpClient client = new OkHttpClient();
    private boolean isChoosingMode;
    private ArrayList<String> chosenBucketIds;

    public static BucketListFragment newInstance(boolean chooseMode, ArrayList<String> collectedBucketIds) {
        BucketListFragment bucketListFragment = new BucketListFragment();

        Bundle args = new Bundle();
        args.putStringArrayList(KEY_CHOSEN_BUCKET_IDS,collectedBucketIds);
        args.putBoolean(KEY_CHOOSING_MODE,chooseMode);
        bucketListFragment.setArguments(args);
        return bucketListFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_floating,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isChoosingMode = getArguments().getBoolean(KEY_CHOOSING_MODE);
        if(isChoosingMode) {
            chosenBucketIds = getArguments().getStringArrayList(KEY_CHOSEN_BUCKET_IDS);
            if(chosenBucketIds == null) {
                chosenBucketIds = new ArrayList<>();
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.spacing_medium)));
        adapter = new BucketListAdapter(new ArrayList<Bucket>(), new BucketListAdapter.LoadMoreListner() {
            @Override
            public void onLoadMore() {
                AsyncTaskCompat.executeParallel(new LoadBucketList(Dribbble.USER_END_POINT,adapter.getDataCount() / COUNT_PER_PAGE + 1));
            }
        }, isChoosingMode);
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BucketDialogFragment dialog = BucketDialogFragment.newInstance();
                dialog.setTargetFragment(BucketListFragment.this, REQ_CODE_NEW_BUCKET);
                dialog.show(getFragmentManager(),BucketDialogFragment.TAG);

                //Snackbar.make(v, "Fab clicked", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_NEW_BUCKET && resultCode == Activity.RESULT_OK) {
            String bucketName = data.getStringExtra(BucketDialogFragment.KEY_BUCKET_NAME);
            String bucketDescription = data.getStringExtra(BucketDialogFragment.KEY_BUCKET_DESCRIPTION);
            if (!TextUtils.isEmpty(bucketName)) {
                AsyncTaskCompat.executeParallel(new NewBucketTask(bucketName, bucketDescription));
            }
        }
    }

    private class LoadBucketList extends AsyncTask<Void,Void,List<Bucket>> {
        String bucketUrl;

        LoadBucketList(String bucketUrl, int page) {
            this.bucketUrl = bucketUrl + "/"+ "buckets?page=" + page;
        }

        @Override
        protected List<Bucket> doInBackground(Void... params) {
            try {
                Response response = Dribbble.makeGetRequest(bucketUrl);
                return Dribbble.parseResponse(response,new TypeToken<List<Bucket>>(){});
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Bucket> buckets) {
            if(buckets != null) {
                if(isChoosingMode) {
                    for(Bucket bucket: buckets) {
                        if(chosenBucketIds.contains(bucket.id)) {
                            bucket.isChoosing = true;
                        }
                    }
                }

                adapter.append(buckets);
                adapter.setShowLoading(buckets.size() == COUNT_PER_PAGE);
            }else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private class NewBucketTask extends AsyncTask<Void,Void,Bucket>{
        private String name;
        private String description;

        public NewBucketTask(String bucketName, String bucketDescription) {
            this.name = bucketName;
            this.description = bucketDescription;
        }

        @Override
        protected Bucket doInBackground(Void... params) {
            FormBody formBody = new FormBody.Builder()
                    .add(Dribbble.KEY_NAME, name)
                    .add(Dribbble.KEY_DESCRIPTION, description)
                    .build();
            try {
                Log.i("fen response",Dribbble.makePostRequest(Dribbble.BUCKETS_END_POINT, formBody).toString());
                return Dribbble.parseResponse(Dribbble.makePostRequest(Dribbble.BUCKETS_END_POINT, formBody), Dribbble.BUCKET_TYPE);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bucket bucket) {
            if (bucket != null) {
                adapter.prepend(Collections.singletonList(bucket));
            } else {
                Snackbar.make(getView(), "Error!", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.ic_save) {
            ArrayList<String> chosenBucketIds = adapter.getSelectedBucketIds();

            Log.i("fen save","save success");
            Intent result = new Intent();
            result.putStringArrayListExtra(KEY_CHOSEN_BUCKET_IDS,chosenBucketIds);
            getActivity().setResult(Activity.RESULT_OK,result);
            getActivity().finish();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isChoosingMode) {
            inflater.inflate(R.menu.menu_save, menu);
        }
    }

}
