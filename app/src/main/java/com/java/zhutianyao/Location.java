package com.java.zhutianyao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

class Everyday{
    String date;//大概你整合一下形式，因为他提供的本来就是标准时间格式，砍一下，只要0710,0340，这样,不要年了
    int confirmed;
    int cured;
    int dead;
}

public class Location {
    final static private String EpidemicUrl="https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
    static protected LinkedList<Location> ChinaLocation;
    static protected LinkedList<Location> WorldLocation;

    //返回当下累计值
    String name;
    List<Everyday> data;//这个数据你看看怎么构造，不会显示太多，大概7-10个，你可以跨度大一点，一个月存一次

    private Location(String n){
        name=n;
        data=new LinkedList<>();
    }
    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder(name);
        sb.append(":");
        for(int i=0;i<data.size();i++){
            sb.append(data.get(i).date);
        }
        return sb.toString();
    }

    static protected void init(){
        ChinaLocation=new LinkedList<>();
        WorldLocation=new LinkedList<>();
        final HashMap<String,String> inChina=new HashMap<String, String>(){{
            put("China|Hubei","湖北");
            put("China|Beijing","北京");
            put("China|Tianjin","天津");
            put("China|Shanghai","上海");
            put("China|Chongqing","重庆");
            put("China|Sichuan","四川");
            put("China|Yunnan","云南");
            put("China|Anhui","安徽");
            put("China|Guizhou","贵州");
            put("China|Hunan","湖南");
            put("China|Zhejiang","浙江");
            put("China|Jiangxi","江西");
            put("China|Jiangsu","江苏");
            put("China|Henan","河南");
            put("China|Hebei","河北");
            put("China|Taiwan","台湾");
            put("China|Shandong","山东");
            put("China|Shanxi","山西");
            put("China|Shaanxi","陕西");
            put("China|Fujian","福建");
        }};
        final HashMap<String,String> inWorld=new HashMap<String,String>(){{
            put("China","中国");
            put("United States of America","美国");
            put("United Kingdom","英国");
            put("Russia","俄罗斯");
            put("France","法国");
            put("India","印度");
            put("Spain","西班牙");
            put("Italy","意大利");
            put("Germany","德国");
            put("Poland","波兰");
            put("Canada","加拿大");
            put("Brazil","巴西");
            put("South Korea","韩国");
            put("Israel","以色列");
            put("Belarus","白俄罗斯");
            put("Iraq","伊拉克");
            put("Afghanistan","阿富汗斯坦");
            put("Japan","日本");
            put("Mexico","墨西哥");
            put("Thailand","泰国");
        }};
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {
                    URL u = new URL(EpidemicUrl);
                    HttpsURLConnection connection = (HttpsURLConnection) u.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Charset", "UTF-8");
                    connection.addRequestProperty("Connection", "Keep-Alive");
                    BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    JSONObject everyWhere=new JSONObject(br.readLine());
                    for(Map.Entry<String, String> entry : inChina.entrySet()){
                        String k = entry.getKey();
                        String v = entry.getValue();
                        JSONObject l=everyWhere.getJSONObject(k);
                        addLocation(l,v,true);
                    }
                    for(Map.Entry<String, String> entry : inWorld.entrySet()){
                        String k = entry.getKey();
                        String v = entry.getValue();
                        JSONObject l=everyWhere.getJSONObject(k);
                        addLocation(l,v,false);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    static private void addLocation(JSONObject place,String name,boolean china){
        List<Location> ll=ChinaLocation;
        if(!china){
            ll=WorldLocation;
        }
        Location l=new Location(name);
        try {
            String begin=place.getString("begin");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter df2=DateTimeFormatter.ofPattern("MM-dd");
                LocalDate t0=LocalDate.parse(begin,DateTimeFormatter.ISO_DATE);
                JSONArray everyday=place.getJSONArray("data");
                int period=everyday.length();
                for(int i=0;i<8;i++){
                    int k=(period*i)/7;
                    if(k>period-1){
                        k=period-1;
                    }
                    JSONArray day=(JSONArray) everyday.get(k);
                    Everyday e=new Everyday();
                    e.confirmed=(int)day.get(0);
                    e.cured=(int)day.get(2);
                    e.dead=(int)day.get(3);
                    LocalDate ti=t0.plusDays(k);
                    e.date=ti.format(df2);
                    l.data.add(e);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ll.add(l);
    }
}