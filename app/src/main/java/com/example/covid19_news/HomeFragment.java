package com.example.covid19_news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;

import butterknife.BindView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;


public class HomeFragment extends BasicFragment {
    private int initIndex;

    @BindView(R.id.home_toolbar)
    Toolbar toolbar;
    @BindView(R.id.home_search_box)
    EditText searchbox;
    @BindView(R.id.home_tab_layout)
    TableLayout tableLayout;
    @BindView(R.id.home_pager)
    ViewPager viewPager;
    @BindView(R.id.home_more_button)
    ImageButton button;


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
                startActivityForResult(new Intent(getActivity(), CategoryActivity.class),3);
                getActivity().overridePendingTransition(R.anim.slide_right_enter,R.anim.slide_stay);
            }
        });



    }
}
