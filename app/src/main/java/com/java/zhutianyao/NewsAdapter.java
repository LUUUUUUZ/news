package com.java.zhutianyao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final Context context;
    //一系列的news
    private List<News> data;
    private OnClick onclick;

    private NewsAdapter(Context context, OnClick onclick ){
        data=new ArrayList<News>();
        this.onclick=onclick;
        this.context=context.getApplicationContext();
    }
//
//    @SuppressLint("ResourceAsColor")
//    static void updateHasRead(final View view){
//        System.out.println("!!!!update has read!!!");
//        TextView t=view.findViewById(R.id.title);
//        t.setTextColor(R.color.colorHasRead);
//        TextView t2=view.findViewById(R.id.publisher);
//        t2.setTextColor(R.color.colorHasRead);
//        TextView t3=view.findViewById(R.id.publish_time);
//        t3.setTextColor(R.color.colorHasRead);
//
//    }
//
//    @SuppressLint("ResourceAsColor")
//    static void notupdateHasRead(final View view){
//        System.out.println("!!!!not update the read!!!");
//        TextView t=view.findViewById(R.id.title);
//        t.setTextColor(R.color.colorTitle);
//        TextView t2=view.findViewById(R.id.publisher);
//        t2.setTextColor(R.color.colorSubtitle);
//        TextView t3=view.findViewById(R.id.publish_time);
//        t3.setTextColor(R.color.colorSubtitle);
//
//    }

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
        System.out.println("data.size() for now:"+data.size());
        add(data,this.data.size());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //根据position找到new设置view
        News d=get(position);
        System.out.println("onBindView:"+d.isRead);
        holder.title.setText(d.title);
        //holder.title.setTextColor(Color.RED);
        holder.publisher.setText(d.publisher);
        holder.publishTime.setText(Util.parseTime(d.publishTime));
        holder.setOnclick(onclick,d);

        if(d.isRead){
            int color=context.getResources().getColor(R.color.colorHasRead);
            holder.title.setTextColor(color);
            holder.publishTime.setTextColor(color);
            holder.publisher.setTextColor(color);
            System.out.println("Set Grey.."+d.id+holder.itemView.getId());
        }else{
            int color=context.getResources().getColor(R.color.colorTitle);
            holder.title.setTextColor(color);
            int colors=context.getResources().getColor(R.color.colorSubtitle);
            holder.publishTime.setTextColor(colors);
            holder.publisher.setTextColor(colors);
            System.out.println("Not Set Grey.."+d.id+holder.itemView.getId());
        }
        //holder.publisher.setTextColor(0xff00ff00);
        //如果新闻isread
        //updateHasRead(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return data.size();
//        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.card_title)
        TextView title;
        @BindView(R.id.card_publisher)
        TextView publisher;
        @BindView(R.id.card_publish_time)
        TextView publishTime;

        ViewHolder(View itemView){
            super(itemView);
            //ButterKnife.bind(itemView);
            title=(TextView)itemView.findViewById(R.id.card_title);
            publisher=(TextView) itemView.findViewById(R.id.card_publisher);
            publishTime=(TextView)itemView.findViewById(R.id.card_publish_time);

        }

        void setOnclick(final OnClick onClick,final News news){
            itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    Global.addHistory(news);
                    onClick.click(v,getAdapterPosition(),news);
                    if(news.isRead){
                        int color=context.getResources().getColor(R.color.colorHasRead);
                        title.setTextColor(color);
                        publishTime.setTextColor(color);
                        publisher.setTextColor(color);
                    }

                }
            });
        }

        //这里点击新闻之后需要在user层面做一系列的事情
        //读出新闻整体
        //注意历史添加


    }
}
