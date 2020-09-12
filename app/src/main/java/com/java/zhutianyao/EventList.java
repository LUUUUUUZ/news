package com.java.zhutianyao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import javax.net.ssl.HttpsURLConnection;

public class EventList {
    final static private String EventListUrl="https://covid-dashboard.aminer.cn/api/events/";
    final static private int ListMax=200;
    final static protected HashMap<Integer,String> EveryTypes=new HashMap<Integer, String>(){{
        put(allType, "all");
        put(eventType, "event");
        put(pointsType, "points");
        put(newsType, "news");
        put(paperType, "paper");
    }};
    final static protected int allType=0;
    final static protected int eventType=1;
    final static protected int pointsType=2;
    final static protected int newsType=3;
    final static protected int paperType=4;
    static protected LinkedList<String> ForwardNews;
    static protected LinkedList<String> ForwardPaper;
    static protected LinkedList<String> UpdateNews;
    static protected LinkedList<String> UpdatePaper;

    final private int type;
    private LinkedList<String> getForward;
    private LinkedList<String> getUpdate;
    private EventList(int t){
        type=t;
        if(type==newsType){
            getForward=ForwardNews;
            getUpdate=UpdateNews;
        }
        else if(type==paperType){
            getForward=ForwardPaper;
            getUpdate=UpdatePaper;
        }
    }
    private void makeList(){
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                for(int ind=0;ind<ListMax/20;ind++) {
                    StringBuilder u = new StringBuilder(EventListUrl);
                    //https://covid-dashboard.aminer.cn/api/events/list?type=paper&page=1&size=5
                    u.append("list?type=");
                    u.append(EveryTypes.get(type));
                    u.append("&page=" + ind + 1);
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
                            String tmp=jarry.getJSONObject(i).getString("_id");
                            Global.db.insertNews(jarry.getJSONObject(i));
                            if(!getForward.contains(tmp)){
                                getForward.addLast(tmp);
                            }
                        }
                        wait(20*1000);
                    } catch (IOException | JSONException |InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void makeUpdate(){
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                while(true) {
                    try {
                        wait(60*1000);
                        for (int updateOn = 1; updateOn != 0; updateOn++) {
                            StringBuilder u = new StringBuilder(EventListUrl);
                            //https://covid-dashboard.aminer.cn/api/events/list?type=paper&page=1&size=5
                            u.append("list?type=");
                            u.append(EveryTypes.get(type));
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
    static protected void init(){
        ForwardNews=new LinkedList<>();
        ForwardPaper=new LinkedList<>();
        UpdateNews=new LinkedList<>();
        UpdatePaper=new LinkedList<>();
        EventList downloadNews=new EventList(newsType);
        downloadNews.makeList();
        downloadNews.makeUpdate();
        EventList downloadPaper=new EventList(paperType);
        downloadPaper.makeList();
        downloadNews.makeUpdate();
    }
}
