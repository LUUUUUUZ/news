package com.java.zhutianyao;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ScholarFragment extends BasicFragment {
    private int initIndex;

    @BindView(R.id.scholar_toolbar)
    Toolbar toolbar;
    @BindView(R.id.scholar_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.scholar_pager)
    ViewPager viewPager;


    @Override
    protected int getLayoutResource() {
        return R.layout.scholar_activity;
    }

    ScholarFragment(int index){
        this.initIndex=index;
    }

    private PagerAdapter pagerAdapter;

    @Nullable
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //碎片创建后，把toolbar和主界面的菜单项连接起来：
        MainActivity a=(MainActivity) getActivity();
        a.setSupportActionBar(toolbar);
        a.getSupportActionBar().setDisplayShowTitleEnabled(false);

        tabLayout.setupWithViewPager(viewPager);
        initData();
    }

    void initData(){
        List<String> chosen=new ArrayList<>();
        chosen.add(0,"高关注学者");
        chosen.add(1,"追忆学者");
        pagerAdapter = new PagerAdapter(getChildFragmentManager(),chosen);
        viewPager.setAdapter(pagerAdapter);
        int index=initIndex+1;
        if(index>=chosen.size())
            index=chosen.size()-1;
        if(index<0){
            index=0;
        }
        tabLayout.getTabAt(index).select();

    }

    class PagerAdapter extends FragmentPagerAdapter{
        List<String> tabs;
        Fragment[] fragments;

        PagerAdapter(FragmentManager fm,List<String> tabs){
            super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.tabs=tabs;
            fragments= new Fragment[tabs.size()];
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if(fragments[position]!=null)
                return fragments[position];
            return fragments[position]= new ScholarTabFragment(tabs.get(position));
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position);
        }
    }
}
