package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        Button apiSend = findViewById(R.id.button5);
        String userText = getIntent().getStringExtra("username");
        //ボタンを押下したときの処理を記述
        apiSend.setOnClickListener(v -> {
            //APIに飛ばすデータを作成
            String q1Text = ((EditText) findViewById(R.id.editTextTextPersonName7)).getText().toString();
            String q2Text = ((EditText) findViewById(R.id.editTextTextPersonName5)).getText().toString();
            String q3Text = ((EditText) findViewById(R.id.editTextTextPersonName6)).getText().toString();
            String q4Text = ((EditText) findViewById(R.id.editTextTextPersonName8)).getText().toString();
            String q5Text = ((EditText) findViewById(R.id.editTextTextPersonName9)).getText().toString();
            String q6Text = ((EditText) findViewById(R.id.editTextTextPersonName10)).getText().toString();
            String ageText = ((EditText) findViewById(R.id.editTextTextPersonName13)).getText().toString();
            RadioGroup radioGroup = findViewById(R.id.radioGroup1);
            if (q1Text.equals("")
                    || q2Text.equals("")
                    || q3Text.equals("")
                    || q4Text.equals("")
                    || q5Text.equals("")
                    || q6Text.equals("")
                    || radioGroup.getCheckedRadioButtonId() == -1){
                new AlertDialog.Builder(MainActivity4.this)
                        .setTitle("送信失敗")
                        .setMessage("内容を入力してください。")
                        .setPositiveButton("OK", (dialog, which) -> {})
                        .show();
                return;
            }
            String sexText = radioGroup.getCheckedRadioButtonId() == R.id.male ? "男" : "女";
            new Thread(() -> {
               HttpURLConnection conn = null;
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
                   byte[] myData = clientKey.toString().getBytes();
                   URL url = new URL("http://10.0.2.2:9080/test1/UserQuestionnaire");
                   conn = (HttpURLConnection) url.openConnection();
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
                   int code = conn.getResponseCode();
                   InputStream stream = code == 201 ? conn.getInputStream() : conn.getErrorStream();
                   StringBuilder sb = new StringBuilder();
                   String line;
                   BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                   while((line = br.readLine()) != null){
                       sb.append(line);
                   }
                   JSONObject jsonObject = new JSONObject(sb.toString());
                   MainActivity4.this.runOnUiThread(() -> {
                       try {
                           new AlertDialog.Builder(MainActivity4.this)
                                   .setTitle(code == 201 ? "送信成功" : "送信失敗")
                                   .setMessage(jsonObject.getString("message"))
                                   .setPositiveButton("OK", (dialog, which) -> {})
                                   .show();
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   });
               }catch(Exception e){
                   e.printStackTrace();
                   Log.i("失敗", "");
               }finally {
                   if (conn != null){
                       conn.disconnect();
                   }
               }
            }).start();
        });
    }
}