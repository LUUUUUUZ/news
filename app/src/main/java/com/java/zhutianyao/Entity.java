package com.java.zhutianyao;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Relation{
    String relation;
    String label;
    boolean forward;
    protected Relation(JSONObject jobj){
        try {
            relation=jobj.getString("relation");
            label=jobj.getString("label");
            forward=jobj.getBoolean("forward");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

public class Entity {
    final static protected LinkedList<String> RecommendEntity=new LinkedList<String>(){{
        add("covid-19");
        add("疫情");
        add("疫苗");
        add("病毒");
        add("新型冠状病毒");
    }};
    protected Entity(){
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

    @Override
    public String toString(){
        return isEmpty+"\n"+name+"\n"+description+'\n'+properties+'\n'+relations;
    }
}
