package com.example.covid19_news;

import android.os.Bundle;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;


public class EntityhomeFragment extends BasicFragment{
    private ChipAdapter exampleAdapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.entity_home_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EntityActivity a=(EntityActivity) getActivity();
        //放入search history的监听
        exampleAdapter=ChipAdapter.newAdapter(getContext(), view.findViewById(R.id.entity_example_layout), new ChipAdapter.OnClick() {
            @Override
            public void click(Chip chip, int position) {
                String text=exampleAdapter.get(position);
//                exampleAdapter.remove(position);
//                exampleAdapter.add(text,0);
                a.searchText(text);
            }

            @Override
            public void close(Chip chip, int position) {

            }
        });
        initData();
    }
    void initData(){
//        //从接口得到推荐的实体，构造几个就行？随便啦
//        List<String> data= new ArrayList<>();

    }
}
