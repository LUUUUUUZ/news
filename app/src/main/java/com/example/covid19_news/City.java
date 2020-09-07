package com.example.covid19_news;

import java.util.ArrayList;
import java.util.List;

public class City {
    //返回当下累计值
    String name;
    int confirmed;
    int cured;
    int dead;

}

class CityAdapter{
    static private CityAdapter instance;
    List<City> cities;

//    CityAdapter(){
//        cities= ArrayList<City>();
//    }
//
//    static CityAdapter getinstance(){
//        return instance;
//    }
//        List<City> getcities{
//            return cities;
//            //return all cities;
//        }
    //获取数据
    //这里可以用bool表示string是国家还是城市true：是city，中国地图数据，false，世界地图数据，维护两份list就好
    //cities= CityAdapter.getinstance().getcities(true);
}

