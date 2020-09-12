package com.java.zhutianyao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;


public class HomeFragment extends BasicFragment {
    private int initIndex;

    @BindView(R.id.home_toolbar)
    Toolbar toolbar;
    @BindView(R.id.home_search_box)
    EditText searchbox;
    @BindView(R.id.home_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.home_pager)
    ViewPager viewPager;
    @BindView(R.id.home_more_button)
    ImageButton button;

    private PagerAdapter pagerAdapter;

    HomeFragment(int index){
        this.initIndex=index;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.home_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //碎片创建后，把toolbar和主界面的菜单项连接起来：
        MainActivity a=(MainActivity) getActivity();
        a.setSupportActionBar(toolbar);
        a.getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                a,a.drawerLayout, toolbar,R.string.main_navigation_drawer_open,R.string.main_navigation_drawer_close
        );
        a.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //搜索监听
        searchbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //转到search activity
                startActivity(new Intent(getActivity(),SearchActivity.class));
            }
        });

        //分类列表增删监听
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getActivity(), CategoryActivity.class),1);
                getActivity().overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_stay);
            }
        });

        tabLayout.setupWithViewPager(viewPager);
        initData();


    }

    void initData(){
        //得到user的列表清单
//        List<String> chosen= new ArrayList<>();
//        List<String> remian= new ArrayList<>();
//        chosen.add(0,"news");
//        chosen.add(1,"paper");
        List<String> chosen=Global.getChosenTypes();
        pagerAdapter=new PagerAdapter(getChildFragmentManager(),chosen);
        viewPager.setAdapter(pagerAdapter);
        int index=initIndex+1;
        if(index>=chosen.size())
            index=chosen.size()-1;
        if(index<0)
            index=0;
        tabLayout.getTabAt(index).select();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //1:按钮增删，需要刷新主页面
        if(requestCode==1 && resultCode== Activity.RESULT_OK){
            if(data.getBooleanExtra("hasEdited",false)){
                MainActivity a=(MainActivity) getActivity();
                a.onActivityResult(requestCode,resultCode,data);
                return;
            }
            int position=data.getIntExtra("selectPosision",-1);
            if(position!=-1){
                tabLayout.getTabAt(position+1).select();
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
            return fragments[position]= new HomeTabFragment(tabs.get(position));
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
