package com.java.zhutianyao;

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

class SearchEntityThread extends Thread{
    @Override
    public synchronized void run() {
        try {
            URL u = new URL(EntitySearcher.EntityQueueUrl + EntitySearcher.searchName);
            HttpsURLConnection connection = (HttpsURLConnection) u.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.addRequestProperty("Connection", "Keep-Alive");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JSONObject jobj = new JSONObject(br.readLine());
            JSONArray jobj2 = jobj.getJSONArray("data");
            if(jobj2.length()!=0) {
                jobj = jobj2.getJSONObject(0);
                if (jobj.getString("label").equals(EntitySearcher.searchName)) {
                    EntitySearcher.searchResult.isEmpty = false;
                    EntitySearcher.searchResult.name = EntitySearcher.searchName;
                    JSONObject des = jobj.getJSONObject("abstractInfo");
                    if (!des.getString("enwiki").equals("")) {
                        EntitySearcher.searchResult.description = des.getString("enwiki");
                    } else if (!des.getString("baidu").equals("")) {
                        EntitySearcher.searchResult.description = des.getString("baidu");
                    } else if (!des.getString("zhwiki").equals("")) {
                        EntitySearcher.searchResult.description = des.getString("zhwiki");
                    } else {
                        EntitySearcher.searchResult.description = "";
                    }
                    JSONObject cov = des.getJSONObject("COVID");
                    JSONObject pro = cov.getJSONObject("properties");
                    EntitySearcher.searchResult.properties.clear();
                    for (Iterator<String> iter = pro.keys(); iter.hasNext(); ) {
                        String k = iter.next();
                        EntitySearcher.searchResult.properties.put(k, pro.getString(k));
                    }
                    JSONArray rel = cov.getJSONArray("relations");
                    EntitySearcher.searchResult.relations.clear();
                    for (int j = 0; j < rel.length(); j++) {
                        EntitySearcher.searchResult.relations.add(new Relation(rel.getJSONObject(j)));
                    }
                    String imgurl = jobj.getString("img");
                    if (imgurl.equals("null")) {
                        EntitySearcher.searchResult.hasImage = false;
                    } else {
                        EntitySearcher.searchResult.hasImage = true;
                        URL url1 = new URL(imgurl);
                        HttpsURLConnection uc = (HttpsURLConnection) url1.openConnection();
                        InputStream inputStream = uc.getInputStream();
                        EntitySearcher.searchResult.image = BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                    }
                }
            }
            else {
                EntitySearcher.searchResult.isEmpty = true;
            }
            EntitySearcher.searchFinished = true;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}

public class EntitySearcher {
    final static protected String EntityQueueUrl="https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=";
    static protected boolean searchFinished;
    static protected Entity searchResult;
    static protected String searchName;
    protected EntitySearcher() {
        searchResult=new Entity();
    }
    protected synchronized Entity searchFor(final String name){
        searchFinished = false;
        searchName=name;
        SearchEntityThread st = new SearchEntityThread();
        st.start();
        try {
            st.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return searchResult;
    }
}
