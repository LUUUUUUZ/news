package com.java.zhutianyao;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class OneNews{
    static String EventUrl="https://covid-dashboard.aminer.cn/api/event/";
    OneNews(final String id){
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    URL u = new URL(EventUrl+id);
                    HttpsURLConnection connection = (HttpsURLConnection) u.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Charset", "UTF-8");
                    connection.addRequestProperty("Connection", "Keep-Alive");
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JSONObject jobj=new JSONObject(br.readLine());
                    JSONObject jobj2=jobj.getJSONObject("data");
                    Global.db.insertNews(jobj2);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}