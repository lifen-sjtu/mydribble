package com.example.jlwang.mydribble.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jlwang.mydribble.R;
import com.example.jlwang.mydribble.dribbble.Dribbble;
import com.example.jlwang.mydribble.dribbble.auth.Auth;
import com.example.jlwang.mydribble.dribbble.auth.AuthActivity;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jlwang on 10/22/17.
 */

public class LogInActivity extends AppCompatActivity {
    public final static int REQ_CODE_AUTH = 100;
    public String token;
    @BindView(R.id.login_btn) TextView login_btn;

    private String authCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Dribbble.init(this);

        if(Dribbble.isLoggedIn()) {
            Intent intent = new Intent(LogInActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            login_btn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(LogInActivity.this,AuthActivity.class);
//                    startActivityForResult(intent, REQ_CODE_AUTH);
                    Auth.openAuthActivity(LogInActivity.this);

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_AUTH && resultCode == RESULT_OK) {
            authCode = data.getStringExtra(AuthActivity.KEY_AUTH_CODE);
            Log.i("fen auth code", authCode);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add(Auth.KEY_CODE,authCode)
                            .add(Auth.KEY_CLIENT_ID,Auth.CLIENT_ID_VALUE)
                            .add(Auth.KEY_CLIENT_SECRET,Auth.CLIENT_SECRET_VALUE)
                            .build();
                    Request request = new Request.Builder().url(Auth.TOKEN_URL).post(formBody).build();
                    try {
                        Response response = client.newCall(request).execute();
                        String jsonData = response.body().string();
                        JSONObject Jobject = new JSONObject(jsonData);
                        String token = Jobject.getString(Auth.KEY_ACCESS_TOKEN);
                        Log.i("fen token",token);

                        Dribbble.login(LogInActivity.this,token);

                        Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
