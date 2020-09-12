package com.java.zhutianyao;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

public class News {
    static protected HashMap<String,News> AllNews;
    String id;
    String title;
    String publisher;
    LocalDateTime publishTime;
    String content;
    boolean isRead;

    protected News(String id){
        /*if(!Global.db.findNews(id)) {
            OneNews on=new OneNews(id);
        }*/
        JSONObject jobj=Global.db.queryNews(id);
        try {
            this.id = jobj.getString(MyDataBase.ID);
            this.title = jobj.getString(MyDataBase.TITLE);
            this.publisher = jobj.getString(MyDataBase.SOURCE);
            this.content = jobj.getString(MyDataBase.CONTENT);
            this.publishTime = timeDecode(jobj.getString(MyDataBase.DATE));
            this.isRead = false;
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