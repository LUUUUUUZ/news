package com.example.covid19_news;

import android.media.Image;
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
       // entity=get_entity(text);
    }

    void initView() {
        if (!entity.exist) {
            empty_layout.setVisibility(View.VISIBLE);
            entity_hasresult.setVisibility(View.INVISIBLE);
        } else {
            empty_layout.setVisibility(View.INVISIBLE);
            entity_hasresult.setVisibility(View.VISIBLE);
            name.setText(entity.name);
            if(entity.image==null){
                image_layout.setVisibility(View.INVISIBLE);
            }
            else{
                image.setImageBitmap(entity.image);
            }
            description.setText(entity.description);

            Map<String,String> pro=entity.properties;
            if(pro==null){
                properties_layout.setVisibility(View.INVISIBLE);
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
                    properties_table.addView(tableRow);
                }
            }

            List<Relations> rela=entity.relations;
            if(rela==null){
                relations_layout.setVisibility(View.INVISIBLE);
            }
            else{
                for(Relations r : rela){
                    TableRow tableRow = new TableRow(getContext());
                    TextView textView1=new TextView(getContext());
                    Button button=new Button(getContext());
                    ImageView image=new ImageView(getContext());

                    textView1.setText(r.relation);

                    button.setText(r.name);

                    if(r.forward){
                        image.setImageResource(R.drawable.ic_forward);
                    }else{
                        image.setImageResource(R.drawable.ic_backward);
                    }

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final EntityActivity a= (EntityActivity) getActivity();
                            a.searchText(r.name);
                        }
                    });
                    tableRow.addView(textView1);
                    tableRow.addView(image);
                    tableRow.addView(button);
                    relations_table.addView(tableRow);
                }
            }

        }
    }

}
