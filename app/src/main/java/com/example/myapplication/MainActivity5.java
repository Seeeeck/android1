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

public class MainActivity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        //MainActivity2と同じロジック（リクエストコードは全て同じで、リクエストURI及びテキストボックスidを変更してください。）
        //AWS Cognitoへ登録したユーザーでログインを実行できるようにする。
        //ユーザーログインリクエストURI：http://10.0.2.2:3000/auth/login

        /**
         * 処理概要
         * １：MainActivityのログオンボタンより当画面へ遷移
         * ２：ユーザー名とパスワードをテキストボックスへ入力する
         * ３：テキストボックス（ユーザー名とパスワードのテキストボックス）から入力された値を取得し、リクエストパラメータへセット
         * ４：リクエストを行い、AWS Cognitoからのログイン成功レスポンスでアンケート記入画面（MainActivity4）へ画面を遷移させる。
         */

        final Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String accountId = ((EditText) findViewById(R.id.accountId)).getText().toString();
                String accountPassword = ((EditText) findViewById(R.id.accountPassword2)).getText().toString();

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        try{
                            //final String sendData1 = "name=" + name + "&" + "phone_number=" + Phone_Number + "&" + "password="+Password;
                            //データを送信するためにはbyte配列に変換する必要がある
                            //byte[] sendData1_1 = sendData1.getBytes(StandardCharsets.UTF_8);
                            //int postDataLength = sendData1_1.length;
                            //接続先のURLの設定及びコネクションの取得
                            URL url = new URL("http://10.0.2.2:3000/auth/login");
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
                            keyValues.put("name", accountId);
                            keyValues.put("password", accountPassword);
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
                            try{
                                new JSONObject(responseData);
                                System.out.println("失敗");
                            }catch (Exception e){
                                Intent intent = new Intent(MainActivity5.this, MainActivity4.class);
                                intent.putExtra("token",responseData);
                                intent.putExtra("username",accountId);
                                startActivity(intent);
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