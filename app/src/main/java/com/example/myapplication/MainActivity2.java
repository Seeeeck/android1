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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public final class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.editTextTextPersonName2)).getText().toString();
                String email = ((EditText) findViewById(R.id.editTextTextPersonName3)).getText().toString();
                String Password = ((EditText) findViewById(R.id.editTextTextPersonName4)).getText().toString();

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        try{
                            //final String sendData1 = "name=" + name + "&" + "phone_number=" + Phone_Number + "&" + "password="+Password;
                            //データを送信するためにはbyte配列に変換する必要がある
                            //byte[] sendData1_1 = sendData1.getBytes(StandardCharsets.UTF_8);
                            //int postDataLength = sendData1_1.length;
                            //接続先のURLの設定及びコネクションの取得
                            URL url = new URL("http://10.0.2.2:3000/auth/register");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            //接続するための設定
                            connection.setRequestProperty("User-Agent", "Android");
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            connection.setRequestProperty("Accept", Locale.getDefault().toString());
                            connection.setRequestProperty("charset", "utf-8");
                            //connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));

                            //APIからの戻り値と送信するデータの設定を許可する
                            connection.setDoInput(true);
                            connection.setDoOutput(true);
                            connection.setInstanceFollowRedirects(false);

                            //送信するデータの設定
                            //connection.getOutputStream().write(sendData1_1);
                            //connection.getOutputStream().flush();
                            //connection.getOutputStream().close();
                            connection.connect();
                            OutputStream outputStream = connection.getOutputStream();
                            HashMap keyValues = new HashMap<>();
                            keyValues.put("name", name);
                            keyValues.put("email", email);
                            keyValues.put("password", Password);
                            System.out.println(keyValues);
                            if(keyValues.size() > 0){
                                Uri.Builder builder = new Uri.Builder();
                                Set Keys = keyValues.keySet();
                                for(Object key : Keys){
                                    builder.appendQueryParameter((String) key, (String) keyValues.get(key));
                                }
                                String join = builder.build().getEncodedQuery();
                                PrintStream ps = new PrintStream(outputStream);
                                ps.print(join);
                                ps.close();
                            }
                            outputStream.close();

                            int statusCode = connection.getResponseCode();

                            String responseData = "";
                            InputStream stream = connection.getInputStream();
                            StringBuffer sb = new StringBuffer();
                            String line = "";
                            BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                            while((line = br.readLine()) != null){
                                sb.append(line);
                            }
                            try{
                                stream.close();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            responseData = sb.toString();
                            System.out.println(responseData);
                            JSONObject jsData = new JSONObject(responseData);
                            System.out.println(jsData.getString("username"));
                            if(jsData.getString("username") != null){
                                Intent intent = new Intent(MainActivity2.this, MainActivity6.class);
                                intent.putExtra("name",jsData.getString("username"));
                                startActivity(intent);
                            }else{
                                System.out.println("失敗");
                            }
                            System.out.println(responseData);
                            //接続
                            connection.connect();
                            connection.getResponseCode();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute(); //executeメソッドでdoInBackgroundメソッドを別スレッドで実行
            }
        });
    }
}