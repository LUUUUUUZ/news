package com.example.covid19_news;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final Context context;
    //一系列的news
    private List<News> data;
    private OnClick onclick;

    private NewsAdapter(Context context, OnClick onclick ){
        this.onclick=onclick;
        this.context=context.getApplicationContext();
    }

    static void updateHasRead(final View view){
        Resources.Theme theme = view.getContext().getTheme();
        final TypedValue colorHasRead = new TypedValue();
        theme.resolveAttribute(R.color.colorHasRead,colorHasRead,true);
        view.findViewById(R.id.news_card).setBackgroundResource(colorHasRead.resourceId);
    }

    static NewsAdapter newAdapter(final Context context, View view, OnClick onclick){
        final NewsAdapter adapter=new NewsAdapter(context,onclick);
        RecyclerView rv=(RecyclerView) view;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        return adapter;
    }
    public interface OnClick{
        void click(View view,int position,News news);
    }

    News get(int position){
        return data.get(position);
    }

    void clear(){
        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0,size);
    }

    void add(List<News> data,int position){
        this.data.addAll(position,data);
        notifyItemRangeInserted(position,data.size());
    }
    void add(List<News> data){
        add(data,this.data.size());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //根据position找到new设置view

        //如果新闻isread
        //updateHasRead(holder.itemView);


    }

    @Override
    public int getItemCount() {
        //return data.size();
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.publisher)
        TextView publisher;
        @BindView(R.id.publish_time)
        TextView publishTime;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        //这里点击新闻之后需要在user层面做一系列的事情
        //读出新闻整体
        //注意历史添加


    }
}
