package com.java.zhutianyao;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;


import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SwipeConsumer;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;

import java.util.Map;

import butterknife.BindView;

public class ScholarActivity extends BasicActivity {
    @BindView(R.id.scholar_single_toolbar)
    Toolbar toolbar;
    @BindView(R.id.scholar_avater)
    ImageView avater;
    @BindView(R.id.scholar_name)
    TextView name;
    @BindView(R.id.scholar_hindex)
    TextView hindex;
    @BindView(R.id.scholar_activity)
    TextView activity;
    @BindView(R.id.scholar_risingStar)
    TextView risingStar;
    @BindView(R.id.scholar_citations)
    TextView citations;
    @BindView(R.id.scholar_pubs)
    TextView pubs;
    @BindView(R.id.scholar_affiliation)
    TextView affiliation;
    @BindView(R.id.scholar_position)
    TextView position;
    @BindView(R.id.scholar_followed)
    TextView followed;
    @BindView(R.id.scholar_viewed)
    TextView viewed;
    @BindView(R.id.scholar_edu_layout)
    LinearLayout edu_layout;
    @BindView(R.id.scholar_edu)
    TextView edu;
    @BindView(R.id.scholar_bio_layout)
    LinearLayout bio_layout;
    @BindView(R.id.scholar_bio)
    TextView bio;
    @BindView(R.id.scholar_homepage_layout)
    LinearLayout homapage_layout;
    @BindView(R.id.scholar_homepage)
    TextView homepage;
    @BindView(R.id.scholar_work_layout)
    LinearLayout work_layout;
    @BindView(R.id.scholar_work)
    TextView work;
    @BindView(R.id.scholar_table_layout)
    LinearLayout table_layout;
    @BindView(R.id.scholar_tags_table)
    TableLayout tags;

    private Scholar scholar;
    private SwipeConsumer consumer;

    @Override
    protected int getLayoutResource() {
        return R.layout.scholar_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        consumer= SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                .enableLeft();

        initData();
    }

    private void initData(){
        scholar=Global.getScholarByID(getIntent().getExtras().getString("id"));

        if(scholar.name_zh.isEmpty()){
            getSupportActionBar().setTitle(scholar.name);
            name.setText(scholar.name);
        }else{
            getSupportActionBar().setTitle(scholar.name_zh);
            name.setText(scholar.name_zh);
        }
        avater.setImageBitmap(scholar.avatar);
        hindex.setText(String.valueOf(scholar.hindex));
        activity.setText(String.format("%.2f",scholar.activity));
        risingStar.setText(String.format("%.2f",scholar.risingStar));
        citations.setText(String.valueOf(scholar.citations));
        pubs.setText(String.valueOf(scholar.pubs));
        position.setText(scholar.prf.position);
        if(scholar.prf.affiliation_zh.isEmpty()){
            affiliation.setText(scholar.prf.affiliation);
        }else{
            affiliation.setText(scholar.prf.affiliation_zh);
        }

        followed.setText(String.valueOf(scholar.followed));
        viewed.setText(String.valueOf(scholar.viewed));

        if(scholar.prf.edu.isEmpty()){
            edu_layout.setVisibility(View.GONE);
        }else{
            edu.setText(scholar.prf.edu);
        }

        if(scholar.prf.bio.isEmpty()){
            bio_layout.setVisibility(View.GONE);
        }else{
            bio.setText(scholar.prf.bio);
        }

        if(scholar.prf.homepage.isEmpty()){
            homapage_layout.setVisibility(View.GONE);
        }else{
            homepage.setText(scholar.prf.homepage);
        }

        if(scholar.prf.work.isEmpty()){
            work_layout.setVisibility(View.GONE);
        }else{
            work.setText(scholar.prf.work);
        }

        Map<String,Integer> data =scholar.tags;
        if(data.isEmpty()){
            table_layout.setVisibility(View.GONE);
        }
        else {
            for(Map.Entry<String,Integer> entry:data.entrySet()){
                TableRow tableRow = new TableRow(this);
                TextView textView1=new TextView(this);
                TextView textView2=new TextView(this);
                textView1.setText(entry.getKey());
                textView2.setText(String.valueOf(entry.getValue()));
                tableRow.addView(textView1);
                tableRow.addView(textView2);
                tableRow.setPadding(0,20,0,20);
                tags.addView(tableRow);
            }
            tags.setStretchAllColumns(true);
            tags.setColumnShrinkable(0,true);
        }

    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_stay,R.anim.slide_left_exit);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
