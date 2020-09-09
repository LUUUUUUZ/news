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

import butterknife.BindView;

public class MapResultFragment extends BasicFragment {
    @Override
    protected int getLayoutResource() {
        return R.layout.map_result_fragment;
    }

    MapResultFragment(Location l){
        location=l;
    }
    Location location;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    void initView(){
        lineChartManager1= new LineChartManager(lineChart1);
        cured.setOnClickListener(listener);
        dead.setOnClickListener(listener);

        //展示图表
        lineChartManager1.showLineChart(location,"confirmed",getResources().getColor(R.color.blue));
        lineChartManager1.addLine(location,"cured",getResources().getColor(R.color.orange));

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
                    lineChartManager1.resetLine(1,location,"cured",getResources().getColor(R.color.orange),1);
                    break;
                case R.id.dead:
                    view_cured.setBackground(getResources().getDrawable(R.drawable.shape_round_gray));
                    view_dead.setBackground(getResources().getDrawable(R.drawable.shape_round_orange));
                    lineChartManager1.resetLine(1,location,"dead",getResources().getColor(R.color.orange),2);
                    break;
            }
        }
    };
}
