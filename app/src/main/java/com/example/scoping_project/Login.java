package com.example.scoping_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Login extends AppCompatActivity {

    private Button scanBarcodeButton;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private Button  logIn;
    private EditText userName, password;
    OkHttpClient client;
    static String TOKEN=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        client =  new OkHttpClient();

        userName=findViewById(R.id.emailEditText);
        password=findViewById(R.id.passwordEditText);
        logIn=findViewById(R.id.signInBtn);
        scanBarcodeButton = findViewById(R.id.scanQRCodeBtn);

        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSimpleActivity(view);
            }
        });



        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Log.d("pop", "hello");
                    Log.d("pop", userName.getText().toString());
                    Log.d("pop", password.getText().toString());

                    login(userName.getText().toString(),password.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void launchSimpleActivity(View v) {
        launchActivity(JudgeQRScanActivity.class);
    }




    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }





    private void login(final String email, String password) throws JSONException {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email",email);
        jsonObject.put("password",password);

        String jsonString=jsonObject.toString();

        RequestBody requestBody = RequestBody.create(JSON, jsonString);


        Request request = new Request.Builder()
                .url("http://ec2-3-94-187-73.compute-1.amazonaws.com:5000/users/authenticate")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .post(requestBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                try {

                    String json = response.body().string();
                    JSONObject root = new JSONObject(json);

                    Log.d("pop", "lol");


                    String status= root.getString("email");
                    if(status.equals(email)) {

                        TOKEN = root.getString("token");
                        String UserID  = root.getString("_id");


                        Intent intent = new Intent(Login.this, GoToTeamScanActivity.class);
                        intent.putExtra("MY_USERID", UserID);
                        intent.putExtra("MY_TOKEN", TOKEN);
                        startActivity(intent);
//                        finish();

                    }

                    else
                    {
                        Looper.prepare();
                        Toast.makeText(Login.this, "Login is not successful.", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



        });
    }




}
