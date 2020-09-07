package com.example.covid19_news;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

import butterknife.BindView;

public class ChinaDataMapFragment extends BasicFragment {
    @Override
    protected int getLayoutResource() {
        return R.layout.chinadatamap_fragment;
    }
    List<City> cities;

    LineChartManager lineChartManager1;

    @BindView(R.id.lineChart)
    LineChart lineChart1;

    @BindView(R.id.cured)
    ConstraintLayout cured;
    @BindView(R.id.view_cured)
    View view_cured;
    @BindView(R.id.dead)
    ConstraintLayout dead;
    @BindView(R.id.view_dead)
    View view_dead;
    //最上面的标题
    @BindView(R.id.china_toolbar)
    Toolbar toolbar;

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
        a.getSupportActionBar().setDisplayShowTitleEnabled(false);

        initData();
        initView();
    }

    void initData(){
        //获取数据
        //这里可以用bool表示string是国家还是城市true：是city，中国地图数据，false，世界地图数据，维护两份list就好
        //cities= CityAdapter.getinstance().getcities(true);
    }

    void initView(){
        lineChartManager1= new LineChartManager(lineChart1);
        cured.setOnClickListener(listener);
        dead.setOnClickListener(listener);

        //展示图表
        lineChartManager1.showLineChart(cities,"confirmed",getResources().getColor(R.color.blue));
        lineChartManager1.addLine(cities,"cured",getResources().getColor(R.color.orange));

        //设置曲线填充色 以及 MarkerView
        Drawable drawable = getResources().getDrawable(R.drawable.fade_blue);
        lineChartManager1.setChartFillDrawable(drawable);
        lineChartManager1.setMarkerView(getContext());

    }

    View.OnClickListener listener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cured:
                    view_cured.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));
                    view_dead.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    lineChartManager1.resetLine(1,cities,"cured",getResources().getColor(R.color.orange),1);
                    break;
                case R.id.dead:
                    view_cured.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_dead.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));
                    lineChartManager1.resetLine(1,cities,"dead",getResources().getColor(R.color.orange),2);
                    break;
            }
        }
    };
}
