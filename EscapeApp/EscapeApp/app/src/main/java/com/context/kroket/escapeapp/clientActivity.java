package com.context.kroket.escapeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class clientActivity extends AppCompatActivity {

    private TCPClient tcpClient;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        list = new ArrayList<String>();

        new connectTask().execute("");

        list.add("Hoi Jochem! :)");

        if (tcpClient != null) {
            tcpClient.sendMessage("Hoi Jochem2! :)");
        }
    }

    public class connectTask extends AsyncTask<String, String, TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            tcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                public void messageReceived(String mes) {
                    publishProgress(mes);
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);       //onProgressUpdate

            list.add(values[0]);
        }
    }
}
