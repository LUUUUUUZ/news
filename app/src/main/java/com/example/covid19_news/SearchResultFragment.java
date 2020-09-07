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

import butterknife.BindView;

public class SearchResultFragment extends BasicFragment {
    private final String text;
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
    @Override
    protected int getLayoutResource() {
        return R.layout.news_layout;//新闻页
    }

    public SearchResultFragment(String text){
        this.text=text;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = NewsAdapter.newAdapter(getContext(), newsView, new NewsAdapter.OnClick() {
            @Override
            public void click(View view, int position, News news) {
                Intent intent = new Intent(getActivity(),NewsActivity.class);
                intent.putExtra("id",news.id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_stay);
            }
        });
        initData();
    }
    void refresh(final boolean first){
        //上拉刷新
        //上拉刷新
        //参数含义：size categories startDate endData
//        List<News> data=refreshnews(Constants.PAGE_SIZE,category,
//                first ? LocalDateTime.now().minusWeeks(1).format(Constants.TIME_FORMATTER)
//                        :adapter.get(0).getPublishTime().plusSeconds(1).format(Constants.TIME_FORMATTER),
//                LocalDateTime.now().format(Constants.TIME_FORMATTER))
//        if(first){
//            adapter.clear();
//            adapter.add(data);
//            refreshLayout.finishRefresh();
//            loadingLayout.setVisibility(View.GONE);
//            if(data.isEmpty()){
//                emptyLayout.setVisibility(View.VISIBLE);
//                refreshLayout.setVisibility(View.INVISIBLE);
//            }else{
//                emptyLayout.setVisibility(View.INVISIBLE);
//                refreshLayout.setVisibility(View.VISIBLE);
//            }
//
//        }else{
//            if(data.isEmpty()){
//                    BasicApplication.showToast("no more data to show here");
//
//            }else{
//                adapter.add(data,0);
//                newsView.scrollToPosition(0);
//            }
//            refreshLayout.finishRefresh();
//        }

    }

    void loadMore(){
        //下刷加载更多

        //传递一个size，表示希望新给我多少条，一个目录，是news还是paper，一个enddate，表示要跟着它后面的，返回一个List<News> data;
        //List<News> data=loadmoreNews(Constants.PAGE_SIZE,category,adapter.get(adapter.getItemCount()-1).getPublishTime().minusSeconds(1).format(Constants.TIME_FORMATTER));
//        if(data.isEmpty()){
//            refreshLayout.finishLoadMoreWithNoMoreData();
//        }else{
//            adapter.add(data);
//            refreshLayout.finishLoadMore();
//        }
    }

    private void initData(){
        refresh(true);
        emptyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                emptyLayout.setVisibility(View.INVISIBLE);
                loadingLayout.setVisibility(View.VISIBLE);
                refresh(true);
            }
        });
        refreshLayout.setEnableLoadMoreWhenContentNotFull(false);
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
}
