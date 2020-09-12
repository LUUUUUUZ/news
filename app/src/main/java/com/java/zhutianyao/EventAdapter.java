package com.java.zhutianyao;

import android.content.Context;
import android.graphics.Color;
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
import butterknife.ButterKnife;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private final Context context;
    private List<Event> data;
    private OnClick onclick;


    private EventAdapter(Context context, OnClick onclick){
        data=new ArrayList<Event>();
        this.onclick=onclick;
        this.context=context.getApplicationContext();
    }

    static EventAdapter newAdapter(final Context context, View view,OnClick onclick){
        final EventAdapter adapter=new EventAdapter(context,onclick);
        RecyclerView rv=(RecyclerView) view;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        return adapter;
    }

    public interface OnClick{
        void click(View view,int position,Event event);
    }

    Event get(int position){
        return data.get(position);
    }

    void clear(){
        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0,size);
    }

    void add(List<Event> data,int position){
        this.data.addAll(position,data);
        notifyItemRangeInserted(position,data.size());
    }
    void add(List<Event> data){
        System.out.println("data.size() for now:"+data.size());
        add(data,this.data.size());
    }

    void add(Event data){
       add(data,this.data.size());
    }
    void add(Event data,int position){
        this.data.add(position,data);
        notifyItemInserted(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("!!!!!!!!!!!正在建立新的card");
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //根据position找到new设置view
        Event d=get(position);
        System.out.println("!!!!!!!!!!!正在给Card换颜色"+position+"??????");
        holder.title.setText(d.title);
        if(d.id.equals(data.get(0).id)){
            holder.title.setTextColor(Color.parseColor("#8D3D0C"));
            System.out.println("!!!!!!!!!!!"+position+"??????");
        }
        else{
            holder.title.setTextColor(Color.parseColor("#000000"));
        }
        holder.setOnclick(onclick,d);

    }

    @Override
    public int getItemCount() {
        return data.size();
//        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
       @BindView(R.id.event_title)
       TextView title;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        void setOnclick(final OnClick onClick, final Event event){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.click(v,getAdapterPosition(),event);
                }
            });
        }

    }
}
