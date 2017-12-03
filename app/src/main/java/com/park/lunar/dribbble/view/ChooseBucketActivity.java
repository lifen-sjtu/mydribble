package com.park.lunar.dribbble.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.park.lunar.dribbble.R;
import com.park.lunar.dribbble.view.buck_list.BucketListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChooseBucketActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Choose Bucket");

        ArrayList<String> collectedIds = getIntent().getStringArrayListExtra(BucketListFragment.KEY_CHOSEN_BUCKET_IDS);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.shot_fragment_container, BucketListFragment.newInstance(true,collectedIds))
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

}
