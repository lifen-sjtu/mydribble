package com.example.jlwang.mydribble.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.dribbble.Dribbble;
import com.example.jlwang.mydribble.dribbble.auth.Auth;
import com.example.jlwang.mydribble.view.buck_list.BucketListFragment;
import com.example.jlwang.mydribble.view.shot_list.ShotListFragment;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This is the class for main activity view.
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.navigation) NavigationView mNavigationView;

    private View headerView;
    private ActionBarDrawerToggle drawerToggle;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        client = new OkHttpClient();

        setupDrawerContent();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new ShotListFragment())
                    .commit();
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent() {
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,          /* DrawerLayout object */
                R.string.open_drawer,         /* "open drawer" description */
                R.string.close_drawer         /* "close drawer" description */
        );
        drawerLayout.addDrawerListener(drawerToggle);

        headerView = mNavigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.nav_user_name)).setText(Dribbble.getCurrentUser().name);

        headerView.findViewById(R.id.nav_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dribbble.logout(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AsyncTaskCompat.executeParallel(new LoadImageTask(Dribbble.getCurrentUser().avatar_url));

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment = new Fragment();
                switch(item.getItemId()) {
                    case R.id.drawer_item_home:
                        fragment = new ShotListFragment();
                        break;
                    case R.id.drawer_item_like:
                        fragment = new ShotListFragment();
                        break;
                    case R.id.drawer_item_buckets:
                        fragment = BucketListFragment.newInstance(false);
                        break;
                    default:
                        break;
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .commit();
                item.setChecked(true);
                setTitle(item.getTitle());
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private class LoadImageTask extends AsyncTask<Void, Void, byte[]> {

        String imageUrl;

        LoadImageTask(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        protected byte[] doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url(imageUrl)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().bytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            ImageView imageView = (ImageView) headerView.findViewById(R.id.nav_user_pic);
            imageView.setVisibility(View.VISIBLE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
        }
    }
}
