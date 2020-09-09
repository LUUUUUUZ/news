package com.example.covid19_news;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class MyDataBase extends SQLiteOpenHelper{
    final static String DataBaseName="NewsDataBase";

    public MyDataBase(Context context) {
        super(context, DataBaseName, null, 1);
        //SQLiteDatabase.openOrCreateDatabase("/data/data/com.news.db/databases/news.db",null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists "+Global.NewsTableName+"("+
                Global.ID+" varchar(24) primary key ,"+
                Global.TITLE+" Text,"+
                Global.DATE+" Text,"+
                Global.SOURCE+" Text,"+
                Global.CONTENT+" Text,"+
                Global.TYPE+" Integer )";
        db.execSQL(sql);

        String sql1 = "create table if not exists "+Global.HistoryTableName+" ("+
                Global.ID+" Text primary key) ";
        db.execSQL(sql1);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public synchronized void insertNews(JSONObject jobj) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if(!findNews(jobj.getString("_id"))){
                ContentValues cv = new ContentValues();
                cv.put(Global.ID, jobj.getString("_id"));
                cv.put(Global.TITLE, jobj.getString("title"));
                cv.put(Global.DATE, jobj.getString("date"));
                cv.put(Global.SOURCE, jobj.getString("source"));
                cv.put(Global.CONTENT,jobj.getString("content"));
                cv.put(Global.TYPE,jobj.getString("type"));
                //cv.put(Global.LOADED,true);
                System.out.println("inserting news "+jobj.getString("_id"));
                db.insert(Global.NewsTableName, null, cv);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject queryNews(String id) {
        JSONObject jobj = new JSONObject();
        SQLiteDatabase db=getReadableDatabase();
        if(findNews(id)){
            Cursor cursor = db.query(Global.NewsTableName, null, null, null, null, null, null);
            boolean h=false;
            for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
                String k=cursor.getString(0);
                if(k.equals(id)) {
                    h=true;
                    break;
                }
            }
            try {
                jobj.put(Global.ID,cursor.getString(0));
                jobj.put(Global.TITLE,cursor.getString(1));
                jobj.put(Global.DATE,cursor.getString(2));
                jobj.put(Global.SOURCE,cursor.getString(3));
                jobj.put(Global.CONTENT,cursor.getString(4));
            }catch (JSONException e) {
                e.printStackTrace();
            }
            cursor.close();
        }
        return jobj;
    }


    public boolean findNews(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Global.NewsTableName, null, null, null, null, null, null);
        boolean h=false;
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
            String k=cursor.getString(0);
            if(k.equals(id)) {
                h=true;
                break;
            }
        }
        return h;
    }

    public void clean(){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="DROP TABLE "+Global.NewsTableName;
        db.execSQL(sql);
        String sql2="DROP TABLE "+Global.HistoryTableName;
        db.execSQL(sql2);
        onCreate(db);
    }

    public void insertHistory(News n){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.query(Global.HistoryTableName,null,null,null,null,null,null);
        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            if(c.getString(0).equals(n.id)){
                return;
            }
        }
        ContentValues cv = new ContentValues();
        cv.put(Global.ID, n.id);
        db.insert(Global.HistoryTableName, null, cv);
        System.out.println("inserting history "+n.id);
        c.close();
    }

    public LinkedList<String> getHistory(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(Global.HistoryTableName,null,null,null,null,null,null);
        LinkedList<String> his=new LinkedList<>();
        for(c.moveToLast();!c.isBeforeFirst();c.moveToPrevious()){
            his.addLast(c.getString(0));
        }
        c.close();
        return his;
    }

    public void searchFor(final String keyword){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(Global.NewsTableName,null,null,null,null,null,null);
        for(c.moveToFirst();(!c.isAfterLast());c.moveToNext()){
            String k=c.getString(1);
            if(k.contains(keyword)){
                Global.SearchResult.addLast(c.getString(0));
            }
        }
        c.close();
    }
}
