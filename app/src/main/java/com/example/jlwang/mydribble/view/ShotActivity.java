package com.example.jlwang.mydribble.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.view.shot_item.ShotFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jlwang on 10/17/17.
 */

public class ShotActivity  extends AppCompatActivity{
    @BindView(R.id.toolbar) Toolbar toolbar;

    public static final String KEY_SHOT_TITLE = "shot_title";

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setTitle(getActivityTitle());

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.shot_fragment_container,newFragment())
                    .commit();
        }

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    protected Fragment newFragment() {
        return ShotFragment.newInstance(getIntent().getExtras());
    }

    protected String getActivityTitle() {
        return getIntent().getStringExtra(KEY_SHOT_TITLE);
    }
}
