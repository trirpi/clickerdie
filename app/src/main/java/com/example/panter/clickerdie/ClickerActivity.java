package com.example.panter.clickerdie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClickerActivity extends AppCompatActivity {

    private String server_ip;
    private int server_port;

    private Socket socket;

    private static final String TAG = "SocketCommunication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicker);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        server_ip = intent.getStringExtra("ip");
        server_port = intent.getIntExtra("port", 0);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        String textview_text = "Connected to " + server_ip + ":" + Integer.toString(server_port);
        textView.setText(textview_text);

        new Thread(new StartClientThread()).start();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean screen_awake = settings.getBoolean("setting_screen_awake", false);

        if (screen_awake) {
            Log.i(TAG, "Keeping screen on.");
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    protected void onStop() {
        closeConnection();
        super.onStop();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    this.sendNext(null);
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    this.sendPrevious(null);
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    public void sendNext(View view) {
        sendMessage("next");
    }

    public void sendPrevious(View view) {
        sendMessage("previous");
    }

    public void sendMessage(String message) {
        new Thread(new SendServerThread(message)).start();
    }

    public void closeConnection() {
        new Thread(new EndConnectionThread()).start();
    }

    class SendServerThread implements Runnable {
        private String message;

        private SendServerThread(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                // Send message to server
                BufferedWriter output_writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                output_writer.write(this.message);
                output_writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class StartClientThread implements Runnable {

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(server_ip);

                socket = new Socket(serverAddr, server_port);
                Log.i(TAG, "Connecting to server " + server_ip + ":" + Integer.toString(server_port));
                socket.connect(new InetSocketAddress(server_ip, server_port), 5000);

            } catch (UnknownHostException e) {
                closeConnection();
            } catch (IOException e) {
                Log.e(TAG, "Was not able to connect to server.");
            }
        }
    }

    class EndConnectionThread implements Runnable {

        @Override
        public void run() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Was not able to disconnect from server.");
            }
        }
    }
}
