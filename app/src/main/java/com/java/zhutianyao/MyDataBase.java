package com.java.zhutianyao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class MyDataBase extends SQLiteOpenHelper{
    final static private String DataBaseName="NewsDataBase";
    final static private String NewsTableName="NewsTable";
    final static protected String ID="id";
    final static protected String TITLE="title";
    final static protected String DATE="date";
    final static protected String SOURCE="source";
    final static protected String CONTENT="content";
    final static protected String TYPE="type";

    protected MyDataBase(Context context) {
        super(context, DataBaseName, null, 1);
        //SQLiteDatabase.openOrCreateDatabase("/data/data/com.news.db/databases/news.db",null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists "+NewsTableName+"("+
                ID+" varchar(24) primary key ,"+
                TITLE+" Text,"+
                DATE+" Text,"+
                SOURCE+" Text,"+
                CONTENT+" Text,"+
                TYPE+" Integer )";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    protected synchronized void insertNews(JSONObject jobj) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            if(!findNews(jobj.getString("_id"))){
                ContentValues cv = new ContentValues();
                cv.put(ID, jobj.getString("_id"));
                cv.put(TITLE, jobj.getString("title"));
                cv.put(DATE, jobj.getString("date"));
                cv.put(SOURCE, jobj.getString("source"));
                cv.put(CONTENT,jobj.getString("content"));
                cv.put(TYPE,jobj.getString("type"));
                //cv.put(Global.LOADED,true);
                System.out.println("inserting news "+jobj.getString("_id"));
                db.insert(NewsTableName, null, cv);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    protected JSONObject queryNews(String id) {
        JSONObject jobj = new JSONObject();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NewsTableName, null, null, null, null, null, null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
            String k=cursor.getString(0);
            if(k.equals(id)) {
                try {
                    jobj.put(ID,cursor.getString(0));
                    jobj.put(TITLE,cursor.getString(1));
                    jobj.put(DATE,cursor.getString(2));
                    jobj.put(SOURCE,cursor.getString(3));
                    jobj.put(CONTENT,cursor.getString(4));
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        cursor.close();
        return jobj;
    }

    protected boolean findNews(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(NewsTableName, null, null, null, null, null, null);
        boolean h=false;
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
            String k=cursor.getString(0);
            if(k.equals(id)) {
                h=true;
                break;
            }
        }
        cursor.close();
        return h;
    }

    protected void clean(){
        SQLiteDatabase db=this.getWritableDatabase();
        String sql="DROP TABLE "+NewsTableName;
        db.execSQL(sql);
        onCreate(db);
    }

    protected void searchFor(final String keyword){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(NewsTableName,null,null,null,null,null,null);
        for(c.moveToFirst();(!c.isAfterLast());c.moveToNext()){
            String k=c.getString(1);
            if(k.contains(keyword)){
                Global.SearchResult.addLast(c.getString(0));
            }
        }
        c.close();
    }
}
