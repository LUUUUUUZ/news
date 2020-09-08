package com.example.covid19_news;

public class EntityResultFragment extends BasicFragment {
    String text;//实体的名称

    EntityResultFragment(String text){
        this.text=text;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.entity_result_fragment;
    }
}
