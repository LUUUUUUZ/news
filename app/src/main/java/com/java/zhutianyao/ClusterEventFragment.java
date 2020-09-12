package com.java.zhutianyao;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import butterknife.BindView;

public class ClusterEventFragment extends BasicFragment {
    @BindView(R.id.cluster_table)
    TableLayout cluster_table;
    @BindView(R.id.loading_layout)
    LinearLayout loadingLayout;
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.empty_button)
    Button emptyButton;
    @BindView(R.id.cluster_result)
    LinearLayout cluster_layout;



    @Override
    protected int getLayoutResource() {
        return R.layout.cluster_layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    void initView(){
        initData();
        emptyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                emptyLayout.setVisibility(View.INVISIBLE);
                loadingLayout.setVisibility(View.VISIBLE);
                initData();
            }
        });
    }

    void initData(){
        List<EventCluster> data=Global.getClusterResult();

//                Global.getClusterResult();
        if(data.isEmpty()){
            emptyLayout.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.INVISIBLE);
            cluster_layout.setVisibility(View.INVISIBLE);
        }else{
            emptyLayout.setVisibility(View.INVISIBLE);
            loadingLayout.setVisibility(View.INVISIBLE);
            cluster_layout.setVisibility(View.VISIBLE);
            for(EventCluster l:data){
                TableRow tableRow=new TableRow(getContext());
                TextView textView=new TextView(getContext());
                Button button=new Button(getContext());
                textView.setText(l.getKeyWord());
                button.setBackgroundResource(R.drawable.ic_cluster_go);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClusterResultFragment f =new ClusterResultFragment(l);
                        System.out.println("???? then");
                        final ClusterActivity a= (ClusterActivity) getActivity();
                        a.switchFragment(f);
                    }
                });
                tableRow.addView(textView);
                tableRow.addView(button);
                tableRow.setPadding(30,30,30,30);
                cluster_table.addView(tableRow);
            }
            cluster_table.setColumnStretchable(1,false);
            cluster_table.setColumnStretchable(0,true);

        }

    }
}
