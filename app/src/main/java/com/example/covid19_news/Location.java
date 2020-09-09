package com.example.covid19_news;

import java.util.ArrayList;
import java.util.List;

class Everyday{
    String data;//大概你整合一下形式，因为他提供的本来就是标准时间格式，砍一下，只要0710,0340，这样,不要年了
    int confirmed;
    int cured;
    int dead;

}

public class Location {
    //返回当下累计值
    String name;
    List<Everyday> data;//这个数据你看看怎么构造，不会显示太多，大概7-10个，你可以跨度大一点，一个月存一次

}

//get_China_locations() 返回List<Location> 数量你看，反正你给我多少我就会展示多少

//get_World_locations() 返回List<Location> 同上

