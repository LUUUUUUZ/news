package com.example.covid19_news;

public class HomeFragment extends BasicFragment {
    private int initIndex;

    HomeFragment(int index){
        this.initIndex=index;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.home_fragment;
    }
}
