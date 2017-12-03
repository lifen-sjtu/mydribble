package com.park.lunar.dribbble.view;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.park.lunar.dribbble.R;
import com.park.lunar.dribbble.view.shot_item.ShotFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        setTitle(getActivityTitle());

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.shot_fragment_container,newFragment())
                    .commit();
        }

    }

    protected Fragment newFragment() {
        return ShotFragment.newInstance(getIntent().getExtras());
    }

    protected String getActivityTitle() {
        return getIntent().getStringExtra(KEY_SHOT_TITLE);
    }
}
