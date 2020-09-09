package com.example.covid19_news;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.SwipeConsumer;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;

import org.json.JSONObject;

import butterknife.BindView;

public class NewsActivity extends BasicActivity {
    @BindView(R.id.news_toolbar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView titleView;
    @BindView(R.id.publisher)
    TextView publisherView;
    @BindView(R.id.publish_time)
    TextView publishTimeView;
    @BindView(R.id.content)
    TextView contentView;

    private SwipeConsumer consumer;
    private News news;

    @Override
    protected int getLayoutResource() {
        return R.layout.news_activity;
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
        try{
            news=Global.getNewsById(getIntent().getExtras().getString("id"));

            getSupportActionBar().setTitle(news.publisher);

            titleView.setText(news.title);
            contentView.setText(news.content);
            publisherView.setText(news.publisher);
            publishTimeView.setText(Util.parseTime(news.publishTime));

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_toolbar,menu);
        return true;
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
            case R.id.search_button:
                startActivity(new Intent(this,SearchActivity.class));
                return true;
            case R.id.share_button:
                ShareUtil.getInstance().shareText(this,news.title,news.content);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
