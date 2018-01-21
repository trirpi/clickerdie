package com.example.panter.clickerdie;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE_ID = "com.example.panter.MESSAGE";
    private static final int PERMISSION_REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar1: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.toolbar2: {
                break;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        EditText editText_ip = (EditText) findViewById(R.id.editTextIP);
        EditText editText_port = (EditText) findViewById(R.id.EditTextPort);

        String ip = editText_ip.getText().toString();
        String port = editText_port.getText().toString();

        if (!(TextUtils.isEmpty(ip) || TextUtils.isEmpty((port)))) {
            Intent intent = new Intent(this, ClickerActivity.class);
            intent.putExtra("ip", ip);
            intent.putExtra("port", Integer.parseInt(port));
            startActivity(intent);
        } else {
            if (TextUtils.isEmpty(ip)) {
                editText_ip.setError("Fill in an ip.");
            }
            if (TextUtils.isEmpty(port)) {
                editText_port.setError("Fill in a port number.");
            }
        }
    }

    public void LaunchQRScanActivity(View view) {
        // Check if user has given permission to use camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MainActivity.this, QRScanActivity.class);
            startActivity(intent);
        } else {
            // Ask permission to use camera
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        }

    }
}
