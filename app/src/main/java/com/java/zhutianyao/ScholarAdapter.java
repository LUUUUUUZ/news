package com.java.zhutianyao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScholarAdapter extends RecyclerView.Adapter<ScholarAdapter.ViewHolder> {

    private final Context context;
    private List<Scholar> data;
    private OnClick onclick;
    protected boolean loaded;

    private ScholarAdapter(Context context, OnClick onclick ){
        data=new ArrayList<Scholar>();
        this.onclick=onclick;
        this.context=context.getApplicationContext();
        loaded=false;
    }


    static ScholarAdapter newAdapter(final Context context, View view, OnClick onclick){
        final ScholarAdapter adapter=new ScholarAdapter(context,onclick);
        RecyclerView rv=(RecyclerView) view;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        return adapter;
    }
    public interface OnClick{
        void click(View view,int position,Scholar scholar);
    }

    Scholar get(int position){
        return data.get(position);
    }

    void clear(){
        int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0,size);
    }

    void add(List<Scholar> data,int position){
        this.data.addAll(position,data);
        notifyItemRangeInserted(position,data.size());
    }
    void add(List<Scholar> data){
        System.out.println("data.size() for now:"+data.size());
        add(data,this.data.size());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view= LayoutInflater.from(parent.getContext()).inflate(R.layout.scholar_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //根据position找到new设置view
        Scholar d=get(position);
        holder.avater.setImageBitmap(d.avatar);
        if(d.name_zh.isEmpty()){
            holder.name.setText(d.name);
        }else {
            holder.name.setText(d.name_zh);
        }
        System.out.println("!!!!"+d.hindex);

        holder.hindex.setText(String.valueOf(d.hindex));
        holder.activity.setText(String.format("%.2f",d.activity));
        holder.risingStar.setText(String.format("%.2f",d.risingStar));
        holder.citations.setText(String.valueOf(d.citations));
        holder.pubs.setText(String.valueOf(d.pubs));
        holder.position.setText(d.prf.position);
        if(d.prf.affiliation_zh.isEmpty()){
            holder.affiliation.setText(d.prf.affiliation);
        }else{
            holder.affiliation.setText(d.prf.affiliation_zh);
        }
        holder.setOnclick(onclick,d);

    }

    @Override
    public int getItemCount() {
        return data.size();
//        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
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

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        void setOnclick(final OnClick onClick,final Scholar scholar){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClick.click(v,getAdapterPosition(),scholar);
                }
            });
        }


    }
}
