package com.example.jlwang.mydribble.dribbble.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.view.LogInActivity;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jlwang on 10/22/17.
 */

public class AuthActivity extends AppCompatActivity {
    public final static String RED_URL="google.com";
    public final static String KEY_AUTH_CODE = "Authorize code";

    @BindView(R.id.webview) WebView webView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.auth_activity_title));

        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("https://dribbble.com/oauth/authorize?client_id=a20d39c0c7d70febd60ac1c560f4ff1a7e6b4b9bdf3b7a8a8844db2aad204be6");
        webView.loadUrl(Auth.AUTHORIZE_URL + "?" + Auth.KEY_CLIENT_ID + "=" + Auth.CLIENT_ID_VALUE);

        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if(uri.getHost().toString().equals(RED_URL)) {
                    Intent intent = new Intent();
                    intent.putExtra(KEY_AUTH_CODE,uri.getQueryParameter("code"));
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
                return false;

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
