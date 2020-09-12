package com.java.zhutianyao;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//chip的适配器，用于协调切换页面
class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ViewHolder> {

    private List<String> data;
    private boolean isEdit;
    private OnClick onclick;
    private Context context;

    private ChipAdapter(OnClick onClick, Context context){
        this.data=new ArrayList<>();
        this.isEdit=false;
        this.onclick=onClick;
        this.context=context;
    }

    static ChipAdapter newAdapter(Context context,View view,OnClick onclick){
        ChipAdapter adapter=new ChipAdapter(onclick,context);
        ChipsLayoutManager.Builder c=ChipsLayoutManager.newBuilder(context);
        RecyclerView rv=(RecyclerView) view;
        rv.setLayoutManager(c.build());
        rv.setAdapter(adapter);
        rv.addItemDecoration(new SpacingItemDecoration(20,20));
        return adapter;
    }

    List<String> getData(){return data;}

    boolean isEditable(){
        return isEdit;
    }

    String get(int position){return data.get(position);}

    void add(String s,int position){
        data.add(position,s);
        notifyItemInserted(position);
    }

    void add(String s){
        add(s,data.size());
    }

    void add(List<String> data,int position){
        this.data.addAll(position,data);
        notifyItemRangeInserted(position,data.size());
    }

    void add(List<String> data){
        add(data,this.data.size());
    }

    void remove(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    void move(int from,int to){
        Collections.swap(data,from,to);
        notifyItemMoved(from,to);
    }

    void clear(){
        int sz=data.size();
        data.clear();
        notifyItemRangeRemoved(0,sz);
    }

    boolean toggleEdit(){
        isEdit = !isEdit;
        notifyItemRangeChanged(0,data.size());
        return isEdit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //当需要新的ViewHolder来显示列表项时，调用产生新的ViewHolder
        Chip view=new Chip(new ContextThemeWrapper(parent.getContext(),R.style.Theme_MaterialComponents_Light));
        view.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        view.setChipCornerRadius(10);
        return new ViewHolder(view,onclick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //将数据绑定在ViewHolder上
        Chip chip=holder.chip;
        chip.setText(data.get(position));
        chip.setCloseIconVisible(isEdit);
    }

    @Override
    public int getItemCount() {
        //总共要显示的列表数量
        return data.size();
    }

    public interface OnClick{
        void click(Chip chip,int position);

        void close(Chip chip,int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        Chip chip;
        ViewHolder(View itemView, final OnClick onclick){
            super(itemView);
            chip = (Chip) itemView;
            chip.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onclick.click((Chip)view,getAdapterPosition());
                }
            });
            chip.setOnCloseIconClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    onclick.close((Chip)view,getAdapterPosition());
                }
            });
        }
    }
}
