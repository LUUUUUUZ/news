package com.example.covid19_news;

import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class EntitySearcher {
    static boolean searchFinished;
    static Entity searchResult;

    public static void init(){
        searchResult=new Entity();
    }
    public static void searchFor(final String name){
        searchFinished=false;
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    URL u = new URL(Global.EntityQueueUrl+name);
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
                    JSONArray jobj2=jobj.getJSONArray("data");
                    jobj=jobj2.getJSONObject(0);
                    if(jobj.getString("label").equals(name)){
                        searchResult.isEmpty=false;
                        searchResult.name=name;
                        JSONObject des=jobj.getJSONObject("abstractInfo");
                        if(!des.getString("enwiki").equals("")){
                            searchResult.description=des.getString("enwiki");
                        }
                        else if(!des.getString("baidu").equals("")){
                            searchResult.description=des.getString("baidu");
                        }
                        else if(!des.getString("zhwiki").equals("")){
                            searchResult.description=des.getString("zhwiki");
                        }
                        else{
                            searchResult.description="This Entity Has No Description. ";
                        }
                        JSONObject cov=des.getJSONObject("COVID");
                        JSONObject pro=cov.getJSONObject("properties");
                        searchResult.properties.clear();
                        for(Iterator<String> iter=pro.keys();iter.hasNext();){
                            String k=iter.next();
                            searchResult.properties.put(k,pro.getString(k));
                        }
                        JSONArray rel=cov.getJSONArray("relations");
                        searchResult.relations.clear();
                        for(int j=0;j<rel.length();j++){
                            searchResult.relations.add(new Relation(rel.getJSONObject(j)));
                        }
                        String imgurl=jobj.getString("img");
                        if(imgurl.equals("null")){
                            searchResult.hasImage=false;
                        }
                        else{
                            searchResult.hasImage=true;
                            URL url1 = new URL(imgurl);
                            HttpsURLConnection uc = (HttpsURLConnection) url1.openConnection();
                            InputStream inputStream = uc.getInputStream();
                            searchResult.image=BitmapFactory.decodeStream(inputStream);
                            inputStream.close();
                        }
                    }
                    else{
                        searchResult.isEmpty=true;
                    }
                    searchFinished=true;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
