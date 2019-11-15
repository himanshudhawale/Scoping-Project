package com.example.scoping_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import com.google.zxing.Result;

public class TeamQRCodeActivity  extends  Activity implements ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    String token,userId;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

        if(getIntent().getExtras()!=null)
        {
            token= getIntent().getExtras().getString("TOKEN");
            userId = getIntent().getExtras().getString("MY_USERID");

        }
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
        final String TeamId = rawResult.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mScannerView.resumeCameraPreview(TeamQRCodeActivity.this);
                if(TeamId!=null){
                    Intent goToQuestions = new Intent(TeamQRCodeActivity.this, SurveyActivity.class);
                    goToQuestions.putExtra("TEAM_ID",TeamId);
                    goToQuestions.putExtra("TOKEN",token);
                    goToQuestions.putExtra("USER_ID",userId);
                    startActivity(goToQuestions);
                }



            }
        });

        builder.setMessage("QR Code scanned successfully.");
        AlertDialog alert1 = builder.create();
        alert1.show();
    }
    }

