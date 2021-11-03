package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class MainActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        final Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String ConfirmText = ((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString();

                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            URL url = new URL("http://10.0.2.2:3000/auth/activate");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            connection.setRequestProperty("User-Agent", "Android");
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            connection.setRequestProperty("Accept", Locale.getDefault().toString());
                            connection.setRequestProperty("charset", "utf-8");

                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            connection.setInstanceFollowRedirects(false);

                            connection.connect();
                            OutputStream outputStream = connection.getOutputStream();
                            HashMap keyValues = new HashMap<>();

                            keyValues.put("name", "Martin1");
                            keyValues.put("key", ConfirmText);
                            System.out.println(keyValues);
                            if (keyValues.size() > 0) {
                                Uri.Builder builder = new Uri.Builder();
                                Set Keys = keyValues.keySet();
                                for (Object key : Keys) {
                                    builder.appendQueryParameter((String) key, (String) keyValues.get(key));
                                }
                                String join = builder.build().getEncodedQuery();
                                PrintStream ps = new PrintStream(outputStream);
                                ps.print(join);
                                ps.close();
                            }
                            outputStream.close();

                            int statusCode = connection.getResponseCode();

                            String responseDeta = "";
                            InputStream stream = connection.getInputStream();
                            StringBuffer sb = new StringBuffer();
                            String line = "";
                            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            try {
                                stream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            responseDeta = sb.toString();
                            if(responseDeta == "SUCCESS"){
                                Intent intent = new Intent(MainActivity3.this, MainActivity4.class);
                                startActivity(intent);
                            }
                            System.out.println(responseDeta);
                            //接続
                            connection.connect();
                            connection.getResponseCode();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    }.execute();
                }
                });
                    }
        }