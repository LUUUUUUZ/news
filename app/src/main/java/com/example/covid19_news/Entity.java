package com.example.covid19_news;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Relation{
    String relation;
    String lable;
    boolean forward;
    Relation(JSONObject jobj){
        try {
            relation=jobj.getString("relation");
            lable=jobj.getString("label");
            forward=jobj.getBoolean("forward");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

public class Entity {
    Entity(){
        relations=new ArrayList<>();
        properties=new HashMap<>();
    }
    //我没有写get函数，所以不管其他，不要设置成私有的了，我就直接用·的方式访问这些成员变量了
    boolean isEmpty;
    boolean hasImage;
    //实体
    String name;
    //应该是enwiki baidu zhiwiki其中一个用来基本描述的
    String description;
    //属性
    Map<String,String> properties;
    //关系，不显示图的话，应该不需要forward这些
    List<Relation> relations;
    //根据url得到的图片，可能为空
    Bitmap image;
}

//Entity get_entity(String name){
//    return Entity;
//    //这里是搜索函数，做完全匹配，和name一样，才返回唯一这个实体，否则就搜不到
//}

//List<String> get_examples(){
//    //主页面写了几个实体推荐，只需要返回给我几个名字，让我展示就好了，点进去的时候会再调用get_entity得到具体内容的
//}
