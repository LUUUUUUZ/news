package com.java.zhutianyao;

import java.time.format.DateTimeFormatter;

public class Constants {
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String APIUrl="https://covid-dashboard.aminer.cn/api/events/list";
    public static final int PAGE_SIZE=15;
    public static final String[] category={"all","event","points","news","paper"};
    public static final int SEARCH_HISTORY_LIMIT =10;
    //add here
}
