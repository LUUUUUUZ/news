package com.example.covid19_news;

import android.os.Bundle;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class SearchhomeFragment extends BasicFragment{

    @Override
    protected int getLayoutResource() {
        return R.layout.search_home_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //放入search history的监听
        initData();
    }
    void initData(){
        //从接口得到用户现在的搜索历史
    }
}
