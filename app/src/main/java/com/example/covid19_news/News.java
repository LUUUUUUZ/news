package com.example.covid19_news;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;

public class News {

    String id;
    String title;
    String publisher;
    LocalDateTime publishTime;
    String content;
    boolean isRead;

    News(String id){
        //有了id可以建立整个详细新闻，需要在新闻正文内容访问content内容
    }

    public LocalDateTime getPublishTime(){
        return publishTime;
        //to do here
    }

}
