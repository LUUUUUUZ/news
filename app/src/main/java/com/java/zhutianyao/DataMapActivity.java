package com.java.zhutianyao;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;

import butterknife.BindView;

public class DataMapActivity extends BasicActivity {

    @BindView(R.id.data_map_toolbar)
    Toolbar toolbar;

    @Override
    protected int getLayoutResource() {
        return R.layout.data_map_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        switchFragment(new DataMapFragment());
    }

    void switchFragment(BasicFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map_layout,fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
