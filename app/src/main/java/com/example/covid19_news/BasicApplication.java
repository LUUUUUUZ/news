package com.example.covid19_news;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class BasicApplication extends Application {
    private static Context context;
    private static Toast toast;

    public static Context getContext() {
        return context;
    }

    public static void showToast(String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Global.init(context);
    }
}
