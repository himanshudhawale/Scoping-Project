package com.example.scoping_project;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class JudgeQRScanActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    String UserID;
    OkHttpClient client;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view
        client = new OkHttpClient();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(final Result rawResult) {
        // Do something with the result here
        final String Token = rawResult.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mScannerView.resumeCameraPreview(JudgeQRScanActivity.this);

                //started here
                //started here
                //started here
                //started here
                //started here
                //started here
                //started here
                //started here
                Log.d("demoo", "Hellooo");

                if(Token!=null) {
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("token", Token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("demoo", "Hello111oo");


                    String jsonString = jsonObject.toString();
                    RequestBody rbody = RequestBody.create(JSON, jsonString);


                    Request request = new Request.Builder()
                            .url("http://ec2-3-94-187-73.compute-1.amazonaws.com:5000/users/getInfo")
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/json")
                            .post(rbody)
                            .build();


                    Log.d("demoo", request.toString());
                    Log.d("demoo", "Helloo22o");


                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("demoo", "Hel3333looo");

                        }

                        @Override
                        public void onResponse(Call call, okhttp3.Response response) throws IOException {
                            try {
                                String json = response.body().string();
                                final JSONObject root = new JSONObject(json);
                                Log.d("demoo", json);

                                UserID = root.getString("_id");

                                Log.d("demoo", "Helloo44422o" + UserID);

                                Intent intent = new Intent(JudgeQRScanActivity.this, GoToTeamScanActivity.class);
                                intent.putExtra("MY_USERID", UserID);
                                intent.putExtra("MY_TOKEN", Token);
                                Log.d("demooo", "2" + UserID);
                                Log.d("demooo", "3" + Token);

                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });




        builder.setMessage("QR code scanned successfully!");
        AlertDialog alert1 = builder.create();
        alert1.show();



    }
}