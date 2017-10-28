package com.example.jlwang.mydribble.dribbble.auth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by jlwang on 10/22/17.
 */

public class Auth {

    public static final int REQ_CODE = 100;
    public final static String KEY_CLIENT_ID = "client_id";
    public final static String KEY_CLIENT_SECRET = "client_secret";
    public final static String KEY_CODE = "code";
    public static final String KEY_URL = "url";
    private static final String KEY_REDIRECT_URI = "redirect_uri";
    private static final String KEY_SCOPE = "scope";
    public final static String KEY_ACCESS_TOKEN = "access_token";

    public final static String CLIENT_ID_VALUE = "a20d39c0c7d70febd60ac1c560f4ff1a7e6b4b9bdf3b7a8a8844db2aad204be6";
    public final static String CLIENT_SECRET_VALUE = "ce391a56f98ae8e1cad0b80771d70017a98306e5c8acd38d98dd1419797eaa41";

    public final static String AUTHORIZE_URL = "https://dribbble.com/oauth/authorize";
    public final static String TOKEN_URL = "https://dribbble.com/oauth/token";

    private static final String SCOPE = "public+write";

    public static final String REDIRECT_URI = "https://www.google.com";

    private static String getAuthorizeUrl() {
        String url = Uri.parse(AUTHORIZE_URL)
                .buildUpon()
                .appendQueryParameter(KEY_CLIENT_ID, Auth.CLIENT_ID_VALUE)
                .build()
                .toString();

        // fix encode issue
        url += "&" + KEY_REDIRECT_URI + "=" + REDIRECT_URI;
        url += "&" + KEY_SCOPE + "=" + SCOPE;

        return url;
    }
    public static void openAuthActivity(@NonNull Activity activity) {
        Intent intent = new Intent(activity, AuthActivity.class);
        intent.putExtra(KEY_URL, getAuthorizeUrl());

        activity.startActivityForResult(intent, REQ_CODE);
    }
}
