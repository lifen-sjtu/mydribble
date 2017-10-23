package com.example.jlwang.mydribble.dribbble;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jlwang.mydribble.dribbble.auth.Auth;
import com.example.jlwang.mydribble.model.User;
import com.example.jlwang.mydribble.utils.ModelUtils;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jlwang on 10/22/17.
 */

public class Dribbble  extends Application{
    public static final String API_URL = "https://api.dribbble.com/v1/";
    public static final String GET_USER_URL = API_URL + "user";
    public static final String GET_SHOT_LIST_URL = API_URL + "shots";

    public static final String AUTH_HEADER = "Authorization";
    public static final String PREF_AUTH = "auth";

    private static final TypeToken<User> USER_TYPE = new TypeToken<User>(){};
    private static final String KEY_USER = "use";

    private static OkHttpClient client = new OkHttpClient();
    public static String accessToken;
    private static User user;

    public static boolean isLoggedIn() {
        return accessToken != null;
    }

    private static Request.Builder authRequestBuilder(String url) {
        return new Request.Builder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .url(url);
    }

    private static Response makeRequest(Request request) throws IOException {
        Response response = client.newCall(request).execute();
        return response;
    }

    public static Response makeGetRequest(String url) throws IOException {
        Request request = authRequestBuilder(url).build();
        return makeRequest(request);
    }

    public static <T> T parseResponse(Response response,
                                       TypeToken<T> typeToken) throws IOException, JsonSyntaxException {
        String responseString = response.body().string();
        return ModelUtils.toObject(responseString, typeToken);
    }

    public static void init(@NonNull Context context) {
        accessToken = loadAccessToken(context);
        if (accessToken != null) {
            user = loadUser(context);
        }
    }
    public static void login(@NonNull Context context,
                             @NonNull String accessToken) throws IOException, JsonSyntaxException {
        Dribbble.accessToken = accessToken;
        storeAccessToken(context, accessToken);

        Dribbble.user = getUser();
        storeUser(context, user);
    }

    public static User getUser() throws IOException {
        return parseResponse(makeGetRequest(GET_USER_URL),USER_TYPE);
    }

    public static void logout(@NonNull Context context) {
        storeAccessToken(context, null);
        storeUser(context, null);

        accessToken = null;
        user = null;
    }

    public static User getCurrentUser() {
        return user;
    }


    public static String loadAccessToken(@Nullable Context context) {
        // get your access token from SharedPrefs
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(PREF_AUTH, Context.MODE_PRIVATE);
        return sp.getString(Auth.KEY_ACCESS_TOKEN,null);
    }

    public static void storeAccessToken(@Nullable Context context, String token) {
        accessToken = token;
        // save your access token in the SharedPrefs

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(PREF_AUTH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Auth.KEY_ACCESS_TOKEN,token);
    }

    public static void storeUser(@NonNull Context context, @Nullable User user) {
        ModelUtils.save(context, KEY_USER, user);
    }

    public static User loadUser(@NonNull Context context) {
        return ModelUtils.read(context, KEY_USER, new TypeToken<User>(){});
    }
}
