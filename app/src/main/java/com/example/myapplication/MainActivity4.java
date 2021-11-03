package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Button apiSend = (Button) findViewById(R.id.button5);
        //ボタンを押下したときの処理を記述
        apiSend.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //APIに飛ばすデータを作成
                String q1Text = ((EditText) findViewById(R.id.editTextTextPersonName7)).getText().toString();
                String q2Text = ((EditText) findViewById(R.id.editTextTextPersonName5)).getText().toString();
                String q3Text = ((EditText) findViewById(R.id.editTextTextPersonName6)).getText().toString();
                String q4Text = ((EditText) findViewById(R.id.editTextTextPersonName8)).getText().toString();
                String q5Text = ((EditText) findViewById(R.id.editTextTextPersonName9)).getText().toString();
                String q6Text = ((EditText) findViewById(R.id.editTextTextPersonName10)).getText().toString();
                String ageText = ((EditText) findViewById(R.id.editTextTextPersonName12)).getText().toString();
                String sexText = ((EditText) findViewById(R.id.editTextTextPersonName13)).getText().toString();
                String userText = ((EditText) findViewById(R.id.editTextTextPersonName11)).getText().toString();
                new Thread(() -> {
                   URL url = null;
                   try{
                       JSONObject clientKey = new JSONObject();

                       clientKey.put("question1", q1Text);
                       clientKey.put("question2", q2Text);
                       clientKey.put("question3", q3Text);
                       clientKey.put("question4", q4Text);
                       clientKey.put("question5", q5Text);
                       clientKey.put("question6", q6Text);
                       clientKey.put("age", ageText);
                       clientKey.put("sex", sexText);
                       clientKey.put("username", userText);

                       String content = String.valueOf(clientKey);
                       byte[] myData = clientKey.toString().getBytes();
                       Log.i("データ", content);
                       System.out.println(clientKey);
                       url = new URL("http://10.0.2.2:9080/test1/UserQuestionnaire");
                       HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                       conn.setConnectTimeout(7000);
                       conn.setRequestMethod("POST");
                       conn.setRequestProperty("Content-Type", "application/json");
                       conn.setRequestProperty("Accept", "application/json");
                       conn.setRequestProperty("Content-Length", String.valueOf(myData.length));
                       conn.setRequestProperty("Connection", "keep-alive");
                       conn.setDoOutput(true);
                       conn.setDoInput(true);
                       conn.setUseCaches(false);
                       conn.connect();
                       conn.getOutputStream().write(myData);
                       conn.getOutputStream().close();
                       String code = String.valueOf(conn.getResponseCode());
                       Log.i("code=", code);
                       String rs = conn.getResponseMessage();
                       Log.i("result=", rs);
                       int code0 = conn.getResponseCode();
                       if(code0 == 201){
                           Message message = new Message();
                           message.obj = 0;
                       };
                   }catch(Exception e){
                       e.printStackTrace();
                       Log.i("失敗", "");
                   }
                }).start();
            }
        });
    }
}