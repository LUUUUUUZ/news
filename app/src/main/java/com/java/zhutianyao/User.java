package com.java.zhutianyao;

public class User {
    private static User instance;
    public static User getInstance(){
        // to do here
        return instance;
    }

    //List<News> data=loadmoreNews(Constants.PAGE_SIZE,category,adapter.get(adapter.getItemCount()-1).getPublishTime().minusSeconds(1).format(Constants.TIME_FORMATTER));
    //加载更多的新闻
    //传递一个size，表示希望新给我多少条，一个目录，是news还是paper，一个enddate，表示要跟着它后面的，返回一个List<News> data

//    List<News> loadmoreNews(int size, String category, DateTimeFormatter enddate){
//        return data;
//    }

    //上拉刷新
    //参数含义：size categories startDate endData
//        List<News> data=refreshnews(Constants.PAGE_SIZE,category,
//                first ? LocalDateTime.now().minusWeeks(1).format(Constants.TIME_FORMATTER)
//                        :adapter.get(0).getPublishTime().plusSeconds(1).format(Constants.TIME_FORMATTER),
//                LocalDateTime.now().format(Constants.TIME_FORMATTER))
//    List<News> refreshnews(int size,String category,DateTimeFormatter startDate,DateTimeFormatter endData){
//        return data;
//    }

//    List<String> getchosen(){
//        //得到用户选择的category
//    }

//    List<String> getremain(){
//        //得到剩下的category
//    }
//
//    List<News> getHistory(){
//        //得到历史记录
//    }

//    void addHistory(News news){
//        //添加历史记录
//    }

    //从接口得到用户现在的搜索历史
//    List<String> data=getSearchHistory();
//        historyAdapter.add(data);

//    List<String> getSearchHistory(){
//
//    }

}
