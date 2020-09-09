package com.example.covid19_news;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class News {
    String id;
    String title;
    String publisher;
    LocalDateTime publishTime;
    String content;
    boolean isRead;

    News(String id){
        /*if(!Global.db.findNews(id)) {
            OneNews on=new OneNews(id);
        }*/
        JSONObject jobj=Global.db.queryNews(id);
        try {
            this.id = jobj.getString(Global.ID);
            this.title = jobj.getString(Global.TITLE);
            this.publisher = jobj.getString(Global.SOURCE);
            this.content = jobj.getString(Global.CONTENT);
            this.publishTime = timeDecode(jobj.getString(Global.DATE));
            this.isRead = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public LocalDateTime getPublishTime(){
        return publishTime;
        //to do here
    }

    private LocalDateTime timeDecode(String t){
        //"Wed, 08 Jul 2020 15:54:59 GMT"
        String k=t.split(", ")[1];
        LocalDateTime ldt = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss zzzz", Locale.ENGLISH);
            ldt = LocalDateTime.parse(k, df);
        }
        return ldt;
    }

    @Override
    public String toString(){
        return id+" "+title;
    }
}