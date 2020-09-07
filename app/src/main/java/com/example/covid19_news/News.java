package com.example.covid19_news;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;

public class News {

    String title;
    String publisher;
    LocalDateTime publishTime;
    String content;
    boolean isRead;


    public LocalDateTime getPublishTime(){
        return publishTime;
        //to do here
    }

}
