package com.example.scoping_project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.icu.util.LocaleData;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Dashboard extends AppCompatActivity {

    private RecyclerView scoreRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Score> scoresArrayList;
    ScoreAdapter scoreAdapter;
    String token="abc";
    OkHttpClient client;
    Button refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        client  = new OkHttpClient();
        scoresArrayList = new ArrayList<>();

        //token bhejna padega intent see JUDGE WALA
        if(getIntent().getExtras()!=null)
        {
            token = getIntent().getExtras().getString("TOKEN");
        }

        scoreRecyclerView = findViewById(R.id.scoreRecyclerView);

        refresh = findViewById(R.id.buttonRefresh);
        scoreRecyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(Dashboard.this);
        scoreRecyclerView.setLayoutManager(layoutManager);

//        scoresArrayList.clear();

        if(!token.equals("abc"))
        {
            try {
                getUserInfo(token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        scoreAdapter = new ScoreAdapter(Dashboard.this,scoresArrayList);
//        scoreRecyclerView.setAdapter(scoreAdapter);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!token.equals("abc"))
                {
                    try {
                        getUserInfo(token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }




    private void getUserInfo(String token) throws JSONException {

        String url = "http://ec2-3-94-187-73.compute-1.amazonaws.com:5000/support/getTeam";

//        Log.d("demooo", "6" + url);

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

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                scoresArrayList.clear();

                try {
                    String json = response.body().string();

                    Log.d("lol", "7" + json);
                    JSONArray arr = new JSONArray(json);

//                    final JSONObject root = new JSONObject(json);
//                    JSONArray arr = root.getJSONArray("");

                    for(int i =0 ; i < arr.length(); i++)
                    {
                        JSONObject jsonObject = arr.getJSONObject(i);
                        Score sco = new Score(jsonObject.getString("sum"), jsonObject.getString("teamName"));
                        scoresArrayList.add(sco);
                    }




                    Collections.sort(scoresArrayList, new Comparator<Score>(){

                        @Override
                        public int compare(Score t1, Score t2) {
                            return t2.score.compareTo(t1.score);
                        }
                    });

                    for(int i=0;i<scoresArrayList.size();i++)
                    {
                        Log.d("lol", scoresArrayList.get(i).toString());

                    }

//                    scoreRecyclerView.notify();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scoreAdapter = new ScoreAdapter(Dashboard.this,scoresArrayList);

                            scoreRecyclerView.setAdapter(scoreAdapter);

                        }
                    });




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }




    }
