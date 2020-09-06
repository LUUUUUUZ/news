package com.example.covid19_news;

import org.json.JSONObject;

import java.time.LocalDateTime;

public class News {

    String title;
    String publisher;
    LocalDateTime publishTime;
    String content;
    boolean isRead;

    List<News>(JSONObject newsdata){
        //to do here
    }

    public LocalDateTime getPublishTime(){
        return publishTime;
        //to do here
    }

}
