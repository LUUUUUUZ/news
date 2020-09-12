package com.java.zhutianyao;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;

public class ClusterResultFragment extends BasicFragment {
    EventCluster eventCluster;

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

     EventAdapter adapter;


    ClusterResultFragment(EventCluster eventCluster){
        this.eventCluster=eventCluster;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.news_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingLayout.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.GONE);

        adapter= EventAdapter.newAdapter(getContext(), newsView, new EventAdapter.OnClick() {
            @Override
            public void click(View view, int position, Event event) {

            }
        });

        initData();
    }

    void initData(){
        refreshLayout.setVisibility(View.VISIBLE);
        List<Event> data=eventCluster.getCluster();
        adapter.clear();
        System.out.println("!!!!!!!!!!!!!!!!!!!");
        adapter.add(data);
        for(Event e:eventCluster.getCluster()){
            System.out.println(e.title);
        }
        refreshLayout.finishLoadMore();

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
    }
}
