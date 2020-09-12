package com.java.zhutianyao;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

class GetEventThread extends Thread{
    @Override
    public synchronized void run(){
        int downloadEvents=20;
        //https://covid-dashboard.aminer.cn/api/events/list?type=paper&page=1&size=5
        for (int page=1;page<=downloadEvents/20+1;page++) {
            try {
                String tmp = EventCluster.EventsListUrl + "&page=" + page + "&size=20";
                URL u = new URL(tmp);
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
                if (page == 1) {
                    downloadEvents = jobj.getJSONObject("pagination").getInt("total");
                    EventCluster.clusterNumber=(downloadEvents+50)/100;
                }
                JSONArray jarry = jobj.getJSONArray("data");
                for (int i = 0; i < jarry.length(); i++) {
                    JSONObject jobj2 = jarry.getJSONObject(i);
                    Event e = new Event();
                    e.id = jobj2.getString("_id");
                    e.title = jobj2.getString("title");
                    String k = jobj2.getString("seg_text");
                    String[] segs = k.split(" ");
                    for (String seg : segs) {
                        if(!EventCluster.stopWords.contains(seg)) {
                            e.words++;
                            if (e.seg_text.containsKey(seg)) {
                                e.seg_text.get(seg).addTime();
                            } else {
                                e.seg_text.put(seg, new Value());
                            }
                            if(!EventCluster.allWords.contains(seg)){
                                EventCluster.allWords.add(seg);
                            }
                        }
                    }
                    EventCluster.allEvents.add(e);
                    System.out.println("get Event "+EventCluster.allEvents.size());
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class Value{
    protected Value(){
        times=1;
        tf_idf=0;
    }
    protected int times;
    protected double tf_idf;
    protected void addTime(){
        times++;
    }
}

class Event{
    protected Event(){
        seg_text=new HashMap<>();
        words=0;
        belongTo=null;
    }
    String id;
    String title;
    protected HashMap<String,Value> seg_text;
    protected int words;
    protected EventCluster belongTo;
    protected double weight;
    protected double cosTo(Event e){
        double res=0;
        for(String k:EventCluster.allWords){
            if(seg_text.containsKey(k)&&e.seg_text.containsKey(k)){
                res+=seg_text.get(k).tf_idf*seg_text.get(k).tf_idf;
            }
        }
        res=res/(length()*e.length());
        return res;
    }
    private double length(){
        double res=0;
        for(Value u:seg_text.values()){
            res+=u.tf_idf*u.tf_idf;
        }
        res=Math.sqrt(res);
        return res;
    }
}

public class EventCluster {
    static protected Vector<Event> allEvents;
    static protected Vector<String> stopWords;
    static protected Vector<String> allWords;
    final static protected String EventsListUrl="https://covid-dashboard.aminer.cn/api/events/list?type=event";
    static protected int clusterNumber=3;
    static protected boolean clusteringOver;
    static protected ArrayList<EventCluster> allCluster;

    static protected void init(Context context){
        allEvents=new Vector<>();
        stopWords=new Vector<>();
        clusteringOver=false;
        try {
            InputStream inp=context.getAssets().open("cn_stopwords");
            Scanner cin=new Scanner(inp);
            while(cin.hasNext()){
                String k=cin.next();
                stopWords.add(k);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        allWords=new Vector<>();
        new Thread(new Runnable(){
            @Override
            public void run() {
                Thread getEvents=new GetEventThread();
                System.out.println("getting events...");
                getEvents.start();
                try {
                    getEvents.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                calculateTFIDF();
                System.out.println("making clusters...");
                makeClusters();
            }
        }).start();
    }
    static private void calculateTFIDF(){
        for(Event e:allEvents){
            for (String k:e.seg_text.keySet()){
                double t=(e.seg_text.get(k).times+0.0)/(e.words+0.0);
                double i=Math.log((allEvents.size()+0.0)/eventNumberContain(k));
                e.seg_text.get(k).tf_idf=t*i;
            }
        }
    }
    static private int eventNumberContain(String k){
        int res=0;
        for(int i=0;i<allEvents.size();i++){
            if(allEvents.get(i).seg_text.containsKey(k)){
                res++;
            }
        }
        return res;
    }
    static private void makeClusters(){
        allCluster=new ArrayList<>();
        //initialize all clusters
        for(int i=0;i<clusterNumber;i++) {
            allCluster.add(new EventCluster());
            allCluster.get(i).addEvent(allEvents.get(i));
            allCluster.get(i).center=allEvents.get(i);
            allEvents.get(i).weight=1;
        }
        while (!clusteringOver){
            for(EventCluster ec:allCluster){
                if(ec.contentChanged){
                    ec.remarkCenter();
                }
            }
            for(Event e:allEvents){
                double max=0;
                EventCluster belong=null;
                for(EventCluster ec:allCluster){
                    double m=ec.similarity(e);
                    if(m>max){
                        max=m;
                        belong=ec;
                    }
                }
                if(e.belongTo!=belong) {
                    System.out.println("moving event "+e.id);
                    if (belong == null) {
                        belong = allCluster.get(0);
                    }
                    if (e.belongTo != null) {
                        e.belongTo.removeEvent(e);
                    }
                    belong.addEvent(e);
                }
                e.weight = max;
            }
            boolean tmp=false;
            for(EventCluster ec:allCluster){
                tmp|=ec.contentChanged;
            }
            clusteringOver=!tmp;
        }
        System.out.println("Clusters Made!");
    }

    private Vector<Event> containEvents;
    private Event center;
    private boolean contentChanged;
    protected EventCluster(){
        containEvents=new Vector<>();
    }
    private void addEvent(Event e){
        containEvents.add(e);
        e.belongTo=this;
        contentChanged=true;
    }
    private void removeEvent(Event e){
        containEvents.remove(e);
        contentChanged=true;
    }
    private double similarity(Event e){
        return center.cosTo(e);
    }
    private double averageDistance(Event e){
        double res=0;
        for(Event e0:containEvents){
            res+=e0.cosTo(e);
        }
        res/=containEvents.size();
        return res;
    }
    private void remarkCenter(){
        System.out.println("Remark Center...");
        double max=0;
        Event cen=null;
        for(Event e:containEvents){
            double m=averageDistance(e);
            if(m>max){
                cen=e;
                max=m;
            }
        }
        center=cen;
        contentChanged=false;
    }
    public List<Event> getCluster(){
        ArrayList<Event> res=new ArrayList<>();
        for(Event e:containEvents){
            boolean added=false;
            for(int i=0;i<res.size();i++){
                if (res.get(i).weight<e.weight){
                    res.add(i,e);
                    added=true;
                    break;
                }
            }
            if(!added){
                res.add(e);
            }
        }
        return res;
    }
    public String getKeyWord(){
        double[] times=new double[allWords.size()];
        for(Event e:containEvents){
            for(int i=0;i<allWords.size();i++){
                String seg=allWords.get(i);
                if(e.seg_text.containsKey(seg)){
                    times[i]+=e.seg_text.get(seg).tf_idf;
                }
            }
        }
        int index=0;
        double max=0;
        for(int i=0;i<allWords.size();i++){
            if(times[i]>max){
                max=times[i];
                index=i;
            }
        }
        return allWords.get(index);
    }

    @Override
    public String toString(){
        return getKeyWord()+"\n"+containEvents.size()+"\n"+center.title;
    }
}

class TestEventCluster extends EventCluster{
    static private Vector<Event> testEvents;
    protected TestEventCluster(int t){
        testEvents=new Vector<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String tmp = EventCluster.EventsListUrl + "&page=1&size=20";
                    URL u = new URL(tmp);
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
                    JSONArray jarry = jobj.getJSONArray("data");
                    for (int i = 0; i < jarry.length(); i++) {
                        JSONObject jobj2 = jarry.getJSONObject(i);
                        Event e = new Event();
                        e.id = jobj2.getString("_id");
                        e.title = jobj2.getString("title");
                        String k = jobj2.getString("seg_text");
                        String[] segs = k.split(" ");
                        for (String seg : segs) {
                            e.words++;
                            if (e.seg_text.containsKey(seg)) {
                                e.seg_text.get(seg).addTime();
                            } else {
                                e.seg_text.put(seg, new Value());
                            }
                        }
                        testEvents.add(e);
                        System.out.println("get Test Event "+testEvents.size());
                    }
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (testEvents.size()==0);
    }
    @Override
    public String getKeyWord(){
        return "TestKeyWord";
    }
    @Override
    public List<Event> getCluster(){
        ArrayList<Event> l=new ArrayList<>();
        l.addAll(testEvents);
        return l;
    }
}