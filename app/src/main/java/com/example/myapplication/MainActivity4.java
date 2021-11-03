package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Button apiSend = (Button) findViewById(R.id.button5);
        String userText = getIntent().getStringExtra("username");
        AlertDialog.Builder successAlert = new AlertDialog.Builder(MainActivity4.this)
                .setTitle("送信成功")
                .setPositiveButton("OK", (dialog, which) -> {
                });
        AlertDialog.Builder failAlert = new AlertDialog.Builder(MainActivity4.this)
                .setTitle("送信失敗")
                .setPositiveButton("OK", (dialog, which) -> {
                });
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
                String ageText = ((EditText) findViewById(R.id.editTextTextPersonName13)).getText().toString();
                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
                if (q1Text.equals("")
                        || q2Text.equals("")
                        || q3Text.equals("")
                        || q4Text.equals("")
                        || q5Text.equals("")
                        || q6Text.equals("")
                        || radioGroup.getCheckedRadioButtonId() == -1){
                    new AlertDialog.Builder(MainActivity4.this)
                            .setTitle("送信失敗")
                            .setPositiveButton("OK", (dialog, which) -> {})
                            .show();
                    return;
                }
                String sexText = radioGroup.getCheckedRadioButtonId() == R.id.male ? "男" : "女";
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
                       InputStream stream = conn.getInputStream();
                       StringBuilder sb = new StringBuilder();
                       String line = "";
                       BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                       while((line = br.readLine()) != null){
                           sb.append(line);
                       }
                       JSONObject jsonObject = new JSONObject(sb.toString());
                       int code = jsonObject.getInt("code");
                       if (code == 201){
                           MainActivity4.this.runOnUiThread(successAlert::show);
                       }else {
                           MainActivity4.this.runOnUiThread(() -> {
                               try {
                                   failAlert.setMessage(jsonObject.getString("message")).show();
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           });
                       }
                   }catch(Exception e){
                       e.printStackTrace();
                       Log.i("失敗", "");
                   }
                }).start();
            }
        });
    }
}