package com.java.zhutianyao;

import android.os.Bundle;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;

import java.util.List;


public class SearchhomeFragment extends BasicFragment{
    private ChipAdapter historyAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.search_home_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final SearchActivity a=(SearchActivity) getActivity();
        //放入search history的监听
        historyAdapter=ChipAdapter.newAdapter(getContext(), view.findViewById(R.id.search_history_layout), new ChipAdapter.OnClick() {
            @Override
            public void click(Chip chip, int position) {
                String text=historyAdapter.get(position);
                historyAdapter.remove(position);
                historyAdapter.add(text,0);
                a.searchText(text);
            }

            @Override
            public void close(Chip chip, int position) {

            }
        });
        initData();
    }
    void initData(){
//        //从接口得到用户现在的搜索历史
        List<String> data=Global.getSearchHistory();
        historyAdapter.add(data);
    }
}
