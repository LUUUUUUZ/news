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
                intent.putExtra("id",news.id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_stay);
            }
        });

        initData();
    }

    void initData() {
        loadMore(true);
        emptyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                emptyLayout.setVisibility(View.INVISIBLE);
                loadingLayout.setVisibility(View.VISIBLE);
                refresh();
            }
        });

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore(false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();

            }
        });

    }

    private void loadMore(boolean first){
        List<News> data=Global.getForwardList(Constants.PAGE_SIZE,category);


        if(first){
            adapter.clear();
            adapter.add(data);
            refreshLayout.finishLoadMore();
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
               refreshLayout.finishLoadMoreWithNoMoreData();
            }else{
                adapter.add(data);
                refreshLayout.finishLoadMore();

            }
        }
        //下刷加载更多

        //传递一个size，表示希望新给我多少条，一个目录，是news还是paper，一个enddate，表示要跟着它后面的，返回一个List<News> data;
//        System.out.println("!!!!"+data.size());


    }

    void refresh(){
        //上拉刷新
        //参数含义：size categories startDate endData
//        List<News> data=refreshnews(Constants.PAGE_SIZE,category,
//                first ? LocalDateTime.now().minusWeeks(1).format(Constants.TIME_FORMATTER)
//                        :adapter.get(0).getPublishTime().plusSeconds(1).format(Constants.TIME_FORMATTER),
//                LocalDateTime.now().format(Constants.TIME_FORMATTER))
        List<News> data=Global.getUpdateList(Constants.PAGE_SIZE,category);
        if(data.isEmpty()){
            BasicApplication.showToast("no more data to show here");

        }else{
//            System.out.println("????"+data.size());
            adapter.add(data,0);
            newsView.scrollToPosition(0);
        }
        refreshLayout.finishRefresh();
    }
}
