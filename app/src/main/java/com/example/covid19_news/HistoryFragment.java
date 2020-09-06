package com.example.covid19_news;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import butterknife.BindView;

public class HistoryFragment extends BasicFragment {
    //上拉刷新、下拉加载
    //外层 包括头+身体+脚
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    //新闻正文部分
    @BindView(R.id.news_layout)
    RecyclerView newsView;
    //最上面的标题
    @BindView(R.id.history_toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutResource() {
        //返回当前对应的历史碎片
        return R.layout.history_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //碎片创建后，把toolbar和主界面的菜单项连接起来：
        MainActivity a=(MainActivity) getActivity();
        a.setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                a,a.drawerLayout, toolbar,R.string.main_navigation_drawer_open,R.string.main_navigation_drawer_close
        );
        a.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        a.getSupportActionBar().setDisplayShowTitleEnabled(false);
//        //建立右上角的菜单栏，进行删除操作
//        setHasOptionsMenu(true);

        initData();
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.edit_news,menu);
//        //如果正在更改，就把edit放置成√对应clearall可见
//        //to do here
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId()){
//            case R.id.clear_all:
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    void initData(){

    }
}
