package com.example.covid19_news;

public class SearchResultFragment extends BasicFragment {
    private final String text;
    @Override
    protected int getLayoutResource() {
        return R.layout.news_layout;//新闻页
    }

    public SearchResultFragment(String text){
        this.text=text;
    }
}
