package com.java.zhutianyao;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class EntityResultFragment extends BasicFragment {
    String text;//实体的名称
    Entity entity;
    @BindView(R.id.entity_name)
    TextView name;
    @BindView(R.id.image_view)
    FrameLayout image_layout;
    @BindView(R.id.entity_image)
    ImageView image;
    @BindView(R.id.entity_description)
    TextView description;
    @BindView(R.id.table_layout1)
    LinearLayout properties_layout;
    @BindView(R.id.table_layout2)
    LinearLayout relations_layout;
    @BindView(R.id.entity_tablelayout1)
    TableLayout properties_table;
    @BindView(R.id.entity_tablelayout2)
    TableLayout relations_table;
    @BindView(R.id.empty_one)
            LinearLayout empty_layout;
    @BindView(R.id.entity_hasresult)
            LinearLayout entity_hasresult;
    @BindView(R.id.entity_des)
    LinearLayout des_layout;


    EntityResultFragment(String text){
        this.text=text;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.entity_result_fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    void initData(){
       entity=Global.searchForEntity(text);
    }

    void initView() {
        if (entity.isEmpty) {
            empty_layout.setVisibility(View.VISIBLE);
            entity_hasresult.setVisibility(View.INVISIBLE);
        } else {
            empty_layout.setVisibility(View.INVISIBLE);
            entity_hasresult.setVisibility(View.VISIBLE);
            name.setText(entity.name);
            if(!entity.hasImage){
                image_layout.setVisibility(View.GONE);
                image.setImageBitmap(null);
            }
            else{
                image_layout.setVisibility(View.VISIBLE);
                image.setImageBitmap(entity.image);
            }
            if(entity.description.isEmpty()){
                des_layout.setVisibility(View.GONE);
            }
            else{
                description.setText(entity.description);
            }

            Map<String,String> pro=entity.properties;
            if(pro.isEmpty()){
                properties_layout.setVisibility(View.GONE);
            }
            else{
                for(Map.Entry<String,String> entry: pro.entrySet()){
                    TableRow tableRow = new TableRow(getContext());
                    TextView textView1=new TextView(getContext());
                    TextView textView2=new TextView(getContext());
                    textView1.setText(entry.getKey());
                    textView2.setText(entry.getValue());
                    tableRow.addView(textView1);
                    tableRow.addView(textView2);
                    tableRow.setPadding(0,20,0,20);
                    properties_table.addView(tableRow);
                }
                properties_table.setStretchAllColumns(true);
                properties_table.setColumnShrinkable(1,true);
            }

            List<Relation> rela=entity.relations;
            if(rela.isEmpty()){
                relations_layout.setVisibility(View.INVISIBLE);
            }
            else{
                for(Relation r : rela){
                    TableRow tableRow = new TableRow(getContext());
                    TextView textView1=new TextView(getContext());
                    Button button=new Button(getContext());
                    ImageView image=new ImageView(getContext());
                    System.out.println(r.relation);
                    System.out.println(r.label);
                    textView1.setText(r.relation);

                    button.setText(r.label);

                    if(r.forward){
                        image.setImageResource(R.drawable.ic_forward);
                    }else{
                        image.setImageResource(R.drawable.ic_backward);
                    }

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final EntityActivity a= (EntityActivity) getActivity();
                            a.searchText(r.label);
                        }
                    });
                    tableRow.addView(textView1);
                    tableRow.addView(image);
                    tableRow.addView(button);
//                    tableRow.setPadding(0,20,0,20);
                    relations_table.addView(tableRow);
                }
               relations_table.setStretchAllColumns(false);
                relations_table.setColumnShrinkable(0,true);
                relations_table.setColumnShrinkable(2,true);
            }

        }
    }

}
