package com.example.covid19_news;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;

import javax.net.ssl.HttpsURLConnection;

public class EventList {
    private final int type;
    private LinkedList<String> getForward;
    private LinkedList<String> getUpdate;
    private String lastNews;
    static int read[]={0, 0, 0, 0, 0};
    EventList(int t){
        type=t;
        if(type==Global.newsType){
            getForward=Global.ForwardNews;
            getUpdate=Global.UpdateNews;
        }
        else if(type==Global.paperType){
            getForward=Global.ForwardPaper;
            getUpdate=Global.UpdatePaper;
        }
    }
    public void makeList(){
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                for(int ind=0;ind<10;ind++) {
                    StringBuilder u = new StringBuilder(Global.eventListUrl);
                    //https://covid-dashboard.aminer.cn/api/events/list?type=paper&page=1&size=5
                    u.append("list?type=");
                    u.append(Global.everyTypes.get(type));
                    u.append("&page=" + read[type] + 1);
                    u.append("&size=20");
                    try {
                        URL url = new URL(u.toString());
                        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        connection.setRequestMethod("GET");
                        connection.setUseCaches(true);
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("Charset", "UTF-8");
                        connection.addRequestProperty("Connection", "Keep-Alive");
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        JSONObject jobj = new JSONObject(br.readLine());
                        JSONArray jarry = new JSONArray(jobj.getString("data"));
                        for (int i = 0; i < jarry.length(); i++) {
                            Global.db.insertNews(jarry.getJSONObject(i));
                            getForward.addLast(jarry.getJSONObject(i).getString("_id"));
                        }
                        if(lastNews==null){
                            lastNews=jarry.getJSONObject(0).getString("_id");
                        }
                        read[type]++;
                        wait(20*1000);
                    } catch (IOException | JSONException |InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public void makeUpdate(){
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                while(true) {
                    try {
                        wait(60*1000);
                        for (int updateOn = 1; updateOn != 0; updateOn++) {
                            StringBuilder u = new StringBuilder(Global.eventListUrl);
                            //https://covid-dashboard.aminer.cn/api/events/list?type=paper&page=1&size=5
                            u.append("list?type=");
                            u.append(Global.everyTypes.get(type));
                            u.append("&page=" + updateOn);
                            u.append("&size=20");

                            URL url = new URL(u.toString());
                            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                            connection.setConnectTimeout(5000);
                            connection.setReadTimeout(5000);
                            connection.setRequestMethod("GET");
                            connection.setUseCaches(true);
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setRequestProperty("Charset", "UTF-8");
                            connection.addRequestProperty("Connection", "Keep-Alive");
                            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            JSONObject jobj = new JSONObject(br.readLine());
                            JSONArray jarry = new JSONArray(jobj.getString("data"));
                            LinkedList<String> tmp = new LinkedList<>();
                            for (int i = 0; i < jarry.length(); i++) {
                                String k = jarry.getJSONObject(i).getString("_id");
                                if (Global.db.findNews(k)) {
                                    updateOn = 0;
                                    break;
                                } else {
                                    tmp.addLast(k);
                                    Global.db.insertNews(jarry.getJSONObject(i));
                                }
                            }
                            if(!tmp.isEmpty()) {
                                Collections.reverse(tmp);
                                for(int i=0;i<tmp.size();i++){
                                    getUpdate.addLast(tmp.get(i));
                                }
                            }
                            wait(500);
                        }
                    } catch (IOException | JSONException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
