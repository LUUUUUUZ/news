package com.example.covid19_news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.time.LocalDateTime;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeTabFragment extends BasicFragment {

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.news_layout)
    RecyclerView newsView;
    @BindView(R.id.loading_layout)
    LinearLayout loadingLayout;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.empty_button)
    Button emptyButton;
    private NewsAdapter adapter;
    private String category;

    HomeTabFragment(String category){this.category = category;}

    @Override
    protected int getLayoutResource() {
        return R.layout.news_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter=NewsAdapter.newAdapter(getContext(),newsView,new NewsAdapter.OnClick(){
            @Override
            public void click(View view, int position, News news) {
                //显示新闻正文部分
                Intent intent = new Intent(getActivity(),NewsActivity.class);
                //放入json文件？
                //intent.putExtra("news",
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_stay);
            }
        });

        initData();
    }

    void initData() {
        refresh(true);
        emptyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                emptyLayout.setVisibility(View.INVISIBLE);
                loadingLayout.setVisibility(View.VISIBLE);
                refresh(true);
            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh(false);

            }
        });

    }

    private void loadMore(){
        //下刷加载更多
        new NewsNetwork.Builder()
                .add("size",""+Constants.PAGE_SIZE)
                .add("categories",category)
                .add("endDate",adapter.get(adapter.getItemCount()-1).getPublishTime().minusSeconds(1).format(Constants.TIME_FORMATTER))
                .build()
                .run(new NewsNetwork.Callback() {
                    @Override
                    public void timeout() {
                        refreshLayout.finishLoadMore(false);
                    }

                    @Override
                    public void error() {
                        refreshLayout.finishLoadMore(false);

                    }

                    @Override
                    public void ok(List<News> data) {
                        if(data.isEmpty()){
                            refreshLayout.finishLoadMoreWithNoMoreData();
                        }else{
                            adapter.add(data);
                            refreshLayout.finishLoadMore();
                        }

                    }
                });

    }

    void refresh(final boolean first){
        //上拉刷新
        new NewsNetwork.Builder()
                .add("size",""+Constants.PAGE_SIZE)
                .add("categories",category)
                .add("startDate",first ? LocalDateTime.now().minusWeeks(1).format(Constants.TIME_FORMATTER)
                        :adapter.get(0).getPublishTime().plusSeconds(1).format(Constants.TIME_FORMATTER)
                        )
                .add("endDate",LocalDateTime.now().format(Constants.TIME_FORMATTER))
                .build()
                .run(new NewsNetwork.Callback() {
                    @Override
                    public void timeout() {
                        refreshLayout.finishRefresh(false);
                        if(first){
                            loadingLayout.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            refreshLayout.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void error() {
                        refreshLayout.finishRefresh(false);
                        if(first){
                            loadingLayout.setVisibility(View.GONE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            refreshLayout.setVisibility(View.INVISIBLE);
                        }


                    }

                    @Override
                    public void ok(List<News> data) {
                        if(first){
                            adapter.clear();
                            adapter.add(data);
                            refreshLayout.finishRefresh();
                            loadingLayout.setVisibility(View.GONE);
                            if(data.isEmpty()){
                                emptyLayout.setVisibility(View.VISIBLE);
                                refreshLayout.setVisibility(View.INVISIBLE);
                            }else{
                                emptyLayout.setVisibility(View.INVISIBLE);
                                refreshLayout.setVisibility(View.VISIBLE);
                            }

                        }else{
                            if(data.isEmpty()){

                            }else{
                                adapter.add(data,0);
                                newsView.scrollToPosition(0);
                            }
                            refreshLayout.finishRefresh();
                        }

                    }
                });

    }
}