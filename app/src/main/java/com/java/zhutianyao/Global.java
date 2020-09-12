package com.java.zhutianyao;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Global {
    static protected MyDataBase db;
    static private LinkedList<String> ChosenTypes;
    static private LinkedList<String> RemainTypes;
    static private int LoadedHistory;
    static private int LoadedScholar;
    static private EntitySearcher eSearcher;
    static private LinkedList<String> History;
    static private LinkedList<String> SearchHistory;
    static protected LinkedList<String> SearchResult;

    static public void init(Context context){
        System.out.println("Start initializing...");
        db=new MyDataBase(context);
        News.AllNews=new HashMap<>();
        ChosenTypes=new LinkedList<String>(){{
            add("paper");
            add("news");
        }};
        RemainTypes=new LinkedList<>();
        SearchResult=new LinkedList<>();
        LoadedHistory=0;
        LoadedScholar=0;
        History=new LinkedList<>();
        SearchHistory=new LinkedList<>();
        EventList.init();
        eSearcher=new EntitySearcher();
        Location.init();
        Scholar.init();
        EventCluster.init(context);
    }
    static public void clean(Context context){
        Global.db=new MyDataBase(context);
        db.clean();
    }

    static public News getNewsById(String id){
        if(!News.AllNews.containsKey(id)){
            News tmp=new News(id);
            News.AllNews.put(id,tmp);
        }
        return News.AllNews.get(id);
    }

    static public List<News> getForwardList(int size, String type){
        LinkedList<String>from=EventList.ForwardNews;
        if(type.equals(EventList.EveryTypes.get(EventList.paperType))) {
            from = EventList.ForwardPaper;
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
        LinkedList<String>from=EventList.UpdateNews;
        if(type.equals(EventList.EveryTypes.get(EventList.paperType))) {
            from = EventList.UpdatePaper;
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
        for(int i=0;i<size&&SearchResult.size()>0;i++){
            res.add(getNewsById(SearchResult.getFirst()));
            SearchResult.removeFirst();
        }
        return res;
    }
    //Same keyword as lastSearch
    static public List<News> getMoreSearchResult(int size){
        List<News> res=new ArrayList<>();
        for(int i=0;i<size&&SearchResult.size()>0;i++){
            res.add(getNewsById(SearchResult.getFirst()));
            SearchResult.removeFirst();
        }
        return res;
    }
    static public List<String> getSearchHistory(){
        return SearchHistory;
    }

    static public void addHistory(News n){
        n.isRead=true;
        History.addLast(n.id);
        LoadedHistory=0;
    }
    static public List<News> getHistory(int size){
        List<News> res=new ArrayList<>();
        for(int i=1;i<=size&&i+LoadedHistory<=History.size();i++){
            res.add(getNewsById(History.get(History.size()-(i+LoadedHistory))));
        }
        LoadedHistory+=res.size();
        return res;
    }

    static public Entity searchForEntity(String name){
        return eSearcher.searchFor(name.trim());
    }
    static public List<String> getRecommendEntity(){
        return Entity.RecommendEntity;
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

    static public List<Location> getChinaLocation(){
        return Location.ChinaLocation;
    }
    static public List<Location> getWorldLocation(){
        return Location.WorldLocation;
    }

    static public List<Scholar> getPassedawayScholar(){
        List<Scholar> res=new ArrayList<>();
        for(int i=0;i<Scholar.allScholars.size();i++){
            Scholar s=Scholar.allScholars.get(i);
            if(s.is_passedaway){
                res.add(s);
            }
        }
        return res;
    }
    static public void resetScolarLoading(){
        LoadedScholar=0;
    }
    static public List<Scholar> getScholars(int size){
        List<Scholar> res=new ArrayList<>();
        for(int i=0;i<size&&i+LoadedScholar<Scholar.allScholars.size();i++){
            res.add(Scholar.allScholars.get(i+LoadedScholar));
        }
        LoadedScholar+=res.size();
        System.out.println(Scholar.allScholars.size()+" "+res.size()+" "+LoadedScholar);
        return res;
    }
    static public Scholar getScholarByID(String id){
        Scholar res=null;
        for(int i=0;i<Scholar.allScholars.size();i++){
            if(Scholar.allScholars.get(i).id.equals(id)){
                res=Scholar.allScholars.get(i);
            }
        }
        return res;
    }

    static public List<EventCluster> getClusterResult(){
        if(!EventCluster.clusteringOver){
            return new LinkedList<>();
        }
        else {
            return EventCluster.allCluster;
        }
    }
    static public EventCluster getTestCluster(){
        return new TestEventCluster(0);
    };
}