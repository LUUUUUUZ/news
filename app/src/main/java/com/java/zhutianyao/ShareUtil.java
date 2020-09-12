package com.java.zhutianyao;

import android.content.Context;
import android.content.Intent;

public class ShareUtil {
    private static ShareUtil instance;
    private final String SHARE_PANEL_TITLE = "Share to:";

    private ShareUtil(){

    }

    public static ShareUtil getInstance(){
        if(instance==null){
            synchronized (ShareUtil.class){
                if(instance==null){
                    instance=new ShareUtil();
                }
            }
        }
        return instance;
    }

    public void shareText(Context context,String title,String content){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,title);
        if(content.length()>50){
            intent.putExtra(Intent.EXTRA_TEXT,content.substring(0,50)+"...");

        }else{
            intent.putExtra(Intent.EXTRA_TEXT, content);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent,SHARE_PANEL_TITLE));
    }

}
