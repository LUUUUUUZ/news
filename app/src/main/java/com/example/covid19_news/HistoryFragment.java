package com.example.covid19_news;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.util.List;

import butterknife.BindView;

public class HistoryFragment extends BasicFragment {
    //上拉刷新、下拉加载
    //外层 包括头+身体+脚
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    //新闻正文部分
    @BindView(R.id.news_layout)
    RecyclerView newsView;
    @BindView(R.id.empty_button)
    Button emptyButton;
    @BindView(R.id.loading_layout)
    LinearLayout loadingLayout;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    //最上面的标题
    @BindView(R.id.history_toolbar)
    Toolbar toolbar;

    private NewsAdapter adapter;


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
        adapter =NewsAdapter.newAdapter(getContext(), newsView, new NewsAdapter.OnClick() {
            @Override
            public void click(View view, int position, News news) {
                Intent intent = new Intent(getActivity(),NewsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_stay);

            }
        });

        loadMore(true);
        emptyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emptyLayout.setVisibility(View.INVISIBLE);
                loadingLayout.setVisibility(View.VISIBLE);
                loadMore(true);
            }
        });
        refreshLayout.resetNoMoreData();
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore(false);
            }
        });


    }

    void loadMore(boolean first) {
        List<News> data=Global.getHistory(Constants.PAGE_SIZE);

        if(first){
            adapter.clear();
            loadingLayout.setVisibility(View.GONE);
            if(data.isEmpty()){
                emptyLayout.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.INVISIBLE);
            }else{
                emptyLayout.setVisibility(View.INVISIBLE);
                refreshLayout.setVisibility(View.VISIBLE);
                adapter.add(data,0);
                refreshLayout.finishLoadMore();
            }

        }
        else{
            if(data.isEmpty()){
                refreshLayout.finishLoadMoreWithNoMoreData();
            }else{
                adapter.add(data,0);
                refreshLayout.finishLoadMore();
            }

        }
    }
}
