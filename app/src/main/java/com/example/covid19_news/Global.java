package com.example.covid19_news;

import android.content.Context;

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
    static String EventUrl="https://covid-dashboard.aminer.cn/api/event/";
    static String EventListUrl="https://covid-dashboard.aminer.cn/api/events/";
    static String EntityQueueUrl="https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=";
    static HashMap<Integer,String> EveryTypes;
    static LinkedList<String> ChosenTypes;
    static LinkedList<String> RemainTypes;
    static final int allType=0;
    static final int eventType=1;
    static final int pointsType=2;
    static final int newsType=3;
    static final int paperType=4;
    static final int ListMax=200;
    static int LoadedHistory;
    static LinkedList<String> History;
    static LinkedList<String> SearchHistory;
    static LinkedList<String> ForwardNews;
    static LinkedList<String> ForwardPaper;
    static LinkedList<String> UpdateNews;
    static LinkedList<String> UpdatePaper;
    static LinkedList<String> SearchResult;
    static LinkedList<String> RecommendEntity;
    static HashMap<String,News> AllNews;
    //static LinkedList<Location> ChinaLocation;
    //static LinkedList<Location> WorldLocation;

    static public void init(Context context){
        System.out.println("Start initializing...");
        db=new MyDataBase(context);
        AllNews=new HashMap<>();
        ForwardNews=new LinkedList<>();
        ForwardPaper=new LinkedList<>();
        UpdateNews=new LinkedList<>();
        UpdatePaper=new LinkedList<>();
        SearchResult=new LinkedList<>();
        EveryTypes=new HashMap<Integer, String>(){{
            put(allType, "all");
            put(eventType, "event");
            put(pointsType, "points");
            put(newsType, "news");
            put(paperType, "paper");
        }};
        ChosenTypes=new LinkedList<String>(){{
            add("paper");
            add("news");
        }};
        RemainTypes=new LinkedList<>();
        LoadedHistory=0;
        History=db.getHistory();
        SearchHistory=new LinkedList<>();
        RecommendEntity=new LinkedList<String>(){{
            add("covid-19");
            add("疫情");
            add("疫苗");
            add("病毒");
            add("新型冠状病毒");
        }};
        EntitySearcher.init();
        //Location.init();
        EventList downloadNews=new EventList(newsType);
        downloadNews.makeList();
        downloadNews.makeUpdate();
        EventList downloadPaper=new EventList(paperType);
        downloadPaper.makeList();
        downloadNews.makeUpdate();
    }

    static public News getNewsById(String id){
        if(!AllNews.containsKey(id)){
            News tmp=new News(id);
            AllNews.put(id,tmp);
        }
        return AllNews.get(id);
    }

    static public List<News> getForwardList(int size, String type){
        LinkedList<String>from=Global.ForwardNews;
        if(type.equals(Global.EveryTypes.get(Global.paperType))) {
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
        if(type.equals(Global.EveryTypes.get(Global.paperType))) {
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
        SearchResult.clear();
        db.searchFor(keyword.trim());
        SearchHistory.addLast(keyword.trim());
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

    static public List<String> getSearchHistory(){
        return SearchHistory;
    }

    static public void addHistory(News n){
        db.insertHistory(n);
        History.addLast(n.id);
        LoadedHistory=0;
    }

    static public List<News> getHistory(int size){
        List<News> res=new ArrayList<>();
        for(int i=LoadedHistory;i<LoadedHistory+size&&i<History.size();i++){
            res.add(getNewsById(History.get(History.size()-1-i)));
            LoadedHistory++;
        }
        Collections.reverse(res);
        return res;
    }

    static public void clean(Context context){
        Global.db=new MyDataBase(context);
        db.clean();
    }

    static public Entity searchForEntity(String name){
        EntitySearcher.searchFor(name.trim());
        while (!EntitySearcher.searchFinished){}
        return EntitySearcher.searchResult;
    }

    static List<String> getRecommendEntity(){
        return RecommendEntity;
    }

    static public List<String> getChosenTypes() {
        return ChosenTypes;
    }
    static public List<String> getRemainTypes(){
        return RemainTypes;
    }
    static public void updateTypes(List<String> chosen){
        ChosenTypes.clear();
        RemainTypes.clear();
        if(chosen.contains("news")){
            ChosenTypes.add("news");
        }
        else {
            RemainTypes.add("news");
        }
        if(chosen.contains("paper")){
            ChosenTypes.add("paper");
        }
        else {
            RemainTypes.add("paper");
        }
    }
}