package com.example.covid19_news;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import butterknife.BindView;

public class DataMapFragment extends BasicFragment{
    @BindView(R.id.china_table)
    TableLayout china_table;
    @BindView(R.id.world_table)
    TableLayout world_table;

    List<Location> china_locations;
    List<Location> world_locations;


    @Override
    protected int getLayoutResource() {
        return R.layout.data_map_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();

    }

    void initData(){
        //获取数据
//        china_locations=get_china_locations();
//        world_locations=get_world_locations();
    }

    void initView(){
        for(Location l: china_locations){
            TableRow tableRow = new TableRow(getContext());
            TextView textView=new TextView(getContext());
            Button button = new Button(getContext());
            textView.setText(l.name);
            button.setBackgroundResource(R.drawable.ic_go);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    MapResultFragment f=new MapResultFragment(l);
                    final DataMapActivity a =(DataMapActivity) getActivity();
                    a.switchFragment(f);
                }
            });
            tableRow.addView(textView);
            tableRow.addView(button);
            china_table.addView(tableRow);
        }

        for(Location l: world_locations){
            TableRow tableRow = new TableRow(getContext());
            TextView textView=new TextView(getContext());
            Button button = new Button(getContext());
            textView.setText(l.name);
            button.setBackgroundResource(R.drawable.ic_go);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    MapResultFragment f=new MapResultFragment(l);
                    final DataMapActivity a =(DataMapActivity) getActivity();
                    a.switchFragment(f);
                }
            });
            tableRow.addView(textView);
            tableRow.addView(button);
            china_table.addView(tableRow);
        }
    }
}
