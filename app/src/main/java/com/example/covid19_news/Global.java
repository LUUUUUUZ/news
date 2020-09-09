package com.example.covid19_news;

import android.content.Context;
import android.os.Handler;
import android.view.ViewStructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Global {
    static String NewsTableName="NewsTable";
    static String HistoryTableName="HistoryTable";
    static String ID="id";
    static String TITLE="title";
    static String DATE="date";
    static String SOURCE="source";
    static String CONTENT="content";
    static String TYPE="type";
    static String ENTITY="entity";
    static MyDataBase db;
    static String eventUrl="https://covid-dashboard.aminer.cn/api/event/";
    static String eventListUrl="https://covid-dashboard.aminer.cn/api/events/";
    static String entityQueueUrl="https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=";
    static HashMap<Integer,String> everyTypes;
    static final int allType=0;
    static final int eventType=1;
    static final int pointsType=2;
    static final int newsType=3;
    static final int paperType=4;
    static final int ListMax=200;
    static int LoadedHistory;
    static LinkedList<String> History;
    static LinkedList<String> ForwardNews;
    static LinkedList<String> ForwardPaper;
    static LinkedList<String> UpdateNews;
    static LinkedList<String> UpdatePaper;
    static LinkedList<String> SearchResult;
    static HashMap<String,News> allNews;

    static public void init(Context context){
        System.out.println("Start initializing...");
        db=new MyDataBase(context);
        allNews=new HashMap<>();
        ForwardNews=new LinkedList<>();
        ForwardPaper=new LinkedList<>();
        UpdateNews=new LinkedList<>();
        UpdatePaper=new LinkedList<>();
        History=db.getHistory();
        SearchResult=new LinkedList<>();
        everyTypes=new HashMap<>();{
            everyTypes.put(allType, "all");
            everyTypes.put(eventType, "event");
            everyTypes.put(pointsType, "points");
            everyTypes.put(newsType, "news");
            everyTypes.put(paperType, "paper");
        }
        LoadedHistory=0;
        EventList downloadNews=new EventList(newsType);
        downloadNews.makeList();
        downloadNews.makeUpdate();
        EventList downloadPaper=new EventList(paperType);
        downloadPaper.makeList();
        downloadNews.makeUpdate();
    }

    static public News getNewsById(String id){
        if(!allNews.containsKey(id)){
            News tmp=new News(id);
            allNews.put(id,tmp);
        }
        return allNews.get(id);
    }

    static public List<News> getForwardList(int size, String type){
        LinkedList<String>from=Global.ForwardNews;
        if(type.equals(Global.everyTypes.get(Global.paperType))) {
            from = Global.ForwardPaper;
        }
        ArrayList<News> res=new ArrayList<>();
        for(int i=0;i<size&&(!from.isEmpty());i++){
            String id=from.getFirst();
            res.add(getNewsById(id));
            from.removeFirst();
        }
        return res;
    }

    static public List<News> getUpdateList(int size,String type){
        LinkedList<String>from=Global.UpdateNews;
        if(type.equals(Global.everyTypes.get(Global.paperType))) {
            from = Global.UpdatePaper;
        }
        ArrayList<News> res=new ArrayList<>();
        for(int i=0;i<size&&(!from.isEmpty());i++){
            String id=from.getFirst();
            res.add(getNewsById(id));
            from.removeFirst();
        }
        return res;
    }

    static public List<News> getSearchResult(String keyword,int size){
        db.searchFor(keyword.trim());
        List<News> res=new ArrayList<>();
        for(int i=0;i<size;i++){
            res.add(getNewsById(SearchResult.getFirst()));
            SearchResult.removeFirst();
        }
        return res;
    }

    //Same keyword as lastSearch
    static public List<News> getMoreSearchResult(int size){
        List<News> res=new ArrayList<>();
        for(int i=0;i<size;i++){
            res.add(getNewsById(SearchResult.getFirst()));
            SearchResult.removeFirst();
        }
        return res;
    }

    static public void addHistory(News n){
        db.insertHistory(n);
        History.addLast(n.id);
        LoadedHistory=0;
    }

    static public List<News> getHistory(int size){
        if(History==null){
            History=db.getHistory();
        }
        List<News> res=new ArrayList<>();
        for(int i=LoadedHistory;i<LoadedHistory+size&&i<History.size();i++){
            res.add(getNewsById(History.getLast()));
            LoadedHistory++;
        }
        Collections.reverse(res);
        return res;
    }

    static public void clean(Context context){
        Global.db=new MyDataBase(context);
        db.clean();
    }
}
