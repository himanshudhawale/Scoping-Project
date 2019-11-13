package com.example.scoping_project;


import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.oob.SignUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GoToTeamScanActivity extends AppCompatActivity {
    private Button teamQRCodeScanButton;
    String token, userID;
    OkHttpClient client;
    TextView judgeNametextView;
   Button goToDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_team_scan);
        teamQRCodeScanButton = findViewById(R.id.scanTeamQRCodeBtn);
        judgeNametextView = findViewById(R.id.judgeNameTextView);
        goToDashboard=findViewById(R.id.dashBoard);
        client = new OkHttpClient();




        if(getIntent().getExtras()!=null)
        {
            userID= getIntent().getExtras().getString("MY_USERID");
            token= getIntent().getExtras().getString("MY_TOKEN");

            Log.d("demooo","4" + userID);
            Log.d("demooo", "5" + token);


                    try {
                        getUserInfo(userID, token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


        }


        teamQRCodeScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTeamScan = new Intent(GoToTeamScanActivity.this, TeamQRCodeActivity.class);
                goToTeamScan.putExtra("TOKEN",token);
                goToTeamScan.putExtra("MY_USERID",userID);

                Log.d("User id to send",userID);
                startActivity(goToTeamScan);
            }
        });

        goToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GoToTeamScanActivity.this,Dashboard.class);
                intent.putExtra("TOKEN", token);
                 startActivity(intent);
            }
        });
    }


    private void getUserInfo(String userIDD, String tokenn ) throws JSONException {

        String url = "http://ec2-3-94-187-73.compute-1.amazonaws.com:5000/users/"+userIDD;

        Log.d("demooo", "6" + url);

        final Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + token)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("fail", e.getMessage());
                Log.d("demooo", "7" + e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                try {
                    String json = response.body().string();

                    Log.d("demooo", "7" + json);

                    final  JSONObject root = new JSONObject(json);
                   final String fname = root.getString("firstName");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            judgeNametextView.setText("Hello " + fname+ ",");

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
}
