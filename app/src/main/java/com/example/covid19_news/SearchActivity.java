package com.example.covid19_news;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;

public class SearchActivity  extends BasicActivity{

    @BindView(R.id.search_toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_box)
    EditText searchbox;

    @Override
    protected int getLayoutResource() {
        return R.layout.search_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        searchbox.requestFocus();
        searchbox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH){
                    if(textView.getText().length()==0){
                        Toast.makeText(SearchActivity.this,"The input cannot be empty.",Toast.LENGTH_SHORT).show();
                    }else{
                        goSearch();
                    }
                    return true;
                }
                return false;
            }
        });
        switchFragment(new SearchhomeFragment());
    }

    private void switchFragment(BasicFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_layout,fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    void goSearch(){
        InputMethodManager imm=(InputMethodManager) searchbox.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchbox.getWindowToken(),0);
        //得到input search
        final String text= searchbox.getText().toString();
        // to do here add it in history

        //转到搜索结果页面
        switchFragment(new SearchResultFragment(text));
    }



}
