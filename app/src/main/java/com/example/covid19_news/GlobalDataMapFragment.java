package com.example.covid19_news;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import butterknife.BindView;

public class GlobalDataMapFragment extends ChinaDataMapFragment{
    //city这里认为是国家就好了
    @BindView(R.id.china_global_toolbar)
    TextView toolbar;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setText("Global DataMap");
    }

    void initData(){
        //获取数据
        //这里可以用bool表示string是国家还是城市true：是city，中国地图数据，false，世界地图数据，维护两份list就好
        //cities= CityAdapter.getinstance().getcities(false);
    }
}
