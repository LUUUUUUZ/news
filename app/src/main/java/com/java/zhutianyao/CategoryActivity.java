package com.java.zhutianyao;

import butterknife.BindView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

public class CategoryActivity extends SwipeActivity{

    @BindView(R.id.category_edit_current)
    TextView editCurrent;
    @BindView(R.id.category_close)
    ImageButton closebutton;
    @BindView(R.id.category_current_layout)
    RecyclerView currentView;
    int selectPosition=-1;

    boolean hasEdited=false;

    private ChipAdapter remainAdapter;
    private ChipAdapter currentAdapter;
    private ItemTouchHelper itemTouchHelper;


    @Override
    protected int getLayoutResource() {
        return R.layout.category_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        remainAdapter= ChipAdapter.newAdapter(this, findViewById(R.id.category_remain_layout), new ChipAdapter.OnClick() {
            @Override
            public void click(Chip chip, int position) {
                currentAdapter.add(remainAdapter.get(position));
                remainAdapter.remove(position);
                hasEdited=true;
                //更新用户记录的列表项
                Global.updateTypes(currentAdapter.getData());
            }

            @Override
            public void close(Chip chip, int position) {

            }
        });

        currentAdapter=ChipAdapter.newAdapter(this, currentView, new ChipAdapter.OnClick() {
            @Override
            public void click(Chip chip, int position) {
                selectPosition =position;
                finish();
            }

            @Override
            public void close(Chip chip, int position) {
                remainAdapter.add(currentAdapter.get(position));
//                Global.cancelType(currentAdapter.get(position));
                currentAdapter.remove(position);

            }
        });

        currentView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                switch (e.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        if(currentAdapter.isEditable())
                            //正在更改不能左滑直到完成
                            consumer.lockLeft();
                        break;
                    case MotionEvent.ACTION_UP:
                        consumer.unlockLeft();
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        itemTouchHelper =new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                if(currentAdapter.isEditable()){
                    return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                            ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT);
                }
                return 0;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                if(currentAdapter.isEditable()){
                    currentAdapter.move(viewHolder.getAdapterPosition(),target.getAdapterPosition());
                }
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return false;
            }
        });

        itemTouchHelper.attachToRecyclerView(currentView);

        editCurrent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(currentAdapter.toggleEdit()){
                    editCurrent.setText("Complete");
                }else{
                    editCurrent.setText("Edit");
                    hasEdited=true;
                    //更新用户列表
                    //to do here
                    Global.updateTypes(currentAdapter.getData());
                }
            }
        });

        closebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initData();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("hasEdited",hasEdited);
        data.putExtra("selectPosition",selectPosition);
        setResult(RESULT_OK,data);
        super.finish();
    }

    void initData(){
        //从用户那里get现在选择的，和剩下的
        //test below:
        List<String> chosen=Global.getChosenTypes();
        currentAdapter.add(chosen);
        List<String> remain=Global.getRemainTypes();
        remainAdapter.add(remain);
    }
}
