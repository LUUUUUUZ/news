package com.java.zhutianyao;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ScholarTabFragment  extends BasicFragment{

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
    private ScholarAdapter adapter;
    private String category;

    ScholarTabFragment(String category){
        this.category=category;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.news_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter=ScholarAdapter.newAdapter(getContext(), newsView, new ScholarAdapter.OnClick() {
            @Override
            public void click(View view, int position, Scholar scholar) {
                //显示学者正文部分
                Intent intent= new Intent(getActivity(),ScholarActivity.class);
                intent.putExtra("id",scholar.id);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_stay);
            }
        });

        initData();
    }

    void initData(){
        loadMore(true);
        emptyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                emptyLayout.setVisibility(View.INVISIBLE);
                loadingLayout.setVisibility(View.VISIBLE);
                loadMore(true);
            }
        });

        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableRefresh(false);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore(false);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                refresh();

            }
        });
    }

    private void loadMore(boolean first){
        List<Scholar> data;
        if(category.equals("高关注学者")){
            data=Global.getScholars(Constants.PAGE_SIZE);
        }else {
            if(!adapter.loaded) {
                data = Global.getPassedawayScholar();
                adapter.loaded = true;
            }
            else
                data=new ArrayList<>();
        }

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
        }
        else{
            if(data.isEmpty()){
                refreshLayout.finishLoadMoreWithNoMoreData();
            }else{
                adapter.add(data);
                refreshLayout.finishLoadMore();
            }
        }
    }
}
