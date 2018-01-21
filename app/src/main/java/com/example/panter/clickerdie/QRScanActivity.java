package com.example.panter.clickerdie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "QRScan";
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        Log.i(TAG, "Started Activity");
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
    public void handleResult(Result rawResult) {

        String message = rawResult.getText();
        Log.v(TAG, message); // result

        if (message.contains(":")) {
            String[] parts = message.split(":");

            String ip = parts[0];
            int port = Integer.parseInt(parts[1]);

            Intent intent = new Intent(this, ClickerActivity.class);
            intent.putExtra("ip", ip);
            intent.putExtra("port", port);
            startActivity(intent);
        } else {
            finish();
        }



    }
}
